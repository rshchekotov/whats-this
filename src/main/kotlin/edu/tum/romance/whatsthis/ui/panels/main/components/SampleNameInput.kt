package edu.tum.romance.whatsthis.ui.panels.main.components

import edu.tum.romance.whatsthis.io.TextData
import edu.tum.romance.whatsthis.nlp.Monitor
import edu.tum.romance.whatsthis.ui.ClassificationFrame
import edu.tum.romance.whatsthis.ui.component.HintTextField
import javax.swing.JLabel
import javax.swing.JOptionPane

object SampleNameInput: HintTextField("Sample Name") {
    init {
        font = ClassificationFrame.fonts[0]
        addActionListener { submit() }
    }

    fun submit() {
        if(text.isBlank()) {
            ClassificationFrame.visualError("Sample name cannot be blank!")
            return
        }

        if(Editor.isBlank()) {
            ClassificationFrame.visualError("Sample text cannot be blank!")
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