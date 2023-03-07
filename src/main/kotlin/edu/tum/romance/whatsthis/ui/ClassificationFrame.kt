package edu.tum.romance.whatsthis.ui

import edu.tum.romance.whatsthis.ui.panels.MainMenu
import java.awt.Dimension
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JOptionPane
import javax.swing.JPanel

object ClassificationFrame: JFrame() {
    const val width = 800
    const val height = 600

    val fonts = arrayOf(
        FontCache.font("Comfortaa-Regular", 12f),
        FontCache.font("Comfortaa-Regular", 16f),
        FontCache.font("Comfortaa-Regular", 32f)
    )
    private var content: JPanel = MainMenu

    fun open() {
        this.title = "What's This?"
        this.size = Dimension(width, height)
        this.defaultCloseOperation = EXIT_ON_CLOSE
        this.setLocationRelativeTo(null)
        add(content)
        this.pack()
        this.isVisible = true
    }

    fun swapView(view: JPanel) {
        content.isEnabled = false
        remove(content)
        content = view
        add(content)
        this.pack()
        content.isEnabled = true
    }

    fun unimplemented() {
        val label = JLabel("Not implemented yet!")
        label.font = fonts[1]
        JOptionPane.showMessageDialog(this, label,
            "Not implemented", JOptionPane.INFORMATION_MESSAGE)
    }

    fun visualError(message: String) {
        val label = JLabel(message)
        label.font = fonts[0]
        JOptionPane.showMessageDialog(this, label,
            "Error", JOptionPane.ERROR_MESSAGE)
    }
}