package edu.tum.romance.whatsthis.kui.scenes.main.menubar

import javax.swing.JMenuBar

object TopBar: JMenuBar() {
    init {
        add(ViewMenu)
        add(AppearanceMenu)
    }
}