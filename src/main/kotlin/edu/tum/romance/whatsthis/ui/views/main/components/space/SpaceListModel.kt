package edu.tum.romance.whatsthis.ui.views.main.components.space

import edu.tum.romance.whatsthis.nlp.API
import edu.tum.romance.whatsthis.ui.ClassificationFrame.visualError
import javax.swing.table.AbstractTableModel

object SpaceListModel: AbstractTableModel() {
    private val spaces
        get() = API.spaces()

    override fun getRowCount(): Int = spaces.size
    override fun getColumnCount(): Int = 1
    override fun getValueAt(rowIndex: Int, columnIndex: Int): Any = spaces[rowIndex]
    override fun isCellEditable(rowIndex: Int, columnIndex: Int) = true
    override fun getColumnName(column: Int) = "Spaces"

    override fun setValueAt(value: Any?, rowIndex: Int, columnIndex: Int) {
        if(value !is String) return
        if(rowIndex !in spaces.indices || columnIndex != 0) {
            visualError("Fatal Error: Space index out of bounds")
            return
        }
        if(value.isBlank()) {
            visualError("Space name cannot be empty")
            return
        }
        API.alterSpace(spaces[rowIndex], value.toString())
    }
}