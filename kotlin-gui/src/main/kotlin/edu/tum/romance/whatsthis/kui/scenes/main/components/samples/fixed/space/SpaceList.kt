package edu.tum.romance.whatsthis.kui.scenes.main.components.samples.fixed.space

import edu.tum.romance.whatsthis.kui.event.events.space.SpaceDeselectEvent
import edu.tum.romance.whatsthis.kui.event.events.space.SpaceSelectEvent
import edu.tum.romance.whatsthis.kui.util.FontCache.SMALL
import edu.tum.romance.whatsthis.kui.util.FontCache.comfortaa
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.JScrollPane
import javax.swing.JTable

object SpaceList: JScrollPane(VERTICAL_SCROLLBAR_AS_NEEDED, HORIZONTAL_SCROLLBAR_NEVER) {
    private var list: JTable = JTable(SpaceModel)

    init {
        this.viewport.view = list
        list.font = comfortaa(SMALL)
        list.tableHeader.font = comfortaa(SMALL)
        list.fillsViewportHeight = true

        list.selectionModel.addListSelectionListener {
            if(!it.valueIsAdjusting) {
                if(list.selectedRow != -1) {
                    SpaceSelectEvent(SpaceModel.spaces[list.selectedRow]).dispatch()
                }
            }
        }

        list.addMouseListener(object: MouseAdapter() {
            var selection = -1

            override fun mouseClicked(e: MouseEvent) {
                if(e.clickCount == 2) return

                val current = list.rowAtPoint(e.point)
                if(current == -1 || current == selection) {
                    list.selectionModel.clearSelection()
                    selection = -1
                    SpaceDeselectEvent().dispatch()
                } else {
                    selection = current
                }
            }
        })
    }
}