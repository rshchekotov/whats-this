package edu.tum.romance.whatsthis.ui.views.main.components.sample.list

import edu.tum.romance.whatsthis.nlp.API
import edu.tum.romance.whatsthis.ui.ClassificationFrame
import edu.tum.romance.whatsthis.ui.views.main.MainView
import java.awt.Component
import java.awt.datatransfer.StringSelection
import javax.swing.JComponent
import javax.swing.JTable
import javax.swing.TransferHandler
import javax.swing.table.AbstractTableModel
import javax.swing.table.TableCellEditor
import javax.swing.table.TableCellRenderer

class SampleListPane(
    model: AbstractTableModel,
    private val renderer: Map<Int, TableCellRenderer> = mapOf(),
    private val editor: Map<Int, TableCellEditor> = mapOf()
): JTable(model) {
    init {
        tableHeader.setUI(null)
        font = ClassificationFrame.fonts[0]
        selectionModel.addListSelectionListener {
            if(!it.valueIsAdjusting) {
                val selection = selection()
                if(selection is String) {
                    val index = API.vectors.ref(selection)
                    MainView.selectedData.value = index
                }
            }
        }

        transferHandler = object: TransferHandler() {
            override fun getSourceActions(c: JComponent) = MOVE

            override fun createTransferable(c: JComponent?) = StringSelection(selection())
        }

        dragEnabled = true
    }

    private fun selection(): String? {
        if(selectedRow != -1) {
            return model.getValueAt(selectedRow, 0) as String
        }
        return null
    }

    override fun prepareEditor(preset: TableCellEditor?, row: Int, column: Int): Component {
        val prepared = super.prepareEditor(preset, row, column)
        if(column !in editor) {
            prepared.font = ClassificationFrame.fonts[0]
        }
        return prepared
    }

    override fun getCellEditor(row: Int, column: Int): TableCellEditor {
        return if (editor.isNotEmpty() && editor[column] != null) {
            editor[column]!!
        } else super.getCellEditor(row, column)
    }

    override fun getCellRenderer(row: Int, column: Int): TableCellRenderer {
        return if(renderer.isNotEmpty() && renderer[column] != null) {
            renderer[column]!!
        } else super.getCellRenderer(row, column)
    }
}