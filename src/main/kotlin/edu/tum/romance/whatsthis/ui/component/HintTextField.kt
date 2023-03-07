package edu.tum.romance.whatsthis.ui.component

import java.awt.Graphics
import java.awt.event.FocusEvent
import java.awt.event.FocusListener
import javax.swing.JTextField

open class HintTextField(private val hint: String): JTextField(hint), FocusListener {
    private var showingHint = true

    init {
        @Suppress("LeakingThis")
        addFocusListener(this)
    }

    override fun focusGained(e: FocusEvent?) {
        if(this.text.isEmpty()) {
            super.setText("")
            showingHint = false
        }
    }

    override fun focusLost(e: FocusEvent?) {
        if(this.text.isEmpty()) {
            super.setText(hint)
            showingHint = true
        }
    }

    override fun getText(): String {
        return if(showingHint) "" else super.getText()
    }

    override fun setText(t: String) {
        this.showingHint = t.isEmpty() && !this.hasFocus()
        super.setText(t)
    }

    override fun paint(g: Graphics) {
        if(showingHint) {
            g.color = this.disabledTextColor
        }
        super.paint(g)
    }
}