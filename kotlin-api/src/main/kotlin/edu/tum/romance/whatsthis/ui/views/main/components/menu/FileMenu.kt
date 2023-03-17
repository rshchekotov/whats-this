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

private const val WTS_YAML = "What's This Sources YAML (*.wts.yaml)"
private const val WTS = "What's This Source DSL (*.wts)"

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
                    if(fileChooser.fileFilter.description == WTS_YAML) {
                        return@ChooserAction IO.exportAsYamlSources(file)
                    } else if(fileChooser.fileFilter.description == WTS) {
                        return@ChooserAction IO.exportAsCustomSources(file)
                    }
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
                    if(chooser.fileFilter.description == WTS) {
                        return@ChooserAction confirmOverwrite { IO.importAsYamlSources(file) }
                    } else if(chooser.fileFilter.description == WTS_YAML) {
                        return@ChooserAction confirmOverwrite { IO.importFromCustomSources(file) }
                    }
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
                    f.absolutePath.endsWith(".wts.yaml")
            override fun getDescription() = WTS_YAML
        })
        fileChooser.addChoosableFileFilter(object : FileFilter() {
            override fun accept(f: File) = f.isDirectory || f.extension == "wts"
            override fun getDescription() = WTS
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