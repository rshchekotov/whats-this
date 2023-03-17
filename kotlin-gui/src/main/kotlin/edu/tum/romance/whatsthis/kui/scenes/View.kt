package edu.tum.romance.whatsthis.kui.scenes

import edu.tum.romance.whatsthis.kui.Main
import edu.tum.romance.whatsthis.kui.components.Loadable
import javax.swing.JPanel

open class View: JPanel(), Loadable {
    override fun onLoad() {}
    override fun onUnload() {}
    fun switch() {
        if(Main.currentView is View) {
            Main.currentView!!.onUnload()
            Main.remove(Main.currentView)
        }
        Main.currentView = this
        Main.add(this)
        this.onLoad()
        Main.revalidate()
        Main.repaint()
    }
}