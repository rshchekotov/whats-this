package edu.tum.romance.whatsthis.ui.views.main.components.space

import edu.tum.romance.whatsthis.ui.views.main.MainView
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.JScrollPane

object SpaceListPane: JScrollPane(
    SpaceList,
    VERTICAL_SCROLLBAR_AS_NEEDED,
    HORIZONTAL_SCROLLBAR_NEVER
) {
    val table = SpaceList

    init {
        addMouseListener(object: MouseAdapter() {
            override fun mouseReleased(e: MouseEvent) {
                if(e.button == MouseEvent.BUTTON1) {
                    val bounds = this@SpaceListPane.bounds
                    val absolutePoint = e.point
                    absolutePoint.translate(bounds.x, bounds.y)
                    if(absolutePoint in bounds) {
                        if(table.rowAtPoint(e.point) == -1) {
                            table.selectionModel.clearSelection()
                            MainView.selectedSpace.value = -1
                        }
                    }
                }
            }
        })
    }
}