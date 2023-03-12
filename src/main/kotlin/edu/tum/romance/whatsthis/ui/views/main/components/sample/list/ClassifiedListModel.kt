package edu.tum.romance.whatsthis.ui.views.main.components.sample.list

import edu.tum.romance.whatsthis.nlp.API
import edu.tum.romance.whatsthis.ui.ClassificationFrame
import edu.tum.romance.whatsthis.ui.components.Loadable
import edu.tum.romance.whatsthis.ui.views.main.MainView
import javax.swing.table.AbstractTableModel

object ClassifiedListModel: AbstractTableModel(), Loadable {
    private var space: String? = null
    private val samples
        get() = API.spaceVectors(space)

    override fun getRowCount() = samples.size
    override fun getColumnCount() = 1
    override fun getValueAt(rowIndex: Int, columnIndex: Int) = samples[rowIndex]
    override fun isCellEditable(rowIndex: Int, columnIndex: Int) = true

    override fun setValueAt(aValue: Any?, rowIndex: Int, columnIndex: Int) {
        if(aValue !is String) return
        if(rowIndex !in samples.indices || columnIndex != 0) {
            ClassificationFrame.visualError("Fatal Error: Sample index out of bounds")
            return
        }
        if(aValue.isBlank()) {
            ClassificationFrame.visualError("Sample name cannot be empty")
            return
        }
        API.renameSample(samples[rowIndex], aValue.toString())
    }

    override fun onLoad() {
        MainView.selectedSpace.observe(1) {
            val new = it.second
            if(new != -1) {
                space = API.spaces()[new]
                fireTableDataChanged()
            }
        }

        MainView.dataUpdate.observe(1) {
            fireTableDataChanged()
        }
    }

    override fun onUnload() {
        MainView.selectedSpace.stopObserving(1)
        MainView.dataUpdate.stopObserving(1)
    }
}