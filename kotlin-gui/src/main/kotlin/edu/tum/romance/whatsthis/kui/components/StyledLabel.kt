package edu.tum.romance.whatsthis.kui.components

import edu.tum.romance.whatsthis.kui.util.FontCache.comfortaa
import javax.swing.JLabel

/**
 * A label with a custom font.
 */
class StyledLabel(size: Int, text: String): JLabel(text) {
    init {
        font = comfortaa(size)
    }
}