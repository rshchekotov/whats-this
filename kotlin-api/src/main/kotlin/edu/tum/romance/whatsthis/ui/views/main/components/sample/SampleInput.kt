package edu.tum.romance.whatsthis.ui.views.main.components.sample

import edu.tum.romance.whatsthis.ui.ClassificationFrame.visualError
import edu.tum.romance.whatsthis.ui.components.HintTextField
import edu.tum.romance.whatsthis.ui.views.main.MainView
import edu.tum.romance.whatsthis.util.observer.trigger

object SampleInput: HintTextField("Sample Name"), Runnable {
    init {
        addActionListener { run() }
        MainView.contentName.observe(1) {
            if(text != it.second) {
                text = it.second
            }
        }
    }

    override fun run() {
        if(text.isBlank()) {
            visualError("Sample name cannot be empty")
            return
        }
        MainView.contentName.value = text
        if(MainView.content.value == null) {
            visualError("No data loaded")
            return
        }
        MainView.dataUpdateAttempt.trigger()
    }
}