package edu.tum.romance.whatsthis.kui

import com.formdev.flatlaf.FlatDarkLaf
import edu.tum.romance.whatsthis.kui.scenes.View
import edu.tum.romance.whatsthis.kui.scenes.main.menubar.AppearanceMenu
import edu.tum.romance.whatsthis.kui.scenes.menu.MenuView
import javax.swing.JFrame

object Main: JFrame() {
    var frameWidth: Int
    var frameHeight: Int

    var currentView: View = MenuView

    init {
        val screenSize = java.awt.Toolkit.getDefaultToolkit().screenSize
        frameWidth = (screenSize.width * 0.8).toInt()
        frameHeight = (screenSize.height * 0.8).toInt()

        title = "What's This?"
        defaultCloseOperation = EXIT_ON_CLOSE
        isResizable = false

        add(currentView)
        pack()
        setLocationRelativeTo(null)

        AppearanceMenu.setLookAndFeel(::FlatDarkLaf, "Dark")
    }

    @JvmStatic
    fun main(args: Array<String>) {
        this.isVisible = true
    }
}