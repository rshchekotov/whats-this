package edu.tum.romance.whatsthis.ui.views.main.components.source.io

import edu.tum.romance.whatsthis.io.TextData
import edu.tum.romance.whatsthis.ui.ClassificationFrame.visualError
import edu.tum.romance.whatsthis.ui.components.HintTextField
import edu.tum.romance.whatsthis.ui.views.main.MainView
import edu.tum.romance.whatsthis.util.urlRegex
import java.net.URL

object WebSourceInput: HintTextField("URL"), Runnable {
    init {
        addActionListener { run() }
        MainView.dataUpdate.observe(4) { text = "" }
    }

    override fun run() {
        if(urlRegex.matches(text)) {
            val data = TextData(URL(text), "")
            MainView.content.value = data
            if(MainView.contentName.value.isBlank()) {
                MainView.contentName.value = data.titleSuggestion
            }
        } else {
            visualError("Your input doesn't seem to be a valid URL.")
        }
    }
}