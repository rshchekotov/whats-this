package edu.tum.romance.whatsthis.kui.scenes.main

import edu.tum.romance.whatsthis.v1.data.TextData
import edu.tum.romance.whatsthis.kui.event.EventHandler
import edu.tum.romance.whatsthis.kui.event.events.progress.ModelLoadEvent
import edu.tum.romance.whatsthis.kui.scenes.main.components.core.representation.*

@Suppress("unused")
object MainModel {
    var sample = ""
    var space = ""
    var data: TextData<*>? = null
    var variable = false
    var render: MainPaneRender = TextRender

    @EventHandler(ModelLoadEvent::class)
    fun reset() {
        sample = ""
        space = ""
        data = null
        variable = false
        render = TextRender
    }
}