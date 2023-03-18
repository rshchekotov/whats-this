package edu.tum.romance.whatsthis.kui.scenes.main.components.samples.variable

import edu.tum.romance.whatsthis.kui.components.SymbolicButton
import edu.tum.romance.whatsthis.kui.scenes.main.components.samples.variable.table.VariableSampleTable
import javax.swing.JScrollPane

object VariablePane: JScrollPane() {
    private val list = VariableSampleTable
    internal val buttonCache = mutableMapOf<String, Pair<SymbolicButton, Boolean>>()

    init {
        viewport.view = list
        list.fillsViewportHeight = true
    }
}