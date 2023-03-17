package edu.tum.romance.whatsthis.ui.views.main.components.menu

import javax.swing.JMenuBar

object TopBar: JMenuBar() {
    init {
        add(FileMenu)
        add(ViewMenu)
        add(ModelMenu)
    }
}