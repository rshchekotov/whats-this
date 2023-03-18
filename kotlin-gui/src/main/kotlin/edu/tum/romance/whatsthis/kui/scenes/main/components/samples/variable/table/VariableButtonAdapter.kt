package edu.tum.romance.whatsthis.kui.scenes.main.components.samples.variable.table

import edu.tum.romance.whatsthis.kui.scenes.main.components.samples.variable.VariablePane
import java.awt.Component
import javax.swing.DefaultCellEditor
import javax.swing.JCheckBox
import javax.swing.JTable

internal object VariableButtonAdapter : DefaultCellEditor(JCheckBox()) {
    private var pushed: String? = null

    override fun getTableCellEditorComponent(
        table: JTable?,
        value: Any?,
        isSelected: Boolean,
        row: Int,
        column: Int
    ): Component {
        pushed = value as String
        val (button, active) = VariablePane.buttonCache[value]!!
        if(!active) {
            button.addActionListener {
                pushed = null
                fireEditingStopped()
            }
            VariablePane.buttonCache[value] = button to true
        }
        return button
    }

    override fun getCellEditorValue(): Any {
        return if(pushed is String) {
            val old = pushed as String
            pushed = null
            VariablePane.buttonCache[old]!!.first.doClick()
            old
        } else ""
    }

    override fun stopCellEditing() = true
}
