package edu.tum.romance.whatsthis.ui.panels.main.components

import edu.tum.romance.whatsthis.nlp.API
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
    private var items = API.vectors().sorted().toMutableList()

    // TODO: Rewrite this to use a shared state where the model
    //       changes are reflected on change of the state
    private val listVariants = arrayOf(VectorTable(VectorModel(
        null,
        { idx, value -> items[idx] = value; items.sort() },
        { idx -> items[idx] },
        { old, new -> API.vectors[old] = new },
        { name -> name in API.vectors() },
        { items.size }
    )), VectorTable(VectorModel(
        null,
        { idx, value -> items[idx] = value; items.sort() },
        { idx -> items[idx] },
        { old, new -> API.vectors[old] = new },
        { name -> name in API.vectors() },
        { items.size }
    ))).map {
        it.apply {

            selectionModel.addListSelectionListener { e ->
                if(!e.valueIsAdjusting) {
                    val selection = selection()
                    if(!selection.isNullOrEmpty()) {
                        val sample = API.vectors[selection]
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
            items = API.vectors().sorted().toMutableList()
            val classFilter = ClassList.list.selection()
            if(classFilter != null && (classFilter in API.spaces())) {
                items = API.spaceVectors(classFilter).sorted().toMutableList()
            }
            list.update()
        } else {
            items = API.vectors().sorted().toMutableList()
            list.update()
        }
    }
}