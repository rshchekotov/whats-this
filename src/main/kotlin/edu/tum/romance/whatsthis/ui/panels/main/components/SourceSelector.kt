package edu.tum.romance.whatsthis.ui.panels.main.components

import edu.tum.romance.whatsthis.io.TextData
import edu.tum.romance.whatsthis.ui.ClassificationFrame
import edu.tum.romance.whatsthis.ui.component.HintTextField
import edu.tum.romance.whatsthis.ui.panels.main.ClassyPanel
import java.net.URL
import javax.swing.*

object SourceSelector: JComboBox<String>() {
    private val cards = mapOf(
        "URL" to object : HintTextField("URL"), Runnable {
            init {
                addActionListener { run() }
                font = ClassificationFrame.fonts[0]
            }

            override fun run() {
                if(text.isNotBlank()) {
                    try {
                        val content = TextData(URL(text))
                        MainViewComponent.content = content.text
                        SampleNameInput.text = content.titleSuggestion
                        text = ""
                    } catch(e: Exception) {
                        ClassificationFrame.visualError("Error: ${e.message}")
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
                            MainViewComponent.content = content.text
                            SampleNameInput.text = content.titleSuggestion
                        } catch (e: Exception) {
                            ClassificationFrame.visualError("Error: ${e.message}")
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
    private val options = cards.keys.toList()

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
            layout.putConstraint(SpringLayout.NORTH, ClassyPanel.sourceInputElement, 5, SpringLayout.SOUTH, MainViewComponent)
            layout.putConstraint(SpringLayout.WEST, ClassyPanel.sourceInputElement, 5, SpringLayout.WEST, ClassyPanel)
            layout.putConstraint(
                SpringLayout.EAST,
                ClassyPanel.sourceInputElement, 0, SpringLayout.EAST, SampleNameInput
            )
            layout.putConstraint(
                SpringLayout.SOUTH,
                ClassyPanel.sourceInputElement, 0, SpringLayout.SOUTH, SourceSelector
            )
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