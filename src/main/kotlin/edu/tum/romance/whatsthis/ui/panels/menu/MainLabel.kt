package edu.tum.romance.whatsthis.ui.panels.menu

import edu.tum.romance.whatsthis.ui.ClassificationFrame
import javax.swing.JLabel

object MainLabel: JLabel("What's This?") {
    init {
        font = ClassificationFrame.fonts[2]
    }
}