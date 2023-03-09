package edu.tum.romance.whatsthis.ui.panels.main.components

import edu.tum.romance.whatsthis.ui.ClassificationFrame
import javax.swing.JButton
import javax.swing.JToolTip

object SourceButton: JButton("⏎") {
    init {
        toolTipText = "Load data from source"
        addActionListener { (SourceSelector.card as Runnable).run() }
    }

    override fun createToolTip(): JToolTip {
        val toolTip = super.createToolTip()
        toolTip.font = ClassificationFrame.fonts[0]
        return toolTip
    }
}