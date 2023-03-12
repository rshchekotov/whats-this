package edu.tum.romance.whatsthis.ui.views.main.components.main

import edu.tum.romance.whatsthis.io.TextData
import edu.tum.romance.whatsthis.nlp.API
import edu.tum.romance.whatsthis.ui.ClassificationFrame
import edu.tum.romance.whatsthis.ui.ClassificationFrame.visualError
import edu.tum.romance.whatsthis.ui.components.Loadable
import edu.tum.romance.whatsthis.ui.views.main.MainView
import javax.swing.JScrollPane
import javax.swing.JTable
import javax.swing.table.DefaultTableModel
import javax.swing.table.TableRowSorter

object DataVectorView: JScrollPane(
    JTable(), VERTICAL_SCROLLBAR_AS_NEEDED, HORIZONTAL_SCROLLBAR_NEVER
), Loadable {
    private val table = this.viewport.view as JTable
    private val scrollerWidth = verticalScrollBar.preferredSize.width

    init {
        table.fillsViewportHeight = true
        table.autoResizeMode = JTable.AUTO_RESIZE_OFF
        table.font = ClassificationFrame.fonts[0]
        table.rowHeight = 20
        table.model = DefaultTableModel(arrayOf<Array<String>>(), arrayOf("Word", "Vector"))

        val sorter = TableRowSorter(table.model)
        sorter.sortsOnUpdates = true
        table.rowSorter = sorter
    }

    private fun updateTableContents() {
        var data: TextData<*>? = MainView.content.value
        if(data != null) {
            return (table.model as DefaultTableModel).setDataVector(
                data.tokens.map { arrayOf(it.first, it.second) }.toTypedArray(),
                arrayOf("Word", "Vector")
            )
        } else if(MainView.selectedData.value != -1) {
            data = API.vectors[MainView.selectedData.value]
            if(data != null) {
                return (table.model as DefaultTableModel).setDataVector(
                    data.tokens.map { arrayOf(it.first, it.second) }.toTypedArray(),
                    arrayOf("Word", "Vector")
                )
            } else {
                visualError("Fatal Error: Selected Data not found.")
            }
        }
        (table.model as DefaultTableModel).setDataVector(
            arrayOf<Array<String>>(),
            arrayOf("Word", "Vector")
        )
    }

    private fun updateTable() {
        updateTableContents()

        val wordColumn = table.columnModel.getColumn(0)
        wordColumn.preferredWidth = 2 * (MainView.dataViewSize.width - scrollerWidth) / 3
        val vectorColumn = table.columnModel.getColumn(1)
        vectorColumn.preferredWidth = (MainView.dataViewSize.width - scrollerWidth) / 3

        val sorter = table.rowSorter as TableRowSorter
        sorter.setComparator(wordColumn.modelIndex, Comparator.comparing(String::lowercase))
        sorter.setComparator(vectorColumn.modelIndex, Comparator.comparingInt(Int::toInt))
        sorter.toggleSortOrder(vectorColumn.modelIndex)
        sorter.toggleSortOrder(vectorColumn.modelIndex)
    }

    override fun onLoad() {
        updateTable()
        MainView.selectedData.observe(1) { (_, _) -> updateTable() }
        MainView.content.observe(1) { (_, _) -> updateTable() }
    }

    override fun onUnload() {
        MainView.selectedData.stopObserving(1)
        MainView.content.stopObserving(1)
    }

}