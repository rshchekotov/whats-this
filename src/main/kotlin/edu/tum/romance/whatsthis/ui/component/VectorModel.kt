package edu.tum.romance.whatsthis.ui.component

import edu.tum.romance.whatsthis.ui.ClassificationFrame
import javax.swing.table.AbstractTableModel

//#region Reusable Components
class VectorModel(
    private val header: String?,
    val setter: (Int, String) -> Unit,
    val getter: (Int) -> String,
    val rename: (String, String) -> Unit,
    val exists: (String) -> Boolean,
    val size: () -> Int
) : AbstractTableModel() {
    override fun getRowCount(): Int = size()
    override fun getColumnCount(): Int = 1
    override fun getValueAt(rowIndex: Int, columnIndex: Int): Any = getter(rowIndex)
    override fun getColumnName(column: Int): String? = header
    override fun isCellEditable(rowIndex: Int, columnIndex: Int): Boolean = true
    override fun setValueAt(value: Any, rowIndex: Int, columnIndex: Int) {
        if (value is String) {
            if (rowIndex < rowCount) {
                val old = getter(rowIndex)
                if (exists(old)) {
                    rename(old, value)
                    setter(rowIndex, value)
                    fireTableDataChanged()
                }
            } else {
                ClassificationFrame.visualError("Cannot rename data at index $rowIndex")
            }
        }
    }
    fun update() = fireTableDataChanged()
}