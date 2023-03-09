package edu.tum.romance.whatsthis.ui.panels.main.components

import edu.tum.romance.whatsthis.ui.ClassificationFrame
import javax.swing.JButton
import javax.swing.JToolTip

object AddClassButton: JButton("⏎") {
    init {
        toolTipText = "Add or edit a class"
        addActionListener { ClassInput.submit() }
    }

    override fun createToolTip(): JToolTip {
        val toolTip = super.createToolTip()
        toolTip.font = ClassificationFrame.fonts[0]
        return toolTip
    }
}