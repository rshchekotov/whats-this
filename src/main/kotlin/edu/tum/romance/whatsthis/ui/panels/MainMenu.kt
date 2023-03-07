package edu.tum.romance.whatsthis.ui.panels

import edu.tum.romance.whatsthis.ui.ClassificationFrame
import java.awt.Dimension
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import javax.swing.Box
import javax.swing.JButton
import javax.swing.JLabel
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

private object MainLabel: JLabel("What's This?") {
    init {
        font = ClassificationFrame.fonts[2]
    }
}

private object CreateButton: JButton("Create") {
    init {
        font = ClassificationFrame.fonts[1]
        addActionListener {
            ClassificationFrame.swapView(ClassyPanel)
        }
    }
}

private object LoadButton: JButton("Load") {
    init {
        font = ClassificationFrame.fonts[1]
        addActionListener {
            ClassificationFrame.unimplemented()
        }
    }
}