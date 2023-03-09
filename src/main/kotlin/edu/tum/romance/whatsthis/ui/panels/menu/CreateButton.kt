package edu.tum.romance.whatsthis.ui.panels.menu

import edu.tum.romance.whatsthis.ui.ClassificationFrame
import edu.tum.romance.whatsthis.ui.panels.main.ClassyPanel
import javax.swing.JButton

object CreateButton: JButton("Create") {
    init {
        font = ClassificationFrame.fonts[1]
        addActionListener {
            ClassificationFrame.swapView(ClassyPanel)
        }
    }
}