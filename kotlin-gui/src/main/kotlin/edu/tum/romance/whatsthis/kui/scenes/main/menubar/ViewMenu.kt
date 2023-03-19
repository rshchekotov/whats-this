package edu.tum.romance.whatsthis.kui.scenes.main.menubar

import edu.tum.romance.whatsthis.kui.scenes.main.components.core.MainPane
import edu.tum.romance.whatsthis.kui.util.FontCache.SMALL
import edu.tum.romance.whatsthis.kui.util.FontCache.comfortaa
import javax.swing.JMenu
import javax.swing.JMenuItem

object ViewMenu: JMenu("View") {
    init {
        font = comfortaa(SMALL)

        for((ref, render) in MainPane.renders) {
            val (name, mnemonic) = ref
            val menuItem = JMenuItem(name)
            menuItem.font = comfortaa(SMALL)
            menuItem.mnemonic = mnemonic
            menuItem.addActionListener { Thread { render.switch() }.start() }
            add(menuItem)
        }
    }
}