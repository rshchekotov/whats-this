package edu.tum.romance.whatsthis.ui.panels.main.components

import edu.tum.romance.whatsthis.nlp.Monitor
import edu.tum.romance.whatsthis.ui.ClassificationFrame
import edu.tum.romance.whatsthis.ui.component.HintTextField

object ClassInput: HintTextField("Class Name") {
    init {
        addActionListener { submit() }
        font = ClassificationFrame.fonts[0]
    }

    fun submit() {
        if(text.isNotBlank()) {
            if(text in Monitor) {
                ClassificationFrame.visualError("Class '$text' already exists!")
                return
            }
            Monitor.createCloud(text)
            ClassList.update()
            text = ""
        } else {
            ClassificationFrame.visualError("Class name cannot be blank!")
        }
    }
}