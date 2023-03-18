package edu.tum.romance.whatsthis.kui.scenes.main

import edu.tum.romance.whatsthis.data.TextData
import edu.tum.romance.whatsthis.kui.scenes.main.components.core.representation.*

@Suppress("unused")
object MainModel {
    var sample = ""
    var space = ""
    var data: TextData<*>? = null
    var render: MainPaneRender = TextRender

    val renders: Array<Pair<String, MainPaneRender>> = arrayOf(
        "Text" to TextRender,
        "Web" to WebRender,
        "Vector" to WordVectorRender,
        "Dictionary" to DictionaryRender,
    )
}