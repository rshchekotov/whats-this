package edu.tum.romance.whatsthis.ui.views.main.components.space

import edu.tum.romance.whatsthis.nlp.API
import edu.tum.romance.whatsthis.ui.ClassificationFrame.visualError
import edu.tum.romance.whatsthis.ui.components.HintTextField
import edu.tum.romance.whatsthis.ui.views.main.MainView
import edu.tum.romance.whatsthis.util.observer.trigger

object SpaceInput: HintTextField("Enter space separated words"), Runnable {
    init {
        addActionListener { run() }
    }

    override fun run() {
        if(text.isBlank()) {
            visualError("Please give the vector space a name.")
            return
        }
        API.alterSpace(text)
        MainView.spaceUpdate.trigger()
    }
}