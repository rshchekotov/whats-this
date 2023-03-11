package edu.tum.romance.whatsthis.ui.panels.main.components

import edu.tum.romance.whatsthis.io.TextData
import edu.tum.romance.whatsthis.ui.ClassificationFrame
import edu.tum.romance.whatsthis.ui.component.HintTextField
import edu.tum.romance.whatsthis.ui.panels.main.ClassyPanel
import java.net.URL
import javax.swing.*

object SourceSelector: JComboBox<String>() {
    private val inputMethods = mapOf(
        "URL" to object : HintTextField("URL"), Runnable {
            init {
                addActionListener { run() }
                font = ClassificationFrame.fonts[0]
            }

            override fun run() {
                if(text.isNotBlank()) {
                    try {
                        val content = TextData(URL(text), "")
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
                            val content = TextData(file, "")
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
        get() = inputMethods[selectedItem]
    private val options = inputMethods.keys.toList()

    init {
        font = ClassificationFrame.fonts[0]
        toolTipText = "Select a source to load data from"
        options.forEach(::addItem)

        addActionListener { ClassyPanel.updateSourceInput() }
    }

    override fun createToolTip(): JToolTip {
        val toolTip = super.createToolTip()
        toolTip.font = ClassificationFrame.fonts[0]
        return toolTip
    }
}