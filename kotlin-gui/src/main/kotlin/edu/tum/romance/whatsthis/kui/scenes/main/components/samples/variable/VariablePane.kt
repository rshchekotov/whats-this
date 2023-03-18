package edu.tum.romance.whatsthis.kui.scenes.main.components.samples.variable

import edu.tum.romance.whatsthis.kui.components.SymbolicButton
import edu.tum.romance.whatsthis.kui.scenes.main.components.samples.variable.table.VariableSampleTable
import java.awt.Dimension
import javax.swing.JScrollPane

object VariablePane: JScrollPane() {
    private val list = VariableSampleTable
    private val scroller = verticalScrollBar.preferredSize.width
    internal val buttonCache = mutableMapOf<String, Pair<SymbolicButton, Boolean>>()

    init {
        viewport.view = list
        list.fillsViewportHeight = true
    }

    override fun setPreferredSize(preferredSize: Dimension) {
        super.setPreferredSize(preferredSize)
        val width = preferredSize.width - scroller
        val samples = VariableSampleTable.getColumn("Sample")
        samples.preferredWidth = (width * 0.9).toInt()
        val buttons = VariableSampleTable.getColumn("Button")
        buttons.preferredWidth = (width * 0.1).toInt()
    }
}