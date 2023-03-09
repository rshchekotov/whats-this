package edu.tum.romance.whatsthis.ui.panels.main.menu

import javax.swing.JMenuBar

//#region Menu Bar
object MenuBar: JMenuBar() {
    init {
        add(FileMenu)
        add(ViewMenu)
        add(ModelMenu)
    }
}