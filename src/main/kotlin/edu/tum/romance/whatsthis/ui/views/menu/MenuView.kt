package edu.tum.romance.whatsthis.ui.views.menu

import edu.tum.romance.whatsthis.ui.ClassificationFrame.unimplemented
import edu.tum.romance.whatsthis.ui.components.StyledButton
import edu.tum.romance.whatsthis.ui.components.StyledLabel
import edu.tum.romance.whatsthis.ui.views.View
import java.awt.Dimension
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import javax.swing.Box
import javax.swing.border.EmptyBorder

object MenuView: View() {
    init {
        this.layout = GridBagLayout()

        val constraints = GridBagConstraints()
        constraints.weightx = 1.0
        constraints.fill = GridBagConstraints.HORIZONTAL
        constraints.gridwidth = GridBagConstraints.REMAINDER

        add(StyledLabel(2, "What's This?"), constraints)
        add(Box.createRigidArea(Dimension(0, 20)), constraints)
        add(StyledButton(1, "Create") { unimplemented() }, constraints)
        add(Box.createRigidArea(Dimension(0, 10)), constraints)
        add(StyledButton(1, "Load") { unimplemented() }, constraints)

        border = EmptyBorder(10, 10, 10, 10)
    }

    override fun onOpen() {}

    override fun onClose() {}
}