package edu.tum.romance.whatsthis.kui.scenes.main.dialogs

import edu.tum.romance.whatsthis.kui.Main
import edu.tum.romance.whatsthis.kui.components.HintTextField
import edu.tum.romance.whatsthis.kui.components.StyledButton
import edu.tum.romance.whatsthis.kui.components.StyledLabel
import edu.tum.romance.whatsthis.kui.util.DialogUtils.visualError
import edu.tum.romance.whatsthis.kui.util.FontCache.MEDIUM
import edu.tum.romance.whatsthis.kui.util.times
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import javax.swing.GroupLayout
import javax.swing.JDialog
import javax.swing.SwingConstants

object CreateSpaceDialog: JDialog(Main, "Create Space", true) {
    private var value: String? = null

    private val label = StyledLabel(MEDIUM, "Space Name:")
    private val input = HintTextField("(e.g. Spam Mails)", MEDIUM)
    private val confirm = StyledButton(MEDIUM, "Confirm", "Create Space") {
        if(input.text.isBlank()) {
            visualError("Space Name cannot be blank.")
            return@StyledButton
        }
        value = input.text
        input.text = ""
        this@CreateSpaceDialog.isVisible = false
    }
    private val cancel = StyledButton(MEDIUM, "Cancel", "Go back") {
        value = null
        input.text = ""
        this@CreateSpaceDialog.isVisible = false
    }

    init {
        // Prevent Closing before Selection
        defaultCloseOperation = DO_NOTHING_ON_CLOSE
        addWindowListener(object: WindowAdapter() {
            override fun windowClosing(e: WindowEvent?) { cancel.doClick() }
        })

        input.addActionListener { confirm.doClick() }
        input.preferredSize = input.preferredSize * (1.3 to 1.0)

        val layout = GroupLayout(contentPane)
        contentPane.layout = layout


        layout.autoCreateGaps = true
        layout.autoCreateContainerGaps = true

        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(label)
                .addComponent(input))
            .addGroup(layout.createSequentialGroup()
                .addComponent(confirm)
                .addComponent(cancel)))

        layout.setVerticalGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                .addComponent(label)
                .addComponent(input))
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                .addComponent(confirm)
                .addComponent(cancel)))

        layout.linkSize(SwingConstants.HORIZONTAL, confirm, cancel)
    }

    fun open(): String? {
        this.pack()
        this.setLocationRelativeTo(Main)
        cancel.requestFocusInWindow()
        this.isVisible = true
        return value
    }
}