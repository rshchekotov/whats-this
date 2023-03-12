package edu.tum.romance.whatsthis.ui.views.main.components.sample

import edu.tum.romance.whatsthis.ui.ClassificationFrame
import edu.tum.romance.whatsthis.ui.views.main.MainView
import edu.tum.romance.whatsthis.ui.views.main.components.sample.list.ClassifiedListModel
import edu.tum.romance.whatsthis.ui.views.main.components.sample.list.SampleListPane
import edu.tum.romance.whatsthis.ui.views.main.components.sample.list.UnclassifiedListModel
import java.awt.Dimension
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
    private val unclassifiedList = SampleListPane(
        UnclassifiedListModel,
        mapOf(1 to UnclassifiedListModel.renderer),
        mapOf(1 to UnclassifiedListModel.editor)
    )
    private val unclassifiedPane = JScrollPane(unclassifiedList)

    init {
        addTab("Classified Samples", classifiedPane)
        addTab("Unclassified Samples", unclassifiedPane)
        val panes = arrayOf(classifiedPane, unclassifiedPane)
        @Suppress("DuplicatedCode")
        for (pane in panes) {
            pane.font = ClassificationFrame.fonts[0]
            pane.addMouseListener(object : MouseAdapter() {
                override fun mouseReleased(e: MouseEvent) {
                    if (e.button == MouseEvent.BUTTON1) {
                        val bounds = pane.bounds
                        val absolutePoint = e.point
                        absolutePoint.translate(bounds.x, bounds.y)
                        if (absolutePoint in bounds) {
                            if (table.rowAtPoint(e.point) == -1) {
                                table.selectionModel.clearSelection()
                                MainView.selectedData.value = -1
                            }
                        }
                    }
                }
            })
        }
    }

    override fun setPreferredSize(preferredSize: Dimension) {
        super.setPreferredSize(preferredSize)
        val width = preferredSize.width - unclassifiedPane.verticalScrollBar.preferredSize.width
        val words = unclassifiedList.getColumn("Sample")
        words.preferredWidth = (width * 0.9).toInt()
        val info = unclassifiedList.getColumn("Information")
        info.preferredWidth = (width * 0.1).toInt()
    }
}