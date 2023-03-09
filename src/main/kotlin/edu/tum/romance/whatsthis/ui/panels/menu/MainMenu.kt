package edu.tum.romance.whatsthis.ui.panels.menu

import java.awt.Dimension
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import javax.swing.Box
import javax.swing.JPanel
import javax.swing.border.EmptyBorder

object MainMenu: JPanel() {
    private const val margin = 30
    init {
        layout = GridBagLayout()

        val constraints = GridBagConstraints()
        constraints.weightx = 1.0
        constraints.fill = GridBagConstraints.HORIZONTAL
        constraints.gridwidth = GridBagConstraints.REMAINDER

        add(MainLabel, constraints)
        add(Box.createRigidArea(Dimension(0, 20)), constraints)
        add(CreateButton, constraints)
        add(Box.createRigidArea(Dimension(0, 10)), constraints)
        add(LoadButton, constraints)

        border = EmptyBorder(margin, margin, margin, margin)
    }
}