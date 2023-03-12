package edu.tum.romance.whatsthis.ui.views.main.components.main

import edu.tum.romance.whatsthis.io.TextData
import edu.tum.romance.whatsthis.nlp.API
import edu.tum.romance.whatsthis.ui.ClassificationFrame
import edu.tum.romance.whatsthis.ui.ClassificationFrame.visualError
import edu.tum.romance.whatsthis.ui.components.Loadable
import edu.tum.romance.whatsthis.ui.views.main.MainView
import javax.swing.JScrollPane
import javax.swing.JTextArea

object DataTextView: JScrollPane(JTextArea(), VERTICAL_SCROLLBAR_AS_NEEDED, HORIZONTAL_SCROLLBAR_NEVER), Loadable {
    private val editor = this.viewport.view as JTextArea

    init {
        this.editor.isEditable = false
        this.editor.lineWrap = true
        this.editor.wrapStyleWord = true
        this.editor.font = ClassificationFrame.fonts[0]
    }

    private fun updateByContent(change: Pair<TextData<*>?, TextData<*>?>) {
        val new = change.second
        if(new == null) {
            val selected = MainView.selectedData.value
            fillBySelection(selected)
        } else {
            this.editor.text = new.text
        }
    }

    private fun updateBySelection(change: Pair<Int, Int>) {
        val content = MainView.content.value
        if (content == null) {
            val new = change.second
            fillBySelection(new)
        } else {
            this.editor.text = content.text
        }
    }

    private fun fillBySelection(selected: Int) {
        if (selected != -1) {
            val data = API.vectors[selected]
            if (data != null) {
                this.editor.text = data.text
            } else {
                visualError("Fatal Error: Selected Data not found.")
            }
        } else {
            this.editor.text = ""
        }
    }

    override fun onLoad() {
        MainView.selectedData.observe(1, ::updateBySelection)
        MainView.content.observe(1, ::updateByContent)
    }

    override fun onUnload() {
        MainView.selectedData.stopObserving(1)
        MainView.content.stopObserving(1)
    }
}