package edu.tum.romance.whatsthis.kui.scenes.main.components.core.representation

import java.awt.Dimension
import javax.swing.JTextArea

object TextRender: MainPaneRender() {
    private val textArea: JTextArea = JTextArea()

    init {
        add(textArea)
        textArea.isEditable = false
    }

    override fun setPreferredSize(preferredSize: Dimension?) {
        super.setPreferredSize(preferredSize)
        textArea.preferredSize = preferredSize
    }
}