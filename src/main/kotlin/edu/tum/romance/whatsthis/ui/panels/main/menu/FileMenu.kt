package edu.tum.romance.whatsthis.ui.panels.main.menu

import edu.tum.romance.whatsthis.ui.ClassificationFrame
import java.awt.event.KeyEvent
import javax.swing.JMenu
import javax.swing.JMenuItem

object FileMenu: JMenu("File") {
    init {
        mnemonic = KeyEvent.VK_F
        font = ClassificationFrame.fonts[0]

        val saveItem = JMenuItem("Save")
        saveItem.font = ClassificationFrame.fonts[0]
        saveItem.mnemonic = KeyEvent.VK_S
        saveItem.addActionListener { ClassificationFrame.unimplemented() }
        add(saveItem)

        val loadItem = JMenuItem("Load")
        loadItem.font = ClassificationFrame.fonts[0]
        loadItem.mnemonic = KeyEvent.VK_L
        loadItem.addActionListener { ClassificationFrame.unimplemented() }
        add(loadItem)
    }
}