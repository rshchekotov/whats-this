package edu.tum.romance.whatsthis.kui.scenes.main.menubar

import edu.tum.romance.whatsthis.kui.io.IO
import edu.tum.romance.whatsthis.kui.util.DialogUtils
import edu.tum.romance.whatsthis.kui.util.FontCache.SMALL
import edu.tum.romance.whatsthis.kui.util.FontCache.comfortaa
import java.io.File
import java.util.*
import javax.swing.JFileChooser
import javax.swing.JMenu
import javax.swing.JMenuItem
import javax.swing.JOptionPane

object PresetMenu: JMenu("Presets") {
    init {
        var items = 0
        val modelResources = javaClass.classLoader.getResource("models")
        if(modelResources != null) {
            val modelDir = File(modelResources.toURI())
            if(modelDir.isDirectory) {
                modelDir.listFiles()!!.forEach {
                    val name = it.name.split(".")[0]
                        .replace("_", " ")
                        .replace("-", " ")
                        .split(Regex("\\s+"))
                        .joinToString(" ") { word ->
                            word.replaceFirstChar {  char ->
                                if (char.isLowerCase()) char.titlecase(Locale.getDefault())
                                else char.toString()
                            }
                        }
                    val item = JMenuItem(name)
                    item.font = comfortaa(SMALL)
                    item.addActionListener { _ ->
                        if(FileMenu.shouldSave()) {
                            val response = DialogUtils.visualQuestion("Do you want to save the current model?")
                            if(response == JOptionPane.YES_OPTION) {
                                if(FileMenu.saveModel() != JFileChooser.APPROVE_OPTION) {
                                    DialogUtils.visualError("Could not save model! Aborting...")
                                    return@addActionListener
                                }
                            } else if(response == JOptionPane.CANCEL_OPTION) {
                                return@addActionListener
                            }
                        }
                        IO.import(it)
                    }
                    add(item)
                    items++
                }
            }
        }
        if(items == 0) {
            val emptyItem = JMenuItem("No presets found")
            emptyItem.isEnabled = false
        }
    }
}