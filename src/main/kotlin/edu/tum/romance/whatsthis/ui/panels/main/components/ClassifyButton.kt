package edu.tum.romance.whatsthis.ui.panels.main.components

import edu.tum.romance.whatsthis.ui.ClassificationFrame
import javax.swing.JButton
import javax.swing.JToolTip

object ClassifyButton: JButton("✓") {
    init {
        toolTipText = "Classify the sample text"
        addActionListener { ClassificationFrame.unimplemented() }
    }

    override fun createToolTip(): JToolTip {
        val toolTip = super.createToolTip()
        toolTip.font = ClassificationFrame.fonts[0]
        return toolTip
    }
}