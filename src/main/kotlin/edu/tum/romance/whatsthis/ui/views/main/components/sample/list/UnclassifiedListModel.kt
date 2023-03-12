package edu.tum.romance.whatsthis.ui.views.main.components.sample.list

import edu.tum.romance.whatsthis.nlp.API
import edu.tum.romance.whatsthis.ui.ClassificationFrame.visualError
import edu.tum.romance.whatsthis.ui.views.main.MainView
import javax.swing.table.AbstractTableModel

object UnclassifiedListModel: AbstractTableModel() {
    private val samples
        get() = API.spaceVectors()

    init {
        MainView.dataUpdate.observe(2) {
            fireTableDataChanged()
        }
    }

    override fun getRowCount() = samples.size
    override fun getColumnCount() = 1
    override fun getValueAt(rowIndex: Int, columnIndex: Int) = samples[rowIndex]
    override fun isCellEditable(rowIndex: Int, columnIndex: Int) = true

    override fun setValueAt(aValue: Any?, rowIndex: Int, columnIndex: Int) {
        if(aValue !is String) return
        if(rowIndex !in samples.indices || columnIndex != 0) {
            visualError("Fatal Error: Sample index out of bounds")
            return
        }
        if(aValue.isBlank()) {
            visualError("Sample name cannot be empty")
            return
        }
        API.renameSample(samples[rowIndex], aValue.toString())
    }
}