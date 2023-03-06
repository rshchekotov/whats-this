package edu.tum.romance.whatsthis.ui.panels

import edu.tum.romance.whatsthis.ui.ClassificationFrame
import edu.tum.romance.whatsthis.ui.component.HintTextField
import java.awt.Dimension
import java.awt.event.ActionEvent
import java.awt.event.KeyEvent
import javax.swing.*

object ClassyPanel: JPanel() {
    // TODO: Change to VecClouds
    val classes = mutableListOf("(No Filter)" to mutableListOf<Int>())
    // TODO: Change to ...?
    @Suppress("unused")
    val samples = mutableListOf("None" to "")

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

        add(SourceInput)
        SourceInput.preferredSize = computeSize(0..2, 1..1)
        layout.putConstraint(SpringLayout.NORTH, SourceInput, 5, SpringLayout.SOUTH, Editor)
        layout.putConstraint(SpringLayout.WEST, SourceInput, 5, SpringLayout.WEST, this)
        layout.putConstraint(SpringLayout.EAST, SourceInput, -5, SpringLayout.WEST, SourceButton)

        add(ClassInput)
        ClassInput.preferredSize = computeSize(0..0, 2..2)
        layout.putConstraint(SpringLayout.NORTH, ClassInput, 5, SpringLayout.SOUTH, SourceInput)
        layout.putConstraint(SpringLayout.WEST, ClassInput, 5, SpringLayout.WEST, this)

        add(AddClassButton)
        AddClassButton.preferredSize = computeSize(1..1, 2..2)
        layout.putConstraint(SpringLayout.NORTH, AddClassButton, 5, SpringLayout.SOUTH, SourceInput)
        layout.putConstraint(SpringLayout.WEST, AddClassButton, 5, SpringLayout.EAST, ClassInput)

        add(SampleNameInput)
        SampleNameInput.preferredSize = computeSize(2..2, 2..2)
        layout.putConstraint(SpringLayout.NORTH, SampleNameInput, 5, SpringLayout.SOUTH, SourceInput)
        layout.putConstraint(SpringLayout.WEST, SampleNameInput, 5, SpringLayout.EAST, AddClassButton)
        layout.putConstraint(SpringLayout.EAST, SampleNameInput, 0, SpringLayout.EAST, SourceInput)

        add(AddSampleButton)
        AddSampleButton.preferredSize = computeSize(3..3, 2..2)
        layout.putConstraint(SpringLayout.NORTH, AddSampleButton, 5, SpringLayout.SOUTH, SourceInput)
        layout.putConstraint(SpringLayout.WEST, AddSampleButton, 5, SpringLayout.EAST, SampleNameInput)
        layout.putConstraint(SpringLayout.EAST, AddSampleButton, 0, SpringLayout.EAST, SourceButton)

        add(ClassifyButton)
        ClassifyButton.preferredSize = computeSize(4..4, 2..2)
        layout.putConstraint(SpringLayout.NORTH, ClassifyButton, 5, SpringLayout.SOUTH, SourceInput)
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

    private fun computeSize(x: IntRange, y: IntRange): Dimension {
        val xSize = horizontalDimensions.slice(x).sum()
        val ySize = verticalDimensions.slice(y).sum()
        return Dimension(xSize, ySize)
    }
}

//#region Core Components
private object Editor: JTextArea() {
    init {
        isEditable = true
        font = ClassificationFrame.fonts[0]
    }
}

private object SourceInput: HintTextField("Source") {
    init {
        font = ClassificationFrame.fonts[0]
    }
}

private object SourceButton: JButton("⏎")

private object SourceSelector: JComboBox<String>() {
    init {
        font = ClassificationFrame.fonts[0]
        addItem("URL")
    }
}

private object ClassInput: HintTextField("Class Name") {
    var editing = -1
    init {
        addActionListener { submit() }
        font = ClassificationFrame.fonts[0]
    }

    fun submit() {
        if(text.isNotBlank()) {
            if(editing != -1) {
                ClassyPanel.classes[editing] = text to ClassyPanel.classes[editing].second
                editing = -1
            } else {
                ClassyPanel.classes.add(text to mutableListOf())
            }
            ClassList.update()
            text = ""
        }
    }
}

private object AddClassButton: JButton("⏎") {
    init {
        addActionListener { ClassInput.submit() }
    }
}

private object SampleNameInput: HintTextField("Sample Name") {
    init {
        font = ClassificationFrame.fonts[0]
        addActionListener { submit() }
    }

    fun submit() {
        if(text.isNotBlank() && Editor.text.isNotBlank()) {
            var index = SampleList.list.selectedIndex
            if(index == -1) {
                index = ClassyPanel.samples.indexOfFirst { it.first == text }
            }

            if(index != -1) {
                ClassyPanel.samples[index] = text to Editor.text
                SampleList.update()
            } else {
                ClassyPanel.samples.add(text to Editor.text)
                index = ClassyPanel.samples.size - 1
            }

            text = ""
            Editor.text = ""

            if(ClassInput.editing != -1) {
                ClassyPanel.classes[ClassInput.editing].second.add(index)
            } else {
                ClassyPanel.classes[0].second.add(index)
            }

            SampleList.update()
        }
    }
}

private object AddSampleButton: JButton("⏎") {
    init {
        addActionListener { SampleNameInput.submit() }
    }
}

private object ClassifyButton: JButton("✓")

private object ClassList: JScrollPane() {
    val list = JList(ClassyPanel.classes.map { it.first }.toTypedArray())
    init {
        list.model = DefaultListModel()
        viewport.view = list
        list.font = ClassificationFrame.fonts[0]

        list.addListSelectionListener {
            if(!it.valueIsAdjusting) {
                if(list.selectedValue != null && list.selectedValue != "(No Filter)") {
                    ClassInput.text = list.selectedValue
                    ClassInput.editing = list.selectedIndex
                } else if(ClassInput.editing != -1) {
                    ClassInput.text = ""
                    ClassInput.editing = -1
                }
                SampleList.update()
            }
        }
    }

    fun update() {
        list.model = DefaultListModel()
        for (i in ClassyPanel.classes) {
            (list.model as DefaultListModel).addElement(i.first)
        }
    }
}

private object SampleList: JScrollPane() {
    val list = JList(ClassyPanel.samples.map { it.first }.toTypedArray())
    init {
        list.model = DefaultListModel()
        viewport.view = list
        list.font = ClassificationFrame.fonts[0]

        // On 'delete'-press, delete the selected index
        list.inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), "delete")
        list.actionMap.put("delete", object: AbstractAction() {
            override fun actionPerformed(e: ActionEvent) {
                if(list.selectedIndex > 0) {
                    val idx = ClassyPanel.samples.indexOfFirst { item ->
                        item.first == list.selectedValue
                    }
                    ClassyPanel.samples.removeAt(idx)
                    for (i in ClassyPanel.classes) {
                        i.second.remove(idx)
                    }
                    SampleNameInput.text = ""
                    Editor.text = ""
                    update()
                }
            }
        })

        list.addListSelectionListener {
            if(!it.valueIsAdjusting) {
                if(list.selectedValue != null) {
                    if(list.selectedValue == "(None)") {
                        SampleNameInput.text = ""
                        Editor.text = ""
                        list.clearSelection()
                        return@addListSelectionListener
                    }

                    val idx = ClassyPanel.samples.indexOfFirst { item ->
                        item.first == list.selectedValue
                    }
                    SampleNameInput.text = ClassyPanel.samples[idx].first
                    Editor.text = ClassyPanel.samples[idx].second
                }
            }
        }
    }

    fun update() {
        list.model = DefaultListModel()
        for ((index, item) in ClassyPanel.samples.withIndex()) {
            if(ClassList.list.selectedIndex > 0) {
                if(ClassyPanel.classes[ClassList.list.selectedIndex].second.contains(index) || index == 0) {
                    (list.model as DefaultListModel).addElement(item.first)
                }
            } else {
                (list.model as DefaultListModel).addElement(item.first)
            }
        }
    }
}
//#endregion

//#region Menu Bar
private object MenuBar: JMenuBar() {
    init {
        add(FileMenu)
    }
}

private object FileMenu: JMenu("File") {
    init {
        mnemonic = KeyEvent.VK_F
        font = ClassificationFrame.fonts[0]

        val saveItem = JMenuItem("Save")
        saveItem.font = ClassificationFrame.fonts[0]
        saveItem.mnemonic = KeyEvent.VK_S
        add(saveItem)

        val loadItem = JMenuItem("Load")
        loadItem.font = ClassificationFrame.fonts[0]
        loadItem.mnemonic = KeyEvent.VK_L
        add(loadItem)
    }
}
//#endregion