package edu.tum.romance.whatsthis.kui.components

import edu.tum.romance.whatsthis.kui.util.FontCache.SMALL
import edu.tum.romance.whatsthis.kui.util.FontCache.comfortaa
import java.awt.event.ActionEvent
import javax.swing.JButton
import javax.swing.JToolTip

/**
 * A button for use with symbol characters.
 * @param text The text to display on the button.
 * @param tooltip The tooltip to display when the mouse hovers over the button.
 * @param action The action to perform when the button is clicked.
 */
open class SymbolicButton(text: String, tooltip: String? = null, action: (e: ActionEvent) -> Unit): JButton(text) {
    init {
        if(tooltip != null) {
            this.toolTipText = tooltip
        }
        this.addActionListener(action)
    }

    override fun createToolTip(): JToolTip {
        val tip = super.createToolTip()
        tip.font = comfortaa(SMALL)
        return tip
    }
}