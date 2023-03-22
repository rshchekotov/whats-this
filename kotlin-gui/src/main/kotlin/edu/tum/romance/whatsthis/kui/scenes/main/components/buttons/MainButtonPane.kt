package edu.tum.romance.whatsthis.kui.scenes.main.components.buttons

import edu.tum.romance.whatsthis.v1.data.TextData
import edu.tum.romance.whatsthis.kui.components.StyledButton
import edu.tum.romance.whatsthis.kui.event.events.data.FixedSampleCreateEvent
import edu.tum.romance.whatsthis.kui.event.events.data.VariableSampleCreateEvent
import edu.tum.romance.whatsthis.kui.event.events.space.SpaceCreateEvent
import edu.tum.romance.whatsthis.kui.scenes.main.dialogs.CreateFixedDialog
import edu.tum.romance.whatsthis.kui.scenes.main.dialogs.CreateSpaceDialog
import edu.tum.romance.whatsthis.kui.scenes.main.dialogs.CreateVariableDialog
import edu.tum.romance.whatsthis.kui.util.FontCache.MEDIUM
import edu.tum.romance.whatsthis.kui.util.gridBagVSpace
import edu.tum.romance.whatsthis.v1.nlp.API
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
            if(name is String) {
                API.alterSpace(name.toString())
                SpaceCreateEvent(name.toString()).dispatch()
            }
        }, constraints)
        gridBagVSpace(20, constraints)
        add(StyledButton(MEDIUM, "Create Classified Sample", "Create Sample") {
            val data = CreateFixedDialog.open()
            if(data is Pair<String, TextData<*>>) {
                API.addSample(data.second, data.first)
                FixedSampleCreateEvent(data.second, data.first).dispatch()
            }
        }, constraints)
        gridBagVSpace(20, constraints)
        add(StyledButton(MEDIUM, "Create Variable Sample", "Create Sample") {
            val data = CreateVariableDialog.open()
            if(data is TextData<*>) {
                API.addSample(data)
                VariableSampleCreateEvent(data).dispatch()
            }
        }, constraints)
    }
}