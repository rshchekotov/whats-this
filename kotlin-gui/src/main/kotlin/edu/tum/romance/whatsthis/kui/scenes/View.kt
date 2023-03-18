package edu.tum.romance.whatsthis.kui.scenes

import edu.tum.romance.whatsthis.kui.Main
import edu.tum.romance.whatsthis.kui.components.Loadable
import javax.swing.JPanel

open class View: JPanel(), Loadable {
    override fun onLoad() {}
    override fun onUnload() {}
    fun switch() {
        Main.currentView.onUnload()
        Main.remove(Main.currentView)
        Main.currentView = this
        Main.add(this)
        Main.pack()
        this.onLoad()
        Main.setLocationRelativeTo(null)
        Main.revalidate()
        Main.repaint()
    }
}