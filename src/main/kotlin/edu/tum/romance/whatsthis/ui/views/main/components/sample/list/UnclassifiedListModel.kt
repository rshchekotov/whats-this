package edu.tum.romance.whatsthis.ui.views.main.components.sample.list

import edu.tum.romance.whatsthis.nlp.API
import edu.tum.romance.whatsthis.ui.ClassificationFrame
import edu.tum.romance.whatsthis.ui.ClassificationFrame.visualError
import edu.tum.romance.whatsthis.ui.components.SymbolicButton
import edu.tum.romance.whatsthis.ui.views.main.MainView
import java.awt.Component
import javax.swing.DefaultCellEditor
import javax.swing.JCheckBox
import javax.swing.JTable
import javax.swing.table.AbstractTableModel
import javax.swing.table.DefaultTableCellRenderer

object UnclassifiedListModel: AbstractTableModel() {
    private val buttonCache = mutableMapOf<String, SymbolicButton>()
    private val samples
        get() = API.spaceVectors()

    init {
        MainView.dataUpdate.observe(2) {
            fireTableDataChanged()
        }
    }

    override fun getRowCount() = samples.size
    override fun getColumnCount() = 2
    override fun getColumnName(column: Int): String {
        return when(column) {
            0 -> "Sample"
            else -> "Information"
        }
    }
    override fun getValueAt(rowIndex: Int, columnIndex: Int): String {
        return samples[rowIndex]
    }
    override fun isCellEditable(rowIndex: Int, columnIndex: Int): Boolean {
        return true
    }

    override fun setValueAt(aValue: Any?, rowIndex: Int, columnIndex: Int) {
        if(aValue !is String) return
        if(columnIndex != 0) return
        if(rowIndex !in samples.indices) {
            visualError("Fatal Error: Sample index out of bounds")
            return
        }
        if(aValue.isBlank()) {
            visualError("Sample name cannot be empty")
            return
        }
        API.renameSample(samples[rowIndex], aValue.toString())
    }

    val renderer = object : DefaultTableCellRenderer() {
        override fun getTableCellRendererComponent(
            table: JTable,
            value: Any,
            isSelected: Boolean,
            hasFocus: Boolean,
            row: Int,
            column: Int
        ): Component {
            val component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column)
            if (column == 1 && value is String) {
                return buttonCache[value] ?: SymbolicButton("\uD83D\uDEC8", "Information on $value") {
                    val distances = API.distances(value).mapIndexed { index, distance ->
                        val space = "<td>${API.spaces()[index]}</td>"
                        val dist = "<td>${String.format("%.2f", distance * 100)}</td>"
                        "<tr>$space$dist</tr>"
                    }.joinToString("")
                    ClassificationFrame.visualInfo("""
                        <html>
                            <h1>Document: $value</h1>
                            <table>
                                <tr>
                                    <th>Space</th>
                                    <th>Distance</th>
                                </tr>
                                $distances
                            </table>
                        </html>
                    """.trimIndent())
                }.also {
                    buttonCache[value] = it
                    isOpaque = true
                }
            }
            return component
        }
    }

    val editor = object: DefaultCellEditor(JCheckBox()) {
        var isPushed: String? = null

        override fun getTableCellEditorComponent(
            table: JTable?,
            value: Any?,
            isSelected: Boolean,
            row: Int,
            column: Int
        ): Component {
            val button: SymbolicButton = buttonCache[value]!!
            if(button.actionListeners.size != 2) {
                button.addActionListener {
                    isPushed = null
                    fireEditingStopped()
                }
            }
            isPushed = value as String
            return button
        }

        override fun getCellEditorValue(): Any {
            return if(isPushed != null) {
                val old = isPushed!!
                isPushed = null
                buttonCache[old]?.doClick()
                old
            } else ""
        }

        override fun stopCellEditing(): Boolean {
            return true
        }
    }
}