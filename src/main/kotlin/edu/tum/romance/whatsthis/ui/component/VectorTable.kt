package edu.tum.romance.whatsthis.ui.component

import edu.tum.romance.whatsthis.ui.ClassificationFrame
import java.awt.Component
import java.awt.Font
import javax.swing.JTable
import javax.swing.table.TableCellEditor

class VectorTable(model: VectorModel): JTable(model) {
    init {
        if(model.getColumnName(0) == null) {
            tableHeader.setUI(null)
        }
    }

    fun update() {
        (model as VectorModel).update()
    }

    fun selection(): String? {
        return when (selectedRow) {
            -1 -> null
            else -> getValueAt(selectedRow, 0) as String
        }
    }

    override fun getFont(): Font {
        return ClassificationFrame.fonts[0]
    }
    override fun prepareEditor(editor: TableCellEditor?, row: Int, column: Int): Component {
        val prepared = super.prepareEditor(editor, row, column)
        prepared.font = ClassificationFrame.fonts[0]
        return prepared
    }
}