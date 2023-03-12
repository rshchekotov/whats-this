package edu.tum.romance.whatsthis.ui.views.main.components.space

import edu.tum.romance.whatsthis.nlp.API
import edu.tum.romance.whatsthis.ui.ClassificationFrame
import edu.tum.romance.whatsthis.ui.views.main.MainView
import edu.tum.romance.whatsthis.util.observer.trigger
import java.awt.Component
import java.awt.datatransfer.DataFlavor
import javax.swing.JTable
import javax.swing.TransferHandler
import javax.swing.table.TableCellEditor

object SpaceList: JTable() {
    init {
        font = ClassificationFrame.fonts[0]
        model = SpaceListModel
        selectionModel.addListSelectionListener {
            if(!it.valueIsAdjusting) {
                MainView.dataUpdate.trigger()

                val row = selectedRow
                MainView.selectedSpace.value = row
            }
        }

        transferHandler = object: TransferHandler() {
            override fun canImport(support: TransferSupport): Boolean {
                return support.isDataFlavorSupported(DataFlavor.stringFlavor)
            }

            override fun importData(support: TransferSupport): Boolean {
                if(!canImport(support)) return false
                val transferable = support.transferable
                val data = transferable.getTransferData(DataFlavor.stringFlavor) as String
                val dropIndex = dropLocation.row
                if(dropIndex in 0 until model.rowCount) {
                    val spaceName = model.getValueAt(dropIndex, 0) as String
                    API.resample(data, spaceName)
                    MainView.dataUpdate.trigger()
                    return true
                }
                return false
            }
        }

        dragEnabled = true
    }

    override fun prepareEditor(editor: TableCellEditor?, row: Int, column: Int): Component {
        val prepared = super.prepareEditor(editor, row, column)
        prepared.font = ClassificationFrame.fonts[0]
        return prepared
    }
}