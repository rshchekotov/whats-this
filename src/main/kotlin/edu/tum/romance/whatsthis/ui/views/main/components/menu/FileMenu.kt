package edu.tum.romance.whatsthis.ui.views.main.components.menu

import edu.tum.romance.whatsthis.io.model.IO
import edu.tum.romance.whatsthis.nlp.API
import edu.tum.romance.whatsthis.ui.ClassificationFrame
import edu.tum.romance.whatsthis.ui.views.main.MainView
import edu.tum.romance.whatsthis.util.observer.trigger
import java.awt.event.KeyEvent
import java.io.File
import javax.swing.JFileChooser
import javax.swing.JMenu
import javax.swing.JMenuItem
import javax.swing.JOptionPane
import javax.swing.filechooser.FileFilter

private const val SOURCES = "What's This Sources (*.wts or *.wts.yaml)"
private const val MODELS = "What's This Model (*.wtm)"

object FileMenu: JMenu("File") {
    init {
        font = ClassificationFrame.fonts[0]

        val save = JMenuItem("Save")
        save.font = ClassificationFrame.fonts[0]
        save.mnemonic = KeyEvent.VK_S
        save.addActionListener {
            // Display File Chooser
            val fileChooser = createChooser()
            fileChooser.addActionListener ChooserAction@ {
                val file: File? = fileChooser.selectedFile
                if(file != null) {
                    return@ChooserAction IO.exportAsSources(file)
                }
            }
            fileChooser.showSaveDialog(ClassificationFrame)
        }
        add(save)

        val load = JMenuItem("Load")
        load.font = ClassificationFrame.fonts[0]
        load.mnemonic = KeyEvent.VK_L
        load.addActionListener {
            val chooser = createChooser()
            chooser.addActionListener ChooserAction@ {
                val file: File? = chooser.selectedFile
                if(file != null) {
                    return@ChooserAction confirmOverwrite { IO.importFromSources(file) }
                }
            }
            chooser.showOpenDialog(ClassificationFrame)
        }
        add(load)
    }

    private fun createChooser(): JFileChooser {
        val fileChooser = JFileChooser()
        fileChooser.isAcceptAllFileFilterUsed = false
        fileChooser.addChoosableFileFilter(object : FileFilter() {
            override fun accept(f: File) = f.isDirectory ||
                    f.absolutePath.endsWith(".wts") ||
                    f.absolutePath.endsWith(".wts.yaml")
            override fun getDescription() = SOURCES
        })
        fileChooser.addChoosableFileFilter(object : FileFilter() {
            override fun accept(f: File) = f.isDirectory || f.extension == "wtm"
            override fun getDescription() = MODELS
        })
        return fileChooser
    }

    private fun confirmOverwrite(import: () -> Unit) {
        if(API.isEmpty()) return import()

        val option = JOptionPane.showOptionDialog(ClassificationFrame,
            "Do you want to overwrite the current model?", "Overwrite", JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE, null, arrayOf("Yes", "No"), "No")
        if(option == JOptionPane.YES_OPTION) {
            API.clear()
            MainView.spaceUpdate.trigger()
            MainView.dataUpdate.trigger()
            import()
        }
    }
}