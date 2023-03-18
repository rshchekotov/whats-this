package edu.tum.romance.whatsthis.kui.scenes.main.components.samples.fixed.space

import javax.swing.JScrollPane
import javax.swing.JTable

object SpaceList: JScrollPane(VERTICAL_SCROLLBAR_AS_NEEDED, HORIZONTAL_SCROLLBAR_NEVER) {
    private var list: JTable = JTable(SpaceModel)

    init {
        this.viewport.view = list
        list.fillsViewportHeight = true
    }
}