package edu.tum.romance.whatsthis.kui.components

import edu.tum.romance.whatsthis.kui.util.FontCache.SMALL
import edu.tum.romance.whatsthis.kui.util.FontCache.comfortaa
import java.awt.Graphics
import java.awt.event.FocusEvent
import java.awt.event.FocusListener
import javax.swing.JTextField

/**
 * A text field that shows a hint when it is empty.
 * @param hint The hint to show.
 * @see JTextField
 */
@Suppress("unused")
class HintTextField(private val hint: String): JTextField(hint), FocusListener {
    private var showingHint = true

    init {
        addFocusListener(this)
        font = comfortaa(SMALL)
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