package edu.tum.romance.whatsthis.ui.panels.main.components

import edu.tum.romance.whatsthis.nlp.Monitor
import edu.tum.romance.whatsthis.ui.component.VectorModel
import edu.tum.romance.whatsthis.ui.component.VectorTable
import java.awt.datatransfer.StringSelection
import java.awt.datatransfer.Transferable
import java.awt.event.*
import javax.swing.JComponent
import javax.swing.JScrollPane
import javax.swing.JTabbedPane
import javax.swing.TransferHandler

object SampleList: JTabbedPane() {
    private var items = Monitor.cacheKeys().sorted().toMutableList()

    private val listVariants = arrayOf(VectorTable(VectorModel(
        null,
        { idx, value -> items[idx] = value; items.sort() },
        { idx -> items[idx] },
        { old, new -> Monitor.renameInCache(old, new) },
        { name -> name in Monitor.cacheKeys() },
        { items.size }
    )), VectorTable(VectorModel(
        null,
        { idx, value -> items[idx] = value; items.sort() },
        { idx -> items[idx] },
        { old, new -> Monitor.renameInCache(old, new) },
        { name -> name in Monitor.unclassified() },
        { items.size }
    ))).map {
        it.apply {

            selectionModel.addListSelectionListener { e ->
                if(!e.valueIsAdjusting) {
                    val selection = selection()
                    if(!selection.isNullOrEmpty()) {
                        val sample = Monitor.loadFromCache(selection)
                        if(sample != null) {
                            MainViewComponent.content = sample.text
                        }
                    }
                }
            }

            transferHandler = object: TransferHandler() {
                override fun getSourceActions(c: JComponent?): Int {
                    return MOVE
                }

                override fun createTransferable(c: JComponent): Transferable {
                    return StringSelection(selection().toString())
                }
            }

            dragEnabled = true
        }
    }
    private val classifiedSamples = JScrollPane(listVariants[0])
    private val unclassifiedSamples = JScrollPane(listVariants[1])
    val list
        get() = listVariants[this.selectedIndex]

    init {
        addTab("Classified Samples", classifiedSamples)
        setMnemonicAt(0, KeyEvent.VK_1)
        addTab("Unclassified Samples", unclassifiedSamples)
        setMnemonicAt(1, KeyEvent.VK_2)
        addChangeListener { update() }
    }

    fun update() {
        if(this.selectedIndex == 0) {
            items = Monitor.cacheKeys().sorted().toMutableList()
            val classFilter = ClassList.list.selection()
            if(classFilter != null && (classFilter in Monitor)) {
                items = items.filter {
                    Monitor[classFilter]!!.contains(it)
                }.toMutableList()
            }
            list.update()
        } else {
            items = Monitor.unclassified()
            list.update()
        }
    }
}