package edu.tum.romance.whatsthis.kui.scenes.main.components.samples.variable.table

import edu.tum.romance.whatsthis.kui.event.events.data.SampleDeselectEvent
import edu.tum.romance.whatsthis.kui.event.events.data.SampleSelectEvent
import edu.tum.romance.whatsthis.kui.scenes.main.MainModel
import edu.tum.romance.whatsthis.kui.scenes.main.components.samples.fixed.vector.VectorList
import edu.tum.romance.whatsthis.kui.util.FontCache.SMALL
import edu.tum.romance.whatsthis.kui.util.FontCache.comfortaa
import edu.tum.romance.whatsthis.v1.nlp.API
import java.awt.Component
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.DefaultCellEditor
import javax.swing.JTable
import javax.swing.JTextField
import javax.swing.table.TableCellEditor
import javax.swing.table.TableCellRenderer

object VariableSampleTable: JTable(VariableSampleModel) {
    init {
        this.tableHeader.setUI(null)
        this.font = comfortaa(SMALL)
        this.selectionModel.addListSelectionListener {
            if(!it.valueIsAdjusting) {
                if (this.selectedRow != -1) {
                    val name = VariableSampleModel.samples[this.selectedRow]
                    MainModel.data = API.getSample(name)
                    SampleSelectEvent(name).dispatch()
                    VectorList.clearSelection()
                }
            }
        }

        this.addMouseListener(object: MouseAdapter() {
            var selection = -1

            override fun mouseClicked(e: MouseEvent) {
                if (e.clickCount == 2) return

                val current = this@VariableSampleTable.rowAtPoint(e.point)
                if (current == selection) {
                    MainModel.data = null
                    selection = -1
                    SampleDeselectEvent().dispatch()
                } else {
                    selection = current
                }
            }
        })
    }

    override fun prepareEditor(editor: TableCellEditor?, row: Int, column: Int): Component {
        val component = super.prepareEditor(editor, row, column)
        if(column == 0) {
            component.font = comfortaa(SMALL)
        }
        return component
    }

    override fun getCellEditor(row: Int, column: Int): TableCellEditor {
        return when (column) {
            1 -> VariableButtonAdapter
            else -> DefaultCellEditor(JTextField()) // TODO: Make it work...
        }
    }

    override fun getCellRenderer(row: Int, column: Int): TableCellRenderer {
        val cellRenderer = super.getCellRenderer(row, column)
        if(column == 1) {
            return VariableSampleRenderer
        }
        return cellRenderer
    }
}