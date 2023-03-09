package edu.tum.romance.whatsthis.ui.panels.main.components

import edu.tum.romance.whatsthis.nlp.Monitor
import edu.tum.romance.whatsthis.ui.component.VectorModel
import edu.tum.romance.whatsthis.ui.component.VectorTable
import java.awt.datatransfer.StringSelection
import java.awt.datatransfer.Transferable
import javax.swing.JComponent
import javax.swing.JScrollPane
import javax.swing.TransferHandler

object SampleList: JScrollPane() {
    var items = Monitor.cloudKeys().sorted().toMutableList()
    val list: VectorTable = VectorTable(VectorModel(
        "Samples",
        { idx, value -> items[idx] = value; items.sort() },
        { idx -> items[idx] },
        { old, new -> Monitor.renameInCache(old, new) },
        { name -> name in Monitor.cacheKeys() },
        { items.size }
    ))

    init {
        viewport.view = list
        list.selectionModel.addListSelectionListener {
            if(!it.valueIsAdjusting) {
                val selection = list.selection()
                if(selection != null) {
                    val sample = Monitor.loadFromCache(selection)
                    if(sample != null) {
                        Editor.content = sample.text
                    }
                }
            }
        }

        list.transferHandler = object: TransferHandler() {
            override fun getSourceActions(c: JComponent?): Int {
                return MOVE
            }

            override fun createTransferable(c: JComponent): Transferable {
                return StringSelection(list.selection().toString())
            }
        }

        list.dragEnabled = true
    }

    fun update() {
        items = Monitor.cacheKeys().sorted().toMutableList()
        val classFilter = ClassList.list.selection()
        if(classFilter != null && (classFilter in Monitor)) {
            items = items.filter {
                Monitor[classFilter]!!.contains(it)
            }.toMutableList()
        }
        list.update()
    }
}