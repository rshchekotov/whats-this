package edu.tum.romance.whatsthis.ui.components

import edu.tum.romance.whatsthis.ui.ClassificationFrame
import java.awt.event.ActionEvent
import javax.swing.JButton
import javax.swing.JToolTip

open class SymbolicButton(text: String, tooltip: String? = null, action: (e: ActionEvent) -> Unit): JButton(text) {
    init {
        if(tooltip != null) {
            this.toolTipText = tooltip
        }
        this.addActionListener(action)
    }

    override fun createToolTip(): JToolTip {
        val tip = super.createToolTip()
        tip.font = ClassificationFrame.fonts[0]
        return tip
    }
}