package edu.tum.romance.whatsthis.ui

import com.formdev.flatlaf.FlatLightLaf
import edu.tum.romance.whatsthis.ui.views.View
import edu.tum.romance.whatsthis.ui.views.menu.MenuView
import java.awt.Dimension
import java.awt.GraphicsEnvironment
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JOptionPane
import javax.swing.UIManager

@Suppress("unused")
object ClassificationFrame: JFrame() {
    private const val width = 800
    private const val height = 600

    val fonts = arrayOf(
        FontCache.font("Comfortaa-Regular", 12f),
        FontCache.font("Comfortaa-Regular", 16f),
        FontCache.font("Comfortaa-Regular", 32f)
    )
    private var content: View = MenuView

    fun open() {
        try {
            UIManager.setLookAndFeel(FlatLightLaf())
            for(font in fonts) {
                GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        this.title = "What's This?"
        this.size = Dimension(width, height)
        this.defaultCloseOperation = EXIT_ON_CLOSE
        this.setLocationRelativeTo(null)
        this.add(content)
        this.pack()
        this.isVisible = true
    }

    fun swapView(view: View) {
        this.content.onUnload()
        this.remove(content)
        this.content = view
        this.add(content)
        this.pack()
        this.content.onLoad()
    }

    fun componentSize(percentage: Pair<Double, Double>): Dimension {
        return Dimension((width * percentage.first).toInt(), (height * percentage.second).toInt())
    }

    fun unimplemented() = visualInfo("Not implemented yet!", 1)

    fun visualInfo(message: String, size: Int = 0) {
        val label = JLabel(message)
        label.font = fonts[size]
        JOptionPane.showMessageDialog(this, label,
            "Information", JOptionPane.INFORMATION_MESSAGE)
    }

    fun visualError(message: String) {
        val label = JLabel(message)
        label.font = fonts[0]
        JOptionPane.showMessageDialog(this, label,
            "Error", JOptionPane.ERROR_MESSAGE)
    }

    fun visualQuestion(message: String): Boolean {
        val label = JLabel(message)
        label.font = fonts[0]
        return JOptionPane.showConfirmDialog(this, label,
            "Question", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION
    }
}