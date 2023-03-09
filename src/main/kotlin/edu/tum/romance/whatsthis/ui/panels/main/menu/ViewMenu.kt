package edu.tum.romance.whatsthis.ui.panels.main.menu

import edu.tum.romance.whatsthis.ui.ClassificationFrame
import edu.tum.romance.whatsthis.ui.panels.main.components.Editor
import java.awt.event.ItemEvent
import java.awt.event.KeyEvent
import javax.swing.JCheckBoxMenuItem
import javax.swing.JMenu

object ViewMenu: JMenu("View") {
    const val TEXT = 0
    const val VEC = 1

    var mode = TEXT

    init {
        mnemonic = KeyEvent.VK_V
        font = ClassificationFrame.fonts[0]

        val textView = JCheckBoxMenuItem("Text View")

        textView.state = true
        textView.font = ClassificationFrame.fonts[0]
        textView.mnemonic = KeyEvent.VK_T
        add(textView)

        val vecView = JCheckBoxMenuItem("Vector View")
        vecView.font = ClassificationFrame.fonts[0]
        vecView.mnemonic = KeyEvent.VK_V
        add(vecView)

        textView.addItemListener {
            if(it.stateChange == ItemEvent.SELECTED) {
                mode = TEXT
                vecView.state = false
                Editor.update()
            } else {
                mode = VEC
            }
        }

        vecView.addItemListener {
            if(it.stateChange == ItemEvent.SELECTED) {
                mode = VEC
                textView.state = false
                Editor.update()
            } else {
                mode = TEXT
            }
        }
    }
}