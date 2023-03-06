package edu.tum.romance.whatsthis.ui.panels

import edu.tum.romance.whatsthis.ui.ClassificationFrame
import java.awt.Dimension
import java.awt.event.KeyEvent
import javax.swing.*

object ClassyPanel: JPanel() {
    // TODO: Change to VecClouds
    val classes = mutableListOf("(None)")
    // TODO: Change to ...?
    @Suppress("unused")
    private val samples = mutableListOf<Pair<String, String>>()

    private const val maxY = ClassificationFrame.height
    private const val maxX = ClassificationFrame.width
    private val horizontalDimensions = arrayOf(
        7,  // ClassInput
        1,  // AddClassButton
        14, // SampleNameInput
        1,  // AddSampleButton
        1   // ClassifyButton
    ).map { (it * maxX / 24.0).toInt() }
    private val verticalDimensions = arrayOf(
        6.25, // Editor
        0.5, // ClassInput
        3.25  // ClassList
    ).map { (it * maxY / 10.0).toInt() }

    init {
        val layout = GroupLayout(this)
        this.layout = layout
        layout.autoCreateGaps = true
        layout.autoCreateContainerGaps = true

        layout.setHorizontalGroup(
            layout.createParallelGroup()
                .addComponent(Editor)
                .addGroup(layout.createSequentialGroup()
                    .addGroup(layout.createParallelGroup()
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(ClassInput)
                            .addComponent(AddClassButton)
                        )
                        .addComponent(ClassList)
                    )
                    .addGroup(layout.createParallelGroup()
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(SampleNameInput)
                            .addComponent(AddSampleButton)
                            .addComponent(ClassifyButton)
                        )
                        .addComponent(SampleList)
                    )
                )
        )

        layout.setVerticalGroup(
            layout.createSequentialGroup()
                .addComponent(Editor)
                .addGroup(layout.createParallelGroup()
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup()
                            .addComponent(ClassInput)
                            .addComponent(AddClassButton)
                        )
                        .addComponent(ClassList)
                    )
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup()
                            .addComponent(SampleNameInput)
                            .addComponent(AddSampleButton)
                            .addComponent(ClassifyButton)
                        )
                        .addComponent(SampleList)
                    )
                )
        )

        ClassList.update()
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
        var xSize = 0
        for (i in x) {
            xSize += horizontalDimensions[i]
        }
        var ySize = 0
        for (i in y) {
            ySize += verticalDimensions[i]
        }
        return Dimension(xSize, ySize)
    }
}

//#region Core Components
private object Editor: JTextArea() {
    val x = 0..4
    val y = 0..0
    init {
        isEditable = true
        preferredSize = ClassyPanel.computeSize(x, y)
        font = ClassificationFrame.fonts[0]
    }
}

private object ClassInput: JTextField() {
    val x = 0..0
    val y = 1..1
    var editing = -1
    init {
        preferredSize = ClassyPanel.computeSize(x, y)
        addActionListener { submit() }
        font = ClassificationFrame.fonts[0]
    }

    fun submit() {
        if(text.isNotBlank()) {
            if(editing != -1) {
                ClassyPanel.classes[editing] = text
                editing = -1
            } else {
                ClassyPanel.classes.add(text)
            }
            ClassList.update()
            text = ""
        }
    }
}

private object AddClassButton: JButton("⏎") {
    val x = 1..1
    val y = 1..1
    init {
        preferredSize = ClassyPanel.computeSize(x, y)
        addActionListener { ClassInput.submit() }
    }
}

private object SampleNameInput: JTextField() {
    val x = 2..2
    val y = 1..1
    init {
        preferredSize = ClassyPanel.computeSize(x, y)
        font = ClassificationFrame.fonts[0]
    }
}

private object AddSampleButton: JButton("⏎") {
    val x = 3..3
    val y = 1..1
    init {
        preferredSize = ClassyPanel.computeSize(x, y)
    }
}

private object ClassifyButton: JButton("✓") {
    val x = 4..4
    val y = 1..1
    init {
        preferredSize = ClassyPanel.computeSize(x, y)
    }
}

private object ClassList: JScrollPane() {
    val x = 0..1
    val y = 2..2
    val list = JList(ClassyPanel.classes.toTypedArray())
    init {
        preferredSize = ClassyPanel.computeSize(x, y)
        list.model = DefaultListModel()
        viewport.view = list
        list.font = ClassificationFrame.fonts[0]

        list.addListSelectionListener {
            if(!it.valueIsAdjusting) {
                if(list.selectedValue != null && list.selectedValue != "(None)") {
                    ClassInput.text = list.selectedValue as String
                    ClassInput.editing = list.selectedIndex
                } else if(ClassInput.editing != -1) {
                    ClassInput.text = ""
                    ClassInput.editing = -1
                }
            }
        }
    }

    fun update() {
        list.model = DefaultListModel()
        for (i in ClassyPanel.classes) {
            (list.model as DefaultListModel).addElement(i)
        }
    }
}

private object SampleList: JScrollPane() {
    val x = 2..4
    val y = 2..2
    init {
        preferredSize = ClassyPanel.computeSize(x, y)
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