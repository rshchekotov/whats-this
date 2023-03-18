package edu.tum.romance.whatsthis.kui.components

import edu.tum.romance.whatsthis.kui.util.FontCache.comfortaa
import java.awt.event.ActionEvent

/**
 * A button with a custom font and tooltip font.
 * @param size The size of the font.
 * @param text The text to display on the button.
 * @param tooltip The tooltip to display on the button.
 * @param action The action to perform when the button is clicked.
 */
open class StyledButton(
    size: Int,
    text: String,
    tooltip: String? = null,
    action: (e: ActionEvent) -> Unit
): SymbolicButton(size, text, tooltip, action) {
    init {
        this.font = comfortaa(size)
    }
}