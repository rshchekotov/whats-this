package edu.tum.romance.whatsthis.kui.scenes.main.menubar

import edu.tum.romance.whatsthis.kui.Main
import edu.tum.romance.whatsthis.kui.event.EventHandler
import edu.tum.romance.whatsthis.kui.event.events.progress.ModelLoadEvent
import edu.tum.romance.whatsthis.kui.io.IO
import edu.tum.romance.whatsthis.kui.io.SourceFormat
import edu.tum.romance.whatsthis.kui.scenes.main.MainView
import edu.tum.romance.whatsthis.kui.scenes.menu.MenuView
import edu.tum.romance.whatsthis.kui.util.DialogUtils.visualError
import edu.tum.romance.whatsthis.kui.util.DialogUtils.visualInfo
import edu.tum.romance.whatsthis.kui.util.DialogUtils.visualQuestion
import edu.tum.romance.whatsthis.kui.util.FontCache.SMALL
import edu.tum.romance.whatsthis.kui.util.FontCache.comfortaa
import edu.tum.romance.whatsthis.v1.nlp.API
import java.io.File
import javax.swing.JFileChooser
import javax.swing.JMenu
import javax.swing.JMenuItem
import javax.swing.JOptionPane.CANCEL_OPTION
import javax.swing.JOptionPane.YES_OPTION
import javax.swing.filechooser.FileFilter

object FileMenu: JMenu("File") {
    private var saveState: Pair<Int, String>? = null

    private val chooser = JFileChooser()
    private val yamlFilter = object: FileFilter() {
        override fun accept(f: File): Boolean {
            return f.isDirectory || f.absolutePath.endsWith(SourceFormat.YAML_EXT)
        }
        override fun getDescription() = "YAML Model (${SourceFormat.YAML_EXT})"
    }
    private val dslFilter = object: FileFilter() {
        override fun accept(f: File): Boolean {
            return f.isDirectory || f.absolutePath.endsWith(SourceFormat.DSL_EXT)
        }
        override fun getDescription() = "Custom DSL Model (${SourceFormat.DSL_EXT})"
    }

    init {
        chooser.fileSelectionMode = JFileChooser.FILES_ONLY
        chooser.isAcceptAllFileFilterUsed = false
        chooser.addChoosableFileFilter(yamlFilter)
        chooser.addChoosableFileFilter(dslFilter)

        font = comfortaa(SMALL)

        val new = JMenuItem("New Model")
        new.font = comfortaa(SMALL)
        new.addActionListener {
            if(shouldSave()) {
                val response = visualQuestion("Do you want to save the current model?")
                if(response == YES_OPTION) {
                    if(saveModel() != JFileChooser.APPROVE_OPTION) {
                        visualError("Could not save model! Aborting...")
                        return@addActionListener
                    }
                } else if(response == CANCEL_OPTION) {
                    return@addActionListener
                }
            }
            API.clear()
            ModelLoadEvent().dispatch()
        }
        add(new)

        val open = JMenuItem("Open Model")
        open.font = comfortaa(SMALL)
        open.addActionListener {
            if(shouldSave()) {
                val response = visualQuestion("Do you want to save the current model?")
                if(response == YES_OPTION) {
                    if(saveModel() != JFileChooser.APPROVE_OPTION) {
                        visualError("Could not save model! Aborting...")
                        return@addActionListener
                    }
                } else if(response == CANCEL_OPTION) {
                    return@addActionListener
                }
            }
            loadModel()
        }
        add(open)

        val save = JMenuItem("Save Model")
        save.font = comfortaa(SMALL)
        save.addActionListener {
            saveModel()
        }
        add(save)

        val close = JMenuItem("Close Model")
        close.font = comfortaa(SMALL)
        close.addActionListener {
            if(shouldSave()) {
                val response = visualQuestion("Do you want to save the current model?")
                if(response == YES_OPTION) {
                    if(saveModel() != JFileChooser.APPROVE_OPTION) {
                        visualError("Could not save model! Aborting...")
                        return@addActionListener
                    }
                } else if(response == CANCEL_OPTION) {
                    return@addActionListener
                }
            }
            API.clear()
            ModelLoadEvent().dispatch()
            MenuView.switch()
        }
        add(close)
    }

    fun shouldSave() = !API.isEmpty() && API.hashCode() != saveState?.first

    fun loadModel() {
        chooser.selectedFile = null
        val result = chooser.showOpenDialog(Main)
        if(result == JFileChooser.APPROVE_OPTION) {
            if(Main.currentView is MenuView) {
                MainView.switch()
            }

            if (chooser.fileFilter == dslFilter) {
                IO.importFromCustomSources(chooser.selectedFile)
            } else {
                IO.importFromYamlSources(chooser.selectedFile)
            }
        }
    }

    fun saveModel(): Int {
        if(API.isEmpty()) {
            visualInfo("Cannot save empty model!")
            return JFileChooser.ERROR_OPTION
        }

        chooser.selectedFile = (saveState?.second?.let { File(it) })
        val result = chooser.showSaveDialog(Main)
        if(result == JFileChooser.APPROVE_OPTION) {
            if(chooser.selectedFile.exists()) {
                val response = visualQuestion("File already exists. Overwrite?")
                if(response != YES_OPTION) {
                    return JFileChooser.CANCEL_OPTION
                }
            }
            if (chooser.fileFilter == dslFilter) {
                IO.exportAsCustomSources(chooser.selectedFile)
            } else {
                IO.exportAsYamlSources(chooser.selectedFile)
            }
            saveState = API.hashCode() to chooser.selectedFile.absolutePath
        }
        return result
    }

    @EventHandler(ModelLoadEvent::class)
    fun onModelLoad() {
        if(chooser.selectedFile != null) {
            saveState = API.hashCode() to chooser.selectedFile.absolutePath
        }
    }
}