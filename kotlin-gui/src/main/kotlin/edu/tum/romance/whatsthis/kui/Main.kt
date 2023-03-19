package edu.tum.romance.whatsthis.kui

import com.formdev.flatlaf.FlatDarkLaf
import edu.tum.romance.whatsthis.kui.scenes.View
import edu.tum.romance.whatsthis.kui.scenes.main.menubar.AppearanceMenu
import edu.tum.romance.whatsthis.kui.scenes.main.menubar.FileMenu
import edu.tum.romance.whatsthis.kui.scenes.menu.MenuView
import edu.tum.romance.whatsthis.kui.util.DialogUtils
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import javax.swing.JFileChooser
import javax.swing.JFrame
import javax.swing.JOptionPane.CANCEL_OPTION
import javax.swing.JOptionPane.YES_OPTION
import kotlin.system.exitProcess

object Main: JFrame() {
    var frameWidth: Int
    var frameHeight: Int

    var currentView: View = MenuView

    init {
        val screenSize = java.awt.Toolkit.getDefaultToolkit().screenSize
        frameWidth = (screenSize.width * 0.8).toInt()
        frameHeight = (screenSize.height * 0.8).toInt()

        title = "What's This?"
        defaultCloseOperation = DO_NOTHING_ON_CLOSE
        addWindowListener(object: WindowAdapter() {
            override fun windowClosing(e: WindowEvent?) {
                if(FileMenu.shouldSave()) {
                    val response = DialogUtils.visualQuestion("Do you want to save the current model?")
                    if(response == YES_OPTION) {
                        if(FileMenu.saveModel() != JFileChooser.APPROVE_OPTION) {
                            DialogUtils.visualError("Could not save model! Aborting...")
                            return
                        }
                    } else if(response == CANCEL_OPTION) {
                        return
                    }
                }
                dispose()
                exitProcess(0)
            }
        })
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