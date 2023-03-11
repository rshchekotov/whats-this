package edu.tum.romance.whatsthis.ui.panels.main.components

import edu.tum.romance.whatsthis.nlp.API
import edu.tum.romance.whatsthis.ui.ClassificationFrame
import edu.tum.romance.whatsthis.ui.component.HintTextField

object ClassInput: HintTextField("Class Name") {
    init {
        addActionListener { submit() }
        font = ClassificationFrame.fonts[0]
    }

    fun submit() {
        if(text.isNotBlank()) {
            if(text in API.spaces) {
                ClassificationFrame.visualError("Class '$text' already exists!")
                return
            }
            API.alterSpace(text)
            ClassList.update()
            text = ""
        } else {
            ClassificationFrame.visualError("Class name cannot be blank!")
        }
    }
}