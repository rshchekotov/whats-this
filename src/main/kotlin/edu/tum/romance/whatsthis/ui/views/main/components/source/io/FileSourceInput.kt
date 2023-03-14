package edu.tum.romance.whatsthis.ui.views.main.components.source.io

import edu.tum.romance.whatsthis.io.data.TextData
import edu.tum.romance.whatsthis.ui.ClassificationFrame
import edu.tum.romance.whatsthis.ui.ClassificationFrame.visualError
import edu.tum.romance.whatsthis.ui.components.StyledButton
import edu.tum.romance.whatsthis.ui.views.main.MainView
import javax.swing.JFileChooser

object FileSourceInput: StyledButton(0, "Browse Files...", null, { FileSourceInput.run() }), Runnable {
    private val fileChooser = JFileChooser()

    init {
        fileChooser.fileSelectionMode = JFileChooser.FILES_ONLY
        fileChooser.addActionListener {
            val file = fileChooser.selectedFile
            if(file != null) {
                val data = TextData(file, "")
                MainView.content.value = data
                if(MainView.contentName.value.isBlank()) {
                    MainView.contentName.value = data.titleSuggestion
                }
            } else {
                visualError("File I/O: Something went wrong.")
            }
        }

        addActionListener { run() }
    }

    override fun run() {
        fileChooser.showOpenDialog(ClassificationFrame)
    }
}