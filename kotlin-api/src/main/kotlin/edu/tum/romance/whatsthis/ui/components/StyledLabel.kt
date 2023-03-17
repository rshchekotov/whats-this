package edu.tum.romance.whatsthis.ui.components

import edu.tum.romance.whatsthis.ui.ClassificationFrame
import javax.swing.JLabel

class StyledLabel(size: Int, text: String): JLabel(text) {
    init {
        font = ClassificationFrame.fonts[size]
    }
}