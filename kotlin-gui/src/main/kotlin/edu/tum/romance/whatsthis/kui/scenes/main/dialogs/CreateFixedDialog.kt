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
import edu.tum.romance.whatsthis.kui.util.FontCache.comfortaa
import edu.tum.romance.whatsthis.kui.util.times
import edu.tum.romance.whatsthis.v1.nlp.API
import edu.tum.romance.whatsthis.v1.util.urlRegex
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import java.net.URL
import javax.swing.*

object CreateFixedDialog: JDialog(Main, "Create Fixed Sample", true) {
    private var value: Pair<String, TextData<*>>? = null

    /* Sample Selector */
    private val chooser: JFileChooser = JFileChooser().apply {
        fileSelectionMode = JFileChooser.FILES_ONLY
    }
    private val sampleLabel = StyledLabel(MEDIUM, "Sample:")
    private val sampleUrlInput = HintTextField("(e.g. https://www.example.com)", MEDIUM).apply {
        addActionListener {
            if(urlRegex.matches(this.text) && value == null) {
                val data = TextData(URL(this.text), "")
                nameInput.text = data.titleSuggestion
                value = "" to data
                nameInput.requestFocusInWindow()
            }
        }
    }
    private val sampleFileInput = StyledButton(MEDIUM, "Select File", "Select File") {
        val result = chooser.showOpenDialog(this@CreateFixedDialog)
        if(result == JFileChooser.APPROVE_OPTION) {
            val file = chooser.selectedFile
            val data = TextData(file, "")
            nameInput.text = data.titleSuggestion
            value = nameInput.text to data
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
    private val nameInput = HintTextField("(e.g. Example)", MEDIUM).apply {
        if(value != null) {
            val data = value!!.second
            data.name = this.text
            value = "" to data
            spaceInput.requestFocusInWindow()
        }
    }

    /* Space Input */
    private val spaceLabel = StyledLabel(MEDIUM, "Space Name:")
    private val spaceTextInput = HintTextField("(e.g. Spam Mails)", MEDIUM)
    private val spaceDropDown = {
        API.spaces().sorted().let {
            if(it.isNotEmpty())
                JSpinner(SpinnerListModel(it)).apply {
                    this.font = comfortaa(MEDIUM)
                }
            else null }
    }
    private var spaceInput: JComponent = (spaceDropDown() ?: spaceTextInput).apply {
        if(this is JTextField) {
            this.addActionListener {
                if(this.text.isNotBlank() && value == null) {
                    value = this.text to TextData(URL(sampleUrlInput.text), nameInput.text)
                    confirm.doClick()
                }
            }
        } else if(this is JSpinner) {
            this.addKeyListener(object: KeyAdapter() {
                override fun keyPressed(e: KeyEvent) {
                    if(e.keyCode == KeyEvent.VK_ENTER) {
                        confirm.doClick()
                    }
                }
            })
        }
    }
    private val spaceSelectToggle = SymbolicButton(MEDIUM, "↹", "Toggle Input Method") {
        if(spaceInput == spaceTextInput && spaceDropDown() == null) {
            visualError("No Spaces Available.")
            return@SymbolicButton
        }

        val new = if(spaceInput == spaceTextInput) spaceDropDown()!! else spaceTextInput
        (this.contentPane.layout as GroupLayout).replace(spaceInput, new)
        spaceInput = new
    }

    private val confirm = StyledButton(MEDIUM, "Confirm", "Create Sample") {
        while(value != null) {
            val (space, data) = value!!
            if(space.isBlank()) break
            if(data.name.isBlank()) break
            return@StyledButton
        }

        val space = spaceValue()
        if(space.isBlank()) {
            visualError("Space Name cannot be blank.")
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
            space to TextData(URL(url), nameInput.text)
        } else {
            val file = chooser.selectedFile
            if(file == null) {
                visualError("No File Selected.")
                return@StyledButton
            }
            space to TextData(file, nameInput.text)
        }

        if(MainModel.variable) {
            SamplePane.selectedIndex = 0
        }

        clear()
        this@CreateFixedDialog.isVisible = false
    }
    private val cancel = StyledButton(MEDIUM, "Cancel", "Go back") {
        value = null
        clear()
        this@CreateFixedDialog.isVisible = false
    }

    init {
        // Prevent Closing before Selection
        defaultCloseOperation = DO_NOTHING_ON_CLOSE
        addWindowListener(object: WindowAdapter() {
            override fun windowClosing(e: WindowEvent?) { cancel.doClick() }
        })

        spaceTextInput.preferredSize = spaceInput.preferredSize * (2.2 to 1.0)
        nameInput.preferredSize = spaceTextInput.preferredSize
        sampleFileInput.preferredSize = spaceTextInput.preferredSize
        sampleUrlInput.preferredSize = spaceTextInput.preferredSize

        val layout = GroupLayout(contentPane)
        contentPane.layout = layout


        layout.autoCreateGaps = true
        layout.autoCreateContainerGaps = true

        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                    .addComponent(sampleLabel)
                    .addComponent(nameLabel)
                    .addComponent(spaceLabel))
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(sampleInput)
                        .addComponent(sampleSelectToggle))
                    .addComponent(nameInput)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(spaceInput)
                        .addComponent(spaceSelectToggle))))
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
                .addComponent(spaceLabel)
                .addComponent(spaceInput)
                .addComponent(spaceSelectToggle))
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                .addComponent(confirm)
                .addComponent(cancel)))

        layout.linkSize(SwingConstants.HORIZONTAL, confirm, cancel)
        layout.linkSize(SwingConstants.VERTICAL, confirm, cancel)
    }

    private fun spaceValue(): String {
        if(spaceInput is JTextField) {
            return (spaceInput as JTextField).text
        } else if(spaceInput is JSpinner) {
            return (spaceInput as JSpinner).value as String
        }
        error("Unknown Input Type: ${spaceInput.javaClass.simpleName}")
    }

    private fun clear() {
        chooser.selectedFile = null
        sampleUrlInput.text = ""
        nameInput.text = ""
        spaceTextInput.text = ""
    }

    fun open(): Pair<String, TextData<*>>? {
        value = null
        val layout = contentPane.layout as GroupLayout

        layout.linkSize(SwingConstants.HORIZONTAL, sampleInput, nameInput, spaceInput)
        layout.linkSize(SwingConstants.VERTICAL, sampleInput, nameInput, spaceInput)

        this.pack()
        this.setLocationRelativeTo(Main)
        cancel.requestFocusInWindow()
        this.isVisible = true
        return value
    }
}