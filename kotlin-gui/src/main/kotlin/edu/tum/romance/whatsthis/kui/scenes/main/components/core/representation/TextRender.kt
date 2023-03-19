package edu.tum.romance.whatsthis.kui.scenes.main.components.core.representation

import edu.tum.romance.whatsthis.kui.event.EventHandler
import edu.tum.romance.whatsthis.kui.event.events.data.SampleDeselectEvent
import edu.tum.romance.whatsthis.kui.event.events.data.SampleSelectEvent
import edu.tum.romance.whatsthis.kui.scenes.main.MainModel
import edu.tum.romance.whatsthis.kui.scenes.main.components.core.MainPane
import edu.tum.romance.whatsthis.kui.util.FontCache.SMALL
import edu.tum.romance.whatsthis.kui.util.FontCache.comfortaa
import java.awt.Component
import javax.swing.JTextArea

object TextRender: JTextArea(), MainPaneRender {
    init {
        this.isEditable = false
        this.lineWrap = true
        this.wrapStyleWord = true
        this.font = comfortaa(SMALL)
    }

    @EventHandler(SampleSelectEvent::class)
    fun onSampleSelect() {
        val data = MainModel.data!!

        this.text = "Loading..."

        Thread {
            super.setText(data.text)
            MainPane.revalidate()
            MainPane.repaint()
        }.start()
    }

    @EventHandler(SampleDeselectEvent::class)
    fun onSampleDeselect() {
        this.text = ""
        MainPane.revalidate()
        MainPane.repaint()
    }

    override fun getComponent(): Component = this
}