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

class SampleListPane(model: AbstractTableModel): JTable(model) {
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

    override fun prepareEditor(editor: TableCellEditor?, row: Int, column: Int): Component {
        val prepared = super.prepareEditor(editor, row, column)
        prepared.font = ClassificationFrame.fonts[0]
        return prepared
    }
}