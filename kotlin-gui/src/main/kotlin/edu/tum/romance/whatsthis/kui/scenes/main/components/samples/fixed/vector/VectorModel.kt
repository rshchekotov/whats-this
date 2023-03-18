package edu.tum.romance.whatsthis.kui.scenes.main.components.samples.fixed.vector

import edu.tum.romance.whatsthis.nlp.API
import javax.swing.table.DefaultTableModel

object VectorModel: DefaultTableModel() {
    private var samples = API.classified().sorted()

    override fun getColumnCount() = 1
    override fun getColumnName(column: Int) = "Sample"
    override fun getValueAt(row: Int, column: Int): Any = samples[row]
}