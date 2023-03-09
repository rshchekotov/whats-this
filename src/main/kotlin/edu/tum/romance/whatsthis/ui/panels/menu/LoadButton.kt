package edu.tum.romance.whatsthis.ui.panels.menu

import edu.tum.romance.whatsthis.ui.ClassificationFrame
import javax.swing.JButton

object LoadButton: JButton("Load") {
    init {
        font = ClassificationFrame.fonts[1]
        addActionListener {
            ClassificationFrame.unimplemented()
        }
    }
}