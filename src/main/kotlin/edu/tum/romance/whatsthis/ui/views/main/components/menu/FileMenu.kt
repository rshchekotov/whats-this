package edu.tum.romance.whatsthis.ui.views.main.components.menu

import edu.tum.romance.whatsthis.ui.ClassificationFrame
import edu.tum.romance.whatsthis.ui.ClassificationFrame.unimplemented
import java.awt.event.KeyEvent
import javax.swing.JMenu
import javax.swing.JMenuItem

object FileMenu: JMenu("File") {
    init {
        font = ClassificationFrame.fonts[0]

        val save = JMenuItem("Save")
        save.font = ClassificationFrame.fonts[0]
        save.mnemonic = KeyEvent.VK_S
        save.addActionListener { unimplemented() }
        add(save)

        val load = JMenuItem("Load")
        load.font = ClassificationFrame.fonts[0]
        load.mnemonic = KeyEvent.VK_L
        load.addActionListener { unimplemented() }
        add(load)
    }
}