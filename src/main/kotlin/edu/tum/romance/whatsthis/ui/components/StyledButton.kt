package edu.tum.romance.whatsthis.ui.components

import edu.tum.romance.whatsthis.ui.ClassificationFrame
import java.awt.event.ActionEvent
import javax.swing.JButton

class StyledButton(size: Int, text: String, action: (e: ActionEvent) -> Unit): JButton(text) {
    init {
        this.font = ClassificationFrame.fonts[size]
        this.addActionListener(action)
    }
}