package edu.tum.romance.whatsthis.kui.scenes.main.components.core.representation

import edu.tum.romance.whatsthis.kui.event.EventHandler
import edu.tum.romance.whatsthis.kui.event.events.data.SampleDeselectEvent
import edu.tum.romance.whatsthis.kui.event.events.data.SampleSelectEvent
import edu.tum.romance.whatsthis.kui.event.events.progress.ModelLoadEvent
import edu.tum.romance.whatsthis.kui.scenes.main.MainModel
import edu.tum.romance.whatsthis.kui.scenes.main.components.core.MainPane
import edu.tum.romance.whatsthis.kui.util.FontCache.SMALL
import edu.tum.romance.whatsthis.kui.util.FontCache.comfortaa
import java.awt.Component
import javax.swing.JTable
import javax.swing.table.DefaultTableModel
import javax.swing.table.TableColumn
import javax.swing.table.TableRowSorter

object WordVectorRender: JTable(), MainPaneRender {
    init {
        fillsViewportHeight = true
        autoResizeMode = AUTO_RESIZE_OFF
        font = comfortaa(SMALL)
        rowHeight = 20
        model = DefaultTableModel(arrayOf<Array<String>>(), arrayOf("Token", "Count"))

        val sorter = TableRowSorter(model)
        sorter.sortsOnUpdates = true
        rowSorter = sorter
    }

    @EventHandler(SampleSelectEvent::class)
    fun onSampleSelect() {
        val data = MainModel.data!!
        (model as DefaultTableModel).setDataVector(
            data.tokens.map { arrayOf(it.first, it.second) }.toTypedArray(),
            arrayOf("Token", "Count")
        )

        val (tokenColumn, countColumn) = adjustColumnSize()

        val sorter = rowSorter as TableRowSorter
        sorter.setComparator(tokenColumn.modelIndex, Comparator.comparing(String::lowercase))
        sorter.setComparator(countColumn.modelIndex, Comparator.comparingInt(Int::toInt))
        sorter.toggleSortOrder(countColumn.modelIndex)
        sorter.toggleSortOrder(countColumn.modelIndex)
    }

    @EventHandler(SampleDeselectEvent::class, ModelLoadEvent::class)
    fun onSampleDeselect() {
        (model as DefaultTableModel).setDataVector(arrayOf<Array<String>>(), arrayOf("Token", "Count"))
        adjustColumnSize()
    }

    private fun adjustColumnSize(): Pair<TableColumn, TableColumn> {
        val width = MainPane.width - MainPane.scroller
        val tokenColumn = columnModel.getColumn(0)
        tokenColumn.preferredWidth = 2 * width / 3
        val countColumn = columnModel.getColumn(1)
        countColumn.preferredWidth = width / 3
        return Pair(tokenColumn, countColumn)
    }

    override fun getComponent(): Component = this
}