package edu.tum.romance.whatsthis.kui.scenes.main.components.samples.variable.table

import edu.tum.romance.whatsthis.nlp.API
import javax.swing.table.DefaultTableModel

internal object VariableSampleModel: DefaultTableModel() {
    @Suppress("unused")
    private var samples = API.spaceVectors().sorted()

    override fun getValueAt(row: Int, column: Int): Any = samples[row]
    override fun getColumnCount(): Int = 2
    override fun getColumnName(column: Int): String = when(column) {
        1 -> "Sample"
        else -> "Button"
    }

    // TODO: Add 'VarSampleCreationEvent'
}