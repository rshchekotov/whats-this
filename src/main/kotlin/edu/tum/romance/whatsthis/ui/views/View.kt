package edu.tum.romance.whatsthis.ui.views

import javax.swing.JPanel

abstract class View: JPanel() {
    abstract fun onOpen()
    abstract fun onClose()
}