package edu.tum.romance.whatsthis.kui.scenes.main.components.samples.variable.table

import edu.tum.romance.whatsthis.kui.event.EventHandler
import edu.tum.romance.whatsthis.kui.event.events.data.VariableSampleCreateEvent
import edu.tum.romance.whatsthis.nlp.API
import javax.swing.table.AbstractTableModel

internal object VariableSampleModel: AbstractTableModel() {
    internal var samples = API.spaceVectors().sorted().toMutableList()

    override fun isCellEditable(rowIndex: Int, columnIndex: Int) = true
    override fun getValueAt(row: Int, column: Int): Any = samples[row]
    override fun getRowCount(): Int = samples.size
    override fun getColumnCount(): Int = 2
    override fun getColumnName(column: Int): String = when(column) {
        1 -> "Button"
        else -> "Sample"
    }

    @EventHandler
    fun onVarSampleCreate(event: VariableSampleCreateEvent) {
        for (i in 0 until samples.size) {
            if (samples[i] == event.data.name) return
            if (samples[i] > event.data.name) {
                samples.add(i, event.data.name)
                fireTableRowsInserted(i, i)
                return
            }
        }
        samples.add(event.data.name)
        fireTableRowsInserted(samples.size - 1, samples.size - 1)
    }
}