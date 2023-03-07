package edu.tum.romance.whatsthis.ui.panels

import edu.tum.romance.whatsthis.io.TextData
import edu.tum.romance.whatsthis.nlp.Monitor
import edu.tum.romance.whatsthis.ui.ClassificationFrame
import edu.tum.romance.whatsthis.ui.ClassificationFrame.visualError
import edu.tum.romance.whatsthis.ui.component.HintTextField
import java.awt.Dimension
import java.awt.event.ActionEvent
import java.awt.event.ItemEvent
import java.awt.event.KeyEvent
import java.net.URL
import java.util.*
import javax.swing.*
import javax.swing.table.DefaultTableModel
import javax.swing.table.TableRowSorter

//#region Core Components
private object Editor: JScrollPane(
    JTextArea(),
    VERTICAL_SCROLLBAR_AS_NEEDED,
    HORIZONTAL_SCROLLBAR_NEVER
) {
    val textArea = viewport.view as JTextArea
    val table = JTable()

    var content: String
        get() = textArea.text
        set(value) {
            textArea.text = value
            update()
        }

    init {
        textArea.isEditable = true
        textArea.lineWrap = true
        textArea.font = ClassificationFrame.fonts[0]

        table.fillsViewportHeight = true
        table.autoCreateRowSorter = true
        table.autoResizeMode = JTable.AUTO_RESIZE_OFF
        table.font = ClassificationFrame.fonts[0]
        table.rowHeight = 20
    }

    fun update() {
        if(content.isNotBlank()) {
            if(ViewMenu.mode == ViewMenu.VEC) {
                val data = TextData(content)

                data.tokens.map { arrayOf(it.first, it.second) }.toTypedArray().let {
                    table.model = DefaultTableModel(it, arrayOf("Word", "Vector"))
                }

                val scrollerWidth = verticalScrollBar.preferredSize.width
                val wordColumn = table.getColumn("Word")
                val vectorColumn = table.getColumn("Vector")
                val preferredWidth = Editor.preferredSize.width - scrollerWidth
                wordColumn.preferredWidth = 2 * preferredWidth / 3
                vectorColumn.preferredWidth = preferredWidth / 3

                val sorter = TableRowSorter(table.model)
                sorter.setComparator(wordColumn.modelIndex, Comparator.comparing(String::lowercase))
                sorter.setComparator(vectorColumn.modelIndex, Comparator.comparingInt(Int::toInt))
                table.rowSorter = sorter

                viewport.view = table
            } else {
                viewport.view = textArea
            }
        }
    }

    fun isBlank(): Boolean = textArea.text.isBlank()
}

private object SourceButton: JButton("⏎") {
    init {
        toolTipText = "Load data from source"
        addActionListener { (SourceSelector.card as Runnable).run() }
    }

    override fun createToolTip(): JToolTip {
        val toolTip = super.createToolTip()
        toolTip.font = ClassificationFrame.fonts[0]
        return toolTip
    }
}

private object SourceSelector: JComboBox<String>() {
    val cards = mapOf(
        "URL" to object : HintTextField("URL"), Runnable {
            init {
                addActionListener { run() }
                font = ClassificationFrame.fonts[0]
            }

            override fun run() {
                if(text.isNotBlank()) {
                    try {
                        val content = TextData(URL(text))
                        Editor.content = content.text
                        text = ""
                    } catch(e: Exception) {
                        // TODO: Improve error handling
                        Editor.content = "Error: ${e.message}"
                    }
                }
            }
        },
        "File" to object : JButton("Browse"), Runnable {
            val fileChooser = JFileChooser()
            init {
                fileChooser.fileSelectionMode = JFileChooser.FILES_ONLY
                fileChooser.addActionListener {
                    val file = fileChooser.selectedFile
                    if (file != null) {
                        try {
                            val content = TextData(file)
                            Editor.content = content.text
                            SampleNameInput.text = file.nameWithoutExtension
                        } catch (e: Exception) {
                            Editor.content = "Error: ${e.message}"
                        }
                    }
                }

                font = ClassificationFrame.fonts[0]
                addActionListener { run() }
            }

            override fun run() {
                fileChooser.showOpenDialog(ClassificationFrame)
            }
        }
    )
    val card
        get() = cards[selectedItem]
    val options = cards.keys.toList()

    init {
        font = ClassificationFrame.fonts[0]
        toolTipText = "Select a source to load data from"
        options.forEach(::addItem)

        addActionListener {
            ClassyPanel.remove(ClassyPanel.sourceInputElement)
            ClassyPanel.sourceInputElement = card as JComponent
            ClassyPanel.add(ClassyPanel.sourceInputElement)
            ClassyPanel.sourceInputElement.preferredSize = ClassyPanel.computeSize(0..2, 1..1)
            val layout = ClassyPanel.layout as SpringLayout
            layout.putConstraint(SpringLayout.NORTH, ClassyPanel.sourceInputElement, 5, SpringLayout.SOUTH, Editor)
            layout.putConstraint(SpringLayout.WEST, ClassyPanel.sourceInputElement, 5, SpringLayout.WEST, ClassyPanel)
            layout.putConstraint(SpringLayout.EAST, ClassyPanel.sourceInputElement, 0, SpringLayout.EAST, SampleNameInput)
            layout.putConstraint(SpringLayout.SOUTH, ClassyPanel.sourceInputElement, 0, SpringLayout.SOUTH, SourceSelector)
            ClassyPanel.revalidate()
            ClassyPanel.repaint()
        }
    }

    override fun createToolTip(): JToolTip {
        val toolTip = super.createToolTip()
        toolTip.font = ClassificationFrame.fonts[0]
        return toolTip
    }
}

private object ClassInput: HintTextField("Class Name") {
    init {
        addActionListener { submit() }
        font = ClassificationFrame.fonts[0]
    }

    fun submit() {
        if(text.isNotBlank()) {
            if(text in Monitor) {
                visualError("Class '$text' already exists!")
                return
            }
            Monitor.cloud(text)
            ClassList.update()
            text = ""
        } else {
            visualError("Class name cannot be blank!")
        }
    }
}

private object AddClassButton: JButton("⏎") {
    init {
        toolTipText = "Add or edit a class"
        addActionListener { ClassInput.submit() }
    }

    override fun createToolTip(): JToolTip {
        val toolTip = super.createToolTip()
        toolTip.font = ClassificationFrame.fonts[0]
        return toolTip
    }
}

private object SampleNameInput: HintTextField("Sample Name") {
    init {
        font = ClassificationFrame.fonts[0]
        addActionListener { submit() }
    }

    fun submit() {
        if(text.isBlank()) {
            visualError("Sample name cannot be blank!")
            return
        }

        if(Editor.isBlank()) {
            visualError("Sample text cannot be blank!")
            return
        }

        if(text in Monitor.cacheKeys()) {
            val label = JLabel("Sample '$text' already exists. Overwrite?")
            label.font = ClassificationFrame.fonts[0]
            val override = JOptionPane.showConfirmDialog(
                ClassificationFrame, label,
                "SampleConfirmation", JOptionPane.YES_NO_OPTION
            )
            if(override != JOptionPane.YES_OPTION) {
                return
            }
        }

        val sample = TextData(Editor.content)
        val className = ClassList.list.selectedValue
        if(className != null) {
            Monitor.add(sample, className)
        } else {
            val label = JLabel("No class selected. Create variable sample?")
            label.font = ClassificationFrame.fonts[0]
            val classlessAdd = JOptionPane.showConfirmDialog(
                ClassificationFrame, label,
                "ClassConfirmation", JOptionPane.YES_NO_OPTION
            )
            if(classlessAdd != JOptionPane.YES_OPTION) return
        }
        Monitor.addToCache(text, sample)
        SampleList.update()
    }
}

private object AddSampleButton: JButton("⏎") {
    init {
        addActionListener { SampleNameInput.submit() }
        toolTipText = "Add or edit the sample text and add it to the selected class"
    }
    override fun createToolTip(): JToolTip {
        val toolTip = super.createToolTip()
        toolTip.font = ClassificationFrame.fonts[0]
        return toolTip
    }
}

private object ClassifyButton: JButton("✓") {
    init {
        toolTipText = "Classify the sample text"
        addActionListener { ClassificationFrame.unimplemented() }
    }

    override fun createToolTip(): JToolTip {
        val toolTip = super.createToolTip()
        toolTip.font = ClassificationFrame.fonts[0]
        return toolTip
    }
}

private object ClassList: JScrollPane() {
    val list = JList(Monitor.cloudKeys().sorted().toTypedArray())
    init {
        list.model = DefaultListModel()
        viewport.view = list
        list.font = ClassificationFrame.fonts[0]

        list.addListSelectionListener {
            if(!it.valueIsAdjusting) {
                SampleList.update()
            }
        }
    }

    fun update() {
        list.model = DefaultListModel()
        for (i in Monitor.cloudKeys().sorted()) {
            (list.model as DefaultListModel).addElement(i)
        }
    }
}

private object SampleList: JScrollPane() {
    val list = JList(Monitor.cacheKeys().sorted().toTypedArray())
    init {
        list.model = DefaultListModel()
        viewport.view = list
        list.font = ClassificationFrame.fonts[0]

        // On 'delete'-press, delete the selected index
        list.inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), "delete")
        list.actionMap.put("delete", object: AbstractAction() {
            override fun actionPerformed(e: ActionEvent) {
                val sampleName = list.selectedValue
                if(sampleName != null) {
                    val sample = Monitor.loadFromCache(sampleName)
                    if(sample == null) {
                        visualError("Sample '$sampleName' does not exist!")
                        return
                    }

                    val cloud = ClassList.list.selectedValue
                    if(cloud != null && (cloud in Monitor)) {
                        Monitor.remove(sample, cloud)
                    }
                    Monitor.removeFromCache(sampleName)
                }
            }
        })

        list.addListSelectionListener {
            if(!it.valueIsAdjusting) {
                if(list.selectedValue != null) {
                    val sample = Monitor.loadFromCache(list.selectedValue)
                    if(sample != null) {
                        Editor.content = sample.text
                    }
                }
            }
        }
    }

    fun update() {
        list.model = DefaultListModel()
        val model = list.model as DefaultListModel
        var samples = Monitor.cacheKeys().sorted()
        val classFilter = ClassList.list.selectedValue
        if(classFilter != null && (classFilter in Monitor)) {
            samples = samples.filter {
                Monitor[classFilter]!!.cloud.contains(Monitor.loadFromCache(it))
            }
        }
        samples.forEach { model.addElement(it) }
    }
}
//#endregion

//#region Menu Bar
private object MenuBar: JMenuBar() {
    init {
        add(FileMenu)
        add(ViewMenu)
    }
}

private object FileMenu: JMenu("File") {
    init {
        mnemonic = KeyEvent.VK_F
        font = ClassificationFrame.fonts[0]

        val saveItem = JMenuItem("Save")
        saveItem.font = ClassificationFrame.fonts[0]
        saveItem.mnemonic = KeyEvent.VK_S
        saveItem.addActionListener { ClassificationFrame.unimplemented() }
        add(saveItem)

        val loadItem = JMenuItem("Load")
        loadItem.font = ClassificationFrame.fonts[0]
        loadItem.mnemonic = KeyEvent.VK_L
        loadItem.addActionListener { ClassificationFrame.unimplemented() }
        add(loadItem)
    }
}

private object ViewMenu: JMenu("View") {
    const val TEXT = 0
    const val VEC = 1

    var mode = TEXT

    init {
        mnemonic = KeyEvent.VK_V
        font = ClassificationFrame.fonts[0]

        val textView = JCheckBoxMenuItem("Text View")

        textView.state = true
        textView.font = ClassificationFrame.fonts[0]
        textView.mnemonic = KeyEvent.VK_T
        add(textView)

        val vecView = JCheckBoxMenuItem("Vector View")
        vecView.font = ClassificationFrame.fonts[0]
        vecView.mnemonic = KeyEvent.VK_V
        add(vecView)

        textView.addItemListener {
            if(it.stateChange == ItemEvent.SELECTED) {
                mode = TEXT
                vecView.state = false
                Editor.update()
            } else {
                mode = VEC
            }
        }

        vecView.addItemListener {
            if(it.stateChange == ItemEvent.SELECTED) {
                mode = VEC
                textView.state = false
                Editor.update()
            } else {
                mode = TEXT
            }
        }
    }
}
//#endregion

object ClassyPanel: JPanel() {
    private const val maxY = ClassificationFrame.height
    private const val maxX = ClassificationFrame.width

    private val horizontalWeights = arrayOf(
        7,  // ClassInput
        2,  // AddClassButton
        14, // SampleNameInput
        2,  // AddSampleButton
        3   // ClassifyButton
    )

    private val verticalWeights = arrayOf(
        5.75, // Editor
        0.5,  // SourceInput
        0.5,  // ClassInput
        3.25  // ClassList
    )

    var sourceInputElement: JComponent

    private val horizontalDimensions = horizontalWeights.map { (it * (maxX - 10.0) / horizontalWeights.sum()).toInt() }
    private val verticalDimensions = verticalWeights.map { (it * (maxY - 10.0) / verticalWeights.sum()).toInt() }

    init {
        val layout = SpringLayout()
        this.layout = layout
        this.preferredSize = Dimension(maxX, maxY)

        add(Editor)
        Editor.preferredSize = computeSize(0..4, 0..0)
        layout.putConstraint(SpringLayout.NORTH, Editor, 5, SpringLayout.NORTH, this)
        layout.putConstraint(SpringLayout.WEST, Editor, 5, SpringLayout.WEST, this)
        layout.putConstraint(SpringLayout.EAST, Editor, -5, SpringLayout.EAST, this)

        add(SourceSelector)
        SourceSelector.preferredSize = computeSize(4..4, 1..1)
        layout.putConstraint(SpringLayout.NORTH, SourceSelector, 5, SpringLayout.SOUTH, Editor)
        layout.putConstraint(SpringLayout.EAST, SourceSelector, -5, SpringLayout.EAST, this)

        add(SourceButton)
        SourceButton.preferredSize = computeSize(3..3, 1..1)
        layout.putConstraint(SpringLayout.NORTH, SourceButton, 5, SpringLayout.SOUTH, Editor)
        layout.putConstraint(SpringLayout.EAST, SourceButton, -5, SpringLayout.WEST, SourceSelector)

        sourceInputElement = SourceSelector.card as JComponent
        add(sourceInputElement)
        sourceInputElement.preferredSize = computeSize(0..2, 1..1)
        layout.putConstraint(SpringLayout.NORTH, sourceInputElement, 5, SpringLayout.SOUTH, Editor)
        layout.putConstraint(SpringLayout.WEST, sourceInputElement, 5, SpringLayout.WEST, this)
        layout.putConstraint(SpringLayout.EAST, sourceInputElement, -5, SpringLayout.WEST, SourceButton)
        layout.putConstraint(SpringLayout.SOUTH, sourceInputElement, 0, SpringLayout.SOUTH, SourceSelector)

        add(ClassInput)
        ClassInput.preferredSize = computeSize(0..0, 2..2)
        layout.putConstraint(SpringLayout.NORTH, ClassInput, 5, SpringLayout.SOUTH, SourceSelector)
        layout.putConstraint(SpringLayout.WEST, ClassInput, 5, SpringLayout.WEST, this)

        add(AddClassButton)
        AddClassButton.preferredSize = computeSize(1..1, 2..2)
        layout.putConstraint(SpringLayout.NORTH, AddClassButton, 5, SpringLayout.SOUTH, SourceSelector)
        layout.putConstraint(SpringLayout.WEST, AddClassButton, 5, SpringLayout.EAST, ClassInput)

        add(SampleNameInput)
        SampleNameInput.preferredSize = computeSize(2..2, 2..2)
        layout.putConstraint(SpringLayout.NORTH, SampleNameInput, 5, SpringLayout.SOUTH, SourceSelector)
        layout.putConstraint(SpringLayout.WEST, SampleNameInput, 5, SpringLayout.EAST, AddClassButton)
        layout.putConstraint(SpringLayout.EAST, SampleNameInput, -5, SpringLayout.WEST, SourceButton)

        add(AddSampleButton)
        AddSampleButton.preferredSize = computeSize(3..3, 2..2)
        layout.putConstraint(SpringLayout.NORTH, AddSampleButton, 5, SpringLayout.SOUTH, SourceSelector)
        layout.putConstraint(SpringLayout.WEST, AddSampleButton, 0, SpringLayout.WEST, SourceButton)
        layout.putConstraint(SpringLayout.EAST, AddSampleButton, 0, SpringLayout.EAST, SourceButton)

        add(ClassifyButton)
        ClassifyButton.preferredSize = computeSize(4..4, 2..2)
        layout.putConstraint(SpringLayout.NORTH, ClassifyButton, 5, SpringLayout.SOUTH, SourceSelector)
        layout.putConstraint(SpringLayout.WEST, ClassifyButton, 5, SpringLayout.EAST, AddSampleButton)
        layout.putConstraint(SpringLayout.EAST, ClassifyButton, -5, SpringLayout.EAST, this)

        add(ClassList)
        ClassList.preferredSize = computeSize(0..1, 3..3)
        layout.putConstraint(SpringLayout.NORTH, ClassList, 5, SpringLayout.SOUTH, ClassInput)
        layout.putConstraint(SpringLayout.WEST, ClassList, 5, SpringLayout.WEST, this)
        layout.putConstraint(SpringLayout.EAST, ClassList, 0, SpringLayout.EAST, AddClassButton)
        layout.putConstraint(SpringLayout.SOUTH, ClassList, -5, SpringLayout.SOUTH, this)

        add(SampleList)
        SampleList.preferredSize = computeSize(2..4, 3..3)
        layout.putConstraint(SpringLayout.NORTH, SampleList, 5, SpringLayout.SOUTH, ClassInput)
        layout.putConstraint(SpringLayout.WEST, SampleList, 5, SpringLayout.EAST, ClassList)
        layout.putConstraint(SpringLayout.EAST, SampleList, -5, SpringLayout.EAST, this)
        layout.putConstraint(SpringLayout.SOUTH, SampleList, -5, SpringLayout.SOUTH, this)

        ClassList.update()
        SampleList.update()
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        if(enabled) {
            ClassificationFrame.jMenuBar = MenuBar
        } else {
            ClassificationFrame.jMenuBar = null
        }
    }

    fun computeSize(x: IntRange, y: IntRange): Dimension {
        val xSize = horizontalDimensions.slice(x).sum()
        val ySize = verticalDimensions.slice(y).sum()
        return Dimension(xSize, ySize)
    }
}