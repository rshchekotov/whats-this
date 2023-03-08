package edu.tum.romance.whatsthis.ui.panels

//#region Imports
import edu.tum.romance.whatsthis.io.TextData
import edu.tum.romance.whatsthis.nlp.Monitor
import edu.tum.romance.whatsthis.ui.ClassificationFrame
import edu.tum.romance.whatsthis.ui.ClassificationFrame.visualError
import edu.tum.romance.whatsthis.ui.component.HintTextField
import java.awt.Component
import java.awt.Dimension
import java.awt.Font
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.StringSelection
import java.awt.datatransfer.Transferable
import java.awt.event.ItemEvent
import java.awt.event.KeyEvent
import java.net.URL
import javax.swing.*
import javax.swing.table.AbstractTableModel
import javax.swing.table.DefaultTableModel
import javax.swing.table.TableCellEditor
import javax.swing.table.TableRowSorter

//#endregion

//#region Core Components
private object Editor: JScrollPane(
    JTextArea(),
    VERTICAL_SCROLLBAR_AS_NEEDED,
    HORIZONTAL_SCROLLBAR_NEVER
) {
    val textArea = viewport.view as JTextArea
    val table = JTable()

    val scrollerWidth = verticalScrollBar.preferredSize.width
    val preferredWidth
        get() = Editor.preferredSize.width - scrollerWidth

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
        table.model = DefaultTableModel(arrayOf<Array<String>>(), arrayOf("Word", "Vector"))

        val wordColumn = table.getColumn("Word")
        val vectorColumn = table.getColumn("Vector")
        wordColumn.preferredWidth = 2 * preferredWidth / 3
        vectorColumn.preferredWidth = preferredWidth / 3

        val sorter = TableRowSorter(table.model)
        sorter.sortsOnUpdates = true
        table.rowSorter = sorter
    }

    fun update() {
        if(content.isNotBlank()) {
            if(ViewMenu.mode == ViewMenu.VEC) {
                val data = TextData(content)

                (table.model as DefaultTableModel).setDataVector(
                    data.tokens.map { arrayOf(it.first, it.second) }.toTypedArray(),
                    arrayOf("Word", "Vector"))

                val wordColumn = table.getColumn("Word")
                val vectorColumn = table.getColumn("Vector")
                wordColumn.preferredWidth = 2 * preferredWidth / 3
                vectorColumn.preferredWidth = preferredWidth / 3

                val sorter = table.rowSorter as TableRowSorter
                sorter.setComparator(wordColumn.modelIndex, Comparator.comparing(String::lowercase))
                sorter.setComparator(vectorColumn.modelIndex, Comparator.comparingInt(Int::toInt))
                sorter.toggleSortOrder(vectorColumn.modelIndex)
                sorter.toggleSortOrder(vectorColumn.modelIndex)

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
                        SampleNameInput.text = content.titleSuggestion
                        text = ""
                    } catch(e: Exception) {
                        visualError("Error: ${e.message}")
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
                            SampleNameInput.text = content.titleSuggestion
                        } catch (e: Exception) {
                            visualError("Error: ${e.message}")
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
            Monitor.createCloud(text)
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
        val className = ClassList.list.selection()
        if(className != null) {
            Monitor.add(text, sample, className)
        } else {
            val label = JLabel("No class selected. Create variable sample?")
            label.font = ClassificationFrame.fonts[0]
            val classlessAdd = JOptionPane.showConfirmDialog(
                ClassificationFrame, label,
                "ClassConfirmation", JOptionPane.YES_NO_OPTION
            )
            if(classlessAdd != JOptionPane.YES_OPTION) return
            Monitor.add(text, sample)
        }

        text = ""
        Editor.content = ""
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
    var items = Monitor.cloudKeys().sorted().toMutableList()
    val list: VectorTable = VectorTable(VectorModel(
        "Classes",
        { idx, value -> items[idx] = value; items.sort() },
        { idx -> items[idx] },
        { old, new -> Monitor.renameCloud(old, new) },
        { name -> name in Monitor },
        { items.size }
    ))

    init {
        viewport.view = list
        list.selectionModel.addListSelectionListener {
            if(!it.valueIsAdjusting) {
                val selection = list.selection()
                if(selection != null) {
                    SampleList.update()
                }
            }
        }

        list.transferHandler = object: TransferHandler() {
            override fun canImport(support: TransferSupport): Boolean {
                return support.isDataFlavorSupported(DataFlavor.stringFlavor)
            }

            override fun importData(support: TransferSupport): Boolean {
                if(!canImport(support)) return false
                val transfer = support.transferable
                val data = transfer.getTransferData(DataFlavor.stringFlavor) as String
                val dropIndex = list.dropLocation.row
                if(dropIndex in items.indices) {
                    val className = items[dropIndex]
                    Monitor.assign(data, className)
                    SampleList.update()
                    return true
                }
                return false
            }
        }

        list.dragEnabled = true
    }

    fun update() {
        items = Monitor.cloudKeys().sorted().toMutableList()
        list.update()
    }
}

private object SampleList: JScrollPane() {
    var items = Monitor.cloudKeys().sorted().toMutableList()
    val list: VectorTable = VectorTable(VectorModel(
        "Samples",
        { idx, value -> items[idx] = value; items.sort() },
        { idx -> items[idx] },
        { old, new -> Monitor.renameInCache(old, new) },
        { name -> name in Monitor.cacheKeys() },
        { items.size }
    ))

    init {
        viewport.view = list
        list.selectionModel.addListSelectionListener {
            if(!it.valueIsAdjusting) {
                val selection = list.selection()
                if(selection != null) {
                    val sample = Monitor.loadFromCache(selection)
                    if(sample != null) {
                        Editor.content = sample.text
                    }
                }
            }
        }

        list.transferHandler = object: TransferHandler() {
            override fun getSourceActions(c: JComponent?): Int {
                return MOVE
            }

            override fun createTransferable(c: JComponent): Transferable {
                return StringSelection(list.selection().toString())
            }
        }

        list.dragEnabled = true
    }

    fun update() {
        items = Monitor.cacheKeys().sorted().toMutableList()
        val classFilter = ClassList.list.selection()
        if(classFilter != null && (classFilter in Monitor)) {
            items = items.filter {
                Monitor[classFilter]!!.contains(it)
            }.toMutableList()
        }
        list.update()
    }
}
//#endregion

//#region Reusable Components
class VectorModel(
    private val header: String,
    val setter: (Int, String) -> Unit,
    val getter: (Int) -> String,
    val rename: (String, String) -> Unit,
    val exists: (String) -> Boolean,
    val size: () -> Int
) : AbstractTableModel() {
    override fun getRowCount(): Int = size()
    override fun getColumnCount(): Int = 1
    override fun getValueAt(rowIndex: Int, columnIndex: Int): Any = getter(rowIndex)
    override fun getColumnName(column: Int): String = header
    override fun isCellEditable(rowIndex: Int, columnIndex: Int): Boolean = true
    override fun setValueAt(value: Any, rowIndex: Int, columnIndex: Int) {
        if (value is String) {
            if (rowIndex < rowCount) {
                val old = getter(rowIndex)
                if (exists(old)) {
                    rename(old, value)
                    setter(rowIndex, value)
                    fireTableDataChanged()
                }
            } else {
                visualError("Cannot rename data at index $rowIndex")
            }
        }
    }
    fun update() = fireTableDataChanged()
}

class VectorTable(model: VectorModel): JTable(model) {
    fun update() {
        (model as VectorModel).update()
    }

    fun selection(): String? {
        return when (selectedRow) {
            -1 -> null
            else -> getValueAt(selectedRow, 0) as String
        }
    }

    override fun getFont(): Font {
        return ClassificationFrame.fonts[0]
    }
    override fun prepareEditor(editor: TableCellEditor?, row: Int, column: Int): Component {
        val prepared = super.prepareEditor(editor, row, column)
        prepared.font = ClassificationFrame.fonts[0]
        return prepared
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

//#region Layout Management
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
//endregion