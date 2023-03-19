package edu.tum.romance.whatsthis.kui.scenes.main.components.core

import edu.tum.romance.whatsthis.kui.scenes.main.MainModel
import edu.tum.romance.whatsthis.kui.scenes.main.components.core.representation.MainPaneRender
import edu.tum.romance.whatsthis.kui.scenes.main.components.core.representation.dictionary.DictionaryRender
import edu.tum.romance.whatsthis.kui.scenes.main.components.core.representation.TextRender
import edu.tum.romance.whatsthis.kui.scenes.main.components.core.representation.WebRender
import edu.tum.romance.whatsthis.kui.scenes.main.components.core.representation.WordVectorRender
import java.awt.Dimension
import java.awt.event.KeyEvent.*
import javax.swing.JScrollPane

object MainPane: JScrollPane(
    MainModel.render.getComponent(),
    VERTICAL_SCROLLBAR_AS_NEEDED,
    HORIZONTAL_SCROLLBAR_NEVER
) {
    val scroller = this.verticalScrollBar.preferredSize.width
    val renders: Array<Pair<Pair<String, Int>, MainPaneRender>> = arrayOf(
        ("Text" to VK_T) to TextRender,
        ("Web" to VK_W) to WebRender,
        ("Vector" to VK_V) to WordVectorRender,
        ("Dictionary" to VK_D) to DictionaryRender,
    )

    override fun setPreferredSize(preferredSize: Dimension?) {
        super.setPreferredSize(preferredSize)
    }
}