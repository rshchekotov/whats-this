package edu.tum.romance.whatsthis.kui.scenes.main.components.buttons

import edu.tum.romance.whatsthis.kui.components.StyledButton
import edu.tum.romance.whatsthis.kui.event.events.space.SpaceCreateEvent
import edu.tum.romance.whatsthis.kui.scenes.main.dialogs.CreateSpaceDialog
import edu.tum.romance.whatsthis.kui.util.DialogUtils.unimplemented
import edu.tum.romance.whatsthis.kui.util.FontCache.MEDIUM
import edu.tum.romance.whatsthis.kui.util.gridBagVSpace
import edu.tum.romance.whatsthis.nlp.API
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import javax.swing.JPanel

/**
 * Horizontal Button Panel on the bottom right of the main view.
 * Contains the buttons to:
 * - Create Sample
 *   - Create Fixed Sample (with space)
 *   - Create Variable Sample (without space)
 * - Create Space
 */
object MainButtonPane: JPanel() {
    init {
        this.layout = GridBagLayout()
        val constraints = GridBagConstraints().apply {
            weightx = 1.0
            fill = GridBagConstraints.HORIZONTAL
            gridwidth = GridBagConstraints.REMAINDER
        }

        add(StyledButton(MEDIUM, "Create Empty Space", "Create Space") {
            val name = CreateSpaceDialog.open()
            API.alterSpace(name.toString())
            SpaceCreateEvent(name.toString()).dispatch()
        }, constraints)
        gridBagVSpace(20, constraints)
        add(StyledButton(MEDIUM, "Create Classified Sample", "Create Sample") {
            unimplemented()
        }, constraints)
        gridBagVSpace(20, constraints)
        add(StyledButton(MEDIUM, "Create Variable Sample", "Create Sample") {
            unimplemented()
        }, constraints)
    }
}