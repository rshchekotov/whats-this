package edu.tum.romance.whatsthis.ui.views.main.components.sample.list

import edu.tum.romance.whatsthis.nlp.API
import edu.tum.romance.whatsthis.ui.components.Loadable
import edu.tum.romance.whatsthis.ui.views.main.MainView
import java.awt.datatransfer.StringSelection
import javax.swing.JComponent
import javax.swing.JTable
import javax.swing.TransferHandler
import javax.swing.table.AbstractTableModel

class SampleListPane(model: AbstractTableModel): JTable(model), Loadable {
    init {
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

    override fun onLoad() {
        (model as Loadable).onLoad()
    }

    override fun onUnload() {
        (model as Loadable).onUnload()
    }

}