package edu.tum.romance.whatsthis.ui.views

import edu.tum.romance.whatsthis.ui.components.Loadable
import javax.swing.JPanel

abstract class View: JPanel(), Loadable {
    abstract override fun onLoad()
    abstract override fun onUnload()
}