package edu.tum.romance.whatsthis.ui.views.main.components.sample

import edu.tum.romance.whatsthis.ui.views.main.MainView
import edu.tum.romance.whatsthis.ui.views.main.components.sample.list.ClassifiedListModel
import edu.tum.romance.whatsthis.ui.views.main.components.sample.list.SampleListPane
import edu.tum.romance.whatsthis.ui.views.main.components.sample.list.UnclassifiedListModel
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.JScrollPane
import javax.swing.JTabbedPane

object SampleList: JTabbedPane() {
    private val table
        get() = when (selectedIndex) {
            0 -> classifiedList
            else -> unclassifiedList
        }

    private val classifiedList = SampleListPane(ClassifiedListModel)
    private val classifiedPane = JScrollPane(classifiedList)
    private val unclassifiedList = SampleListPane(UnclassifiedListModel)
    private val unclassifiedPane = JScrollPane(unclassifiedList)

    init {
        addTab("Classified Samples", classifiedPane)
        addTab("Unclassified Samples", unclassifiedPane)

        val panes = arrayOf(classifiedPane, unclassifiedPane)
        @Suppress("DuplicatedCode")
        for(pane in panes) {
            pane.addMouseListener(object: MouseAdapter() {
                override fun mouseReleased(e: MouseEvent) {
                    if(e.button == MouseEvent.BUTTON1) {
                        val bounds = pane.bounds
                        val absolutePoint = e.point
                        absolutePoint.translate(bounds.x, bounds.y)
                        if(absolutePoint in bounds) {
                            if(table.rowAtPoint(e.point) == -1) {
                                table.selectionModel.clearSelection()
                                MainView.selectedData.value = -1
                            }
                        }
                    }
                }
            })
        }
    }
}