package edu.tum.romance.whatsthis.ui.panels.main.components

import edu.tum.romance.whatsthis.nlp.Monitor
import edu.tum.romance.whatsthis.ui.component.VectorModel
import edu.tum.romance.whatsthis.ui.component.VectorTable
import java.awt.datatransfer.DataFlavor
import javax.swing.JScrollPane
import javax.swing.TransferHandler

object ClassList: JScrollPane() {
    var items = Monitor.cloudKeys().sorted().toMutableList()
    val list: VectorTable = VectorTable(VectorModel(
        "Classes",
        { idx, value -> items[idx] = value; items.sort() },
        { idx -> items[idx] },
        { old, new -> Monitor.renameCloud(old, new) },
        { name -> name in Monitor },
        { items.size }
    ))

    init {
        viewport.view = list
        list.selectionModel.addListSelectionListener {
            if(!it.valueIsAdjusting) {
                SampleList.update()
            }
        }

        list.transferHandler = object: TransferHandler() {
            override fun canImport(support: TransferSupport): Boolean {
                return support.isDataFlavorSupported(DataFlavor.stringFlavor)
            }

            override fun importData(support: TransferSupport): Boolean {
                if(!canImport(support)) return false
                val transfer = support.transferable
                val data = transfer.getTransferData(DataFlavor.stringFlavor) as String
                val dropIndex = list.dropLocation.row
                if(dropIndex in items.indices) {
                    val className = items[dropIndex]
                    Monitor.assign(data, className)
                    SampleList.update()
                    return true
                }
                return false
            }
        }

        list.dragEnabled = true
    }

    fun update() {
        items = Monitor.cloudKeys().sorted().toMutableList()
        list.update()
    }
}