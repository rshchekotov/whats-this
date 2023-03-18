package edu.tum.romance.whatsthis.kui.util

import java.awt.Dimension
import java.awt.GridBagConstraints
import javax.swing.Box
import javax.swing.JPanel
import javax.swing.border.EmptyBorder

operator fun Dimension.times(factors: Pair<Double, Double>): Dimension {
    return Dimension((this.width * factors.first).toInt(), (this.height * factors.second).toInt())
}

operator fun Dimension.minus(subtrahend: Pair<Int, Int>): Dimension {
    return Dimension(this.width - subtrahend.first, this.height - subtrahend.second)
}

fun JPanel.gridBagVSpace(height: Int, constraints: GridBagConstraints) {
    this.add(Box.createRigidArea(Dimension(0, height)), constraints)
}

fun JPanel.emptyBorder(sides: Int) {
    this.border = EmptyBorder(sides, sides, sides, sides)
}