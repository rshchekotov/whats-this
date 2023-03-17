package edu.tum.romance.whatsthis.kui.scenes.menu

import edu.tum.romance.whatsthis.kui.components.StyledButton
import edu.tum.romance.whatsthis.kui.components.StyledLabel
import edu.tum.romance.whatsthis.kui.scenes.View
import edu.tum.romance.whatsthis.kui.scenes.main.MainView
import edu.tum.romance.whatsthis.kui.util.DialogUtils.unimplemented
import edu.tum.romance.whatsthis.kui.util.FontCache.HUGE
import edu.tum.romance.whatsthis.kui.util.FontCache.MEDIUM
import java.awt.Dimension
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import javax.swing.Box
import javax.swing.border.EmptyBorder

@Suppress("unused")
object MenuView: View() {
    init {
        this.layout = GridBagLayout()
        val constraints = GridBagConstraints().apply {
            weightx = 1.0
            fill = GridBagConstraints.HORIZONTAL
            gridwidth = GridBagConstraints.REMAINDER
        }

        add(StyledLabel(HUGE, "What's This?"), constraints)
        add(Box.createRigidArea(Dimension(0, 20)), constraints)
        add(StyledButton(MEDIUM, "New Model") { MainView.switch() }, constraints)
        add(Box.createRigidArea(Dimension(0, 20)), constraints)
        add(StyledButton(MEDIUM, "Load Model") { unimplemented() }, constraints)
        border = EmptyBorder(10, 10, 10, 10)
    }
}