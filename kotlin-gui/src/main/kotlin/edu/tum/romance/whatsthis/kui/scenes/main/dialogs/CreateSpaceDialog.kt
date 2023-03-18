package edu.tum.romance.whatsthis.kui.scenes.main.dialogs

import edu.tum.romance.whatsthis.kui.Main
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import javax.swing.JDialog
import javax.swing.JOptionPane

object CreateSpaceDialog: JDialog(Main, "Create Space", true) {
    init {
        defaultCloseOperation = DO_NOTHING_ON_CLOSE
        addWindowListener(object: WindowAdapter() {
            override fun windowClosing(e: WindowEvent?) { }
        })
    }

    fun open(): Any {
        val optionPane = JOptionPane(
            "Enter Space Name",
            JOptionPane.PLAIN_MESSAGE,
            JOptionPane.OK_CANCEL_OPTION)

        optionPane.wantsInput = true
        optionPane.componentOrientation = Main.componentOrientation
        optionPane.initialValue = null
        optionPane.addPropertyChangeListener { e ->
            if (isVisible && e.source == optionPane && e.propertyName == JOptionPane.VALUE_PROPERTY) {
                isVisible = false
            }
        }
        contentPane = optionPane
        pack()
        setLocationRelativeTo(Main)
        optionPane.selectInitialValue()

        isVisible = true
        return optionPane.inputValue
    }
}