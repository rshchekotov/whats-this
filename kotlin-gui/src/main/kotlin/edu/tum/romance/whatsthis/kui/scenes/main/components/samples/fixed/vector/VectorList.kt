package edu.tum.romance.whatsthis.kui.scenes.main.components.samples.fixed.vector

import edu.tum.romance.whatsthis.kui.event.events.data.SampleDeselectEvent
import edu.tum.romance.whatsthis.kui.event.events.data.SampleSelectEvent
import edu.tum.romance.whatsthis.kui.scenes.main.MainModel
import edu.tum.romance.whatsthis.kui.scenes.main.components.samples.variable.table.VariableSampleTable
import edu.tum.romance.whatsthis.kui.util.FontCache.SMALL
import edu.tum.romance.whatsthis.kui.util.FontCache.comfortaa
import edu.tum.romance.whatsthis.v1.nlp.API
import javax.swing.JScrollPane
import javax.swing.JTable

object VectorList: JScrollPane(VERTICAL_SCROLLBAR_AS_NEEDED, HORIZONTAL_SCROLLBAR_NEVER) {
    private var list: JTable = JTable(VectorModel)

    init {
        this.viewport.view = list
        list.font = comfortaa(SMALL)
        list.tableHeader.font = comfortaa(SMALL)
        list.fillsViewportHeight = true

        list.selectionModel.addListSelectionListener {
            if(!it.valueIsAdjusting) {
                if (list.selectedRow != -1) {
                    val name = VectorModel.samples[list.selectedRow]
                    MainModel.data = API.getSample(name)
                    SampleSelectEvent(name).dispatch()
                    VariableSampleTable.selectionModel.clearSelection()
                }
            }
        }

        list.addMouseListener(object: java.awt.event.MouseAdapter() {
            var selection = -1

            override fun mouseClicked(e: java.awt.event.MouseEvent) {
                if(e.clickCount == 2) return

                val current = list.rowAtPoint(e.point)
                if(current == selection) {
                    list.selectionModel.clearSelection()
                    MainModel.data = null
                    selection = -1
                    SampleDeselectEvent().dispatch()
                } else {
                    selection = current
                }
            }
        })
    }

    fun clearSelection() {
        list.selectionModel.clearSelection()
    }
}