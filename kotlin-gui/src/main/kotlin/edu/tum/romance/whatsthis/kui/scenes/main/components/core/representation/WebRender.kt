package edu.tum.romance.whatsthis.kui.scenes.main.components.core.representation

import edu.tum.romance.whatsthis.kui.event.EventHandler
import edu.tum.romance.whatsthis.kui.event.events.data.SampleDeselectEvent
import edu.tum.romance.whatsthis.kui.event.events.data.SampleSelectEvent
import edu.tum.romance.whatsthis.kui.event.events.progress.ModelLoadEvent
import edu.tum.romance.whatsthis.kui.scenes.main.MainModel
import edu.tum.romance.whatsthis.kui.scenes.main.components.core.MainPane
import edu.tum.romance.whatsthis.kui.util.DialogUtils.visualInfo
import java.awt.Component
import java.net.URL
import javax.swing.JEditorPane
import javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED
import javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER

object WebRender: JEditorPane(), MainPaneRender {
    init {
        this.isEditable = false
        this.contentType = "text/html"
    }

    @EventHandler(SampleSelectEvent::class)
    fun onSampleSelect() {
        val data = MainModel.data!!
        if(data.source !is URL) {
            TextRender.switch()
            visualInfo("The selected sample does not have a web representation.")
            return
        }

        this.text = "<html><body><h1>Loading...</h1></body></html>"

        Thread {
            super.setPage(data.source as URL)
            MainPane.revalidate()
            MainPane.repaint()
        }.start()
    }

    @EventHandler(SampleDeselectEvent::class, ModelLoadEvent::class)
    fun onSampleDeselect() {
        this.text = ""
        MainPane.revalidate()
        MainPane.repaint()
    }

    override fun onLoad() {
        MainPane.horizontalScrollBarPolicy = HORIZONTAL_SCROLLBAR_AS_NEEDED
    }

    override fun onUnload() {
        MainPane.horizontalScrollBarPolicy = HORIZONTAL_SCROLLBAR_NEVER
    }

    override fun getComponent(): Component = this
}