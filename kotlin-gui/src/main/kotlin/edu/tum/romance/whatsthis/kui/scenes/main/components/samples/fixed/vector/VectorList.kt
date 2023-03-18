package edu.tum.romance.whatsthis.kui.scenes.main.components.samples.fixed.vector

import edu.tum.romance.whatsthis.kui.util.FontCache.SMALL
import edu.tum.romance.whatsthis.kui.util.FontCache.comfortaa
import javax.swing.JScrollPane
import javax.swing.JTable

object VectorList: JScrollPane(VERTICAL_SCROLLBAR_AS_NEEDED, HORIZONTAL_SCROLLBAR_NEVER) {
    private var list: JTable = JTable(VectorModel)

    init {
        this.viewport.view = list
        list.font = comfortaa(SMALL)
        list.tableHeader.font = comfortaa(SMALL)
        list.fillsViewportHeight = true
    }
}