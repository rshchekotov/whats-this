package edu.tum.romance.whatsthis.ui.panels.main.components

import edu.tum.romance.whatsthis.ui.ClassificationFrame
import javax.swing.JButton
import javax.swing.JToolTip

object AddSampleButton: JButton("⏎") {
    init {
        addActionListener { SampleNameInput.submit() }
        toolTipText = "Add or edit the sample text and add it to the selected class"
    }
    override fun createToolTip(): JToolTip {
        val toolTip = super.createToolTip()
        toolTip.font = ClassificationFrame.fonts[0]
        return toolTip
    }
}