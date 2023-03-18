package edu.tum.romance.whatsthis.kui.scenes.main.components.samples.variable.table

import edu.tum.romance.whatsthis.kui.util.FontCache.SMALL
import edu.tum.romance.whatsthis.kui.util.FontCache.comfortaa
import java.awt.Component
import javax.swing.JTable
import javax.swing.table.TableCellEditor
import javax.swing.table.TableCellRenderer

object VariableSampleTable: JTable(VariableSampleModel) {
    init {
        this.tableHeader.setUI(null)
        this.font = comfortaa(SMALL)
    }

    override fun prepareEditor(editor: TableCellEditor?, row: Int, column: Int): Component {
        val component = super.prepareEditor(editor, row, column)
        if(column == 0) {
            component.font = comfortaa(SMALL)
        }
        return component
    }

    override fun getCellEditor(row: Int, column: Int): TableCellEditor {
        val cellEditor = super.getCellEditor()
        if(column == 1) {
            return VariableSampleEditor
        }
        return cellEditor
    }

    override fun getCellRenderer(row: Int, column: Int): TableCellRenderer {
        val cellRenderer = super.getCellRenderer(row, column)
        if(column == 1) {
            return VariableSampleRenderer
        }
        return cellRenderer
    }
}