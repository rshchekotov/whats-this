package edu.tum.romance.whatsthis.kui.scenes.main.dialogs

import edu.tum.romance.whatsthis.v1.data.TextData
import edu.tum.romance.whatsthis.kui.Main
import edu.tum.romance.whatsthis.kui.components.HintTextField
import edu.tum.romance.whatsthis.kui.components.StyledButton
import edu.tum.romance.whatsthis.kui.components.StyledLabel
import edu.tum.romance.whatsthis.kui.components.SymbolicButton
import edu.tum.romance.whatsthis.kui.scenes.main.MainModel
import edu.tum.romance.whatsthis.kui.scenes.main.components.samples.SamplePane
import edu.tum.romance.whatsthis.kui.util.DialogUtils.visualError
import edu.tum.romance.whatsthis.kui.util.FontCache.MEDIUM
import edu.tum.romance.whatsthis.kui.util.times
import edu.tum.romance.whatsthis.v1.util.urlRegex
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import java.net.URL
import javax.swing.*

object CreateVariableDialog: JDialog(Main, "Create Fixed Sample", true) {
    private var value: TextData<*>? = null

    /* Sample Selector */
    private val chooser: JFileChooser = JFileChooser().apply {
        fileSelectionMode = JFileChooser.FILES_ONLY
    }
    private val sampleLabel = StyledLabel(MEDIUM, "Sample:")
    private val sampleUrlInput = HintTextField("(e.g. https://www.example.com)", MEDIUM).apply {
        addActionListener {
            if(urlRegex.matches(text)) {
                value = TextData(URL(text), "")
                nameInput.text = value!!.titleSuggestion
                nameInput.requestFocusInWindow()
            }
        }
    }
    private val sampleFileInput = StyledButton(MEDIUM, "Select File", "Select File") {
        val response = chooser.showOpenDialog(this@CreateVariableDialog)
        if(response == JFileChooser.APPROVE_OPTION) {
            val file = chooser.selectedFile
            value = TextData(file, "")
            nameInput.text = value!!.titleSuggestion
            nameInput.requestFocusInWindow()
        }
    }
    private var sampleInput: JComponent = sampleUrlInput
    private val sampleSelectToggle = SymbolicButton(MEDIUM, "↹", "Toggle Input Method") {
        val new = if(sampleInput == sampleUrlInput) sampleFileInput else sampleUrlInput
        (this.contentPane.layout as GroupLayout).replace(sampleInput, new)
        sampleInput = new
    }

    /* Sample Name  */
    private val nameLabel = StyledLabel(MEDIUM, "Name:")
    private val nameInput = HintTextField("(e.g. Example)", MEDIUM)

    private val confirm = StyledButton(MEDIUM, "Confirm", "Create Sample") {
        while(value != null) {
            if(value!!.name.isBlank()) break
            return@StyledButton
        }

        if(nameInput.text.isBlank()) {
            visualError("Sample Name cannot be blank.")
            return@StyledButton
        }

        value = if(sampleInput is JTextField) {
            val url = (sampleInput as JTextField).text
            if(url.isBlank()) {
                visualError("Sample URL cannot be blank.")
                return@StyledButton
            }
            TextData(URL(url), nameInput.text)
        } else {
            val file = chooser.selectedFile
            if(file == null) {
                visualError("No File Selected.")
                return@StyledButton
            }
            TextData(file, nameInput.text)
        }

        if(!MainModel.variable) {
            SamplePane.selectedIndex = 1
        }

        clear()
        this@CreateVariableDialog.isVisible = false
    }
    private val cancel = StyledButton(MEDIUM, "Cancel", "Go back") {
        value = null
        clear()
        this@CreateVariableDialog.isVisible = false
    }

    init {
        // Prevent Closing before Selection
        defaultCloseOperation = DO_NOTHING_ON_CLOSE
        addWindowListener(object: WindowAdapter() {
            override fun windowClosing(e: WindowEvent?) { cancel.doClick() }
        })

        nameInput.preferredSize = nameInput.preferredSize * (2.2 to 1.0)
        sampleFileInput.preferredSize = nameInput.preferredSize
        sampleUrlInput.preferredSize = nameInput.preferredSize

        nameInput.addActionListener {
            if(value != null) {
                value!!.text = nameInput.text
                confirm.doClick()
            }
        }

        val layout = GroupLayout(contentPane)
        contentPane.layout = layout


        layout.autoCreateGaps = true
        layout.autoCreateContainerGaps = true

        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                    .addComponent(sampleLabel)
                    .addComponent(nameLabel))
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(sampleInput)
                        .addComponent(sampleSelectToggle))
                    .addComponent(nameInput)))
            .addGroup(layout.createSequentialGroup()
                .addComponent(confirm)
                .addComponent(cancel)))

        layout.setVerticalGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                .addComponent(sampleLabel)
                .addComponent(sampleInput)
                .addComponent(sampleSelectToggle))
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                .addComponent(nameLabel)
                .addComponent(nameInput))
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                .addComponent(confirm)
                .addComponent(cancel)))

        layout.linkSize(SwingConstants.HORIZONTAL, confirm, cancel)
        layout.linkSize(SwingConstants.VERTICAL, confirm, cancel)
    }

    private fun clear() {
        chooser.selectedFile = null
        sampleUrlInput.text = ""
        nameInput.text = ""
    }

    fun open(): TextData<*>? {
        value = null
        val layout = contentPane.layout as GroupLayout

        layout.linkSize(SwingConstants.HORIZONTAL, sampleInput, nameInput)
        layout.linkSize(SwingConstants.VERTICAL, sampleInput, nameInput)

        this.pack()
        this.setLocationRelativeTo(Main)
        cancel.requestFocusInWindow()
        this.isVisible = true
        return value
    }
}