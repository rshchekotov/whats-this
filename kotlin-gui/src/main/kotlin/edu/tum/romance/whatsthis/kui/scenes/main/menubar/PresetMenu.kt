package edu.tum.romance.whatsthis.kui.scenes.main.menubar

import edu.tum.romance.whatsthis.kui.io.IO
import edu.tum.romance.whatsthis.kui.util.DialogUtils
import edu.tum.romance.whatsthis.kui.util.FontCache.SMALL
import edu.tum.romance.whatsthis.kui.util.FontCache.comfortaa
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.util.*
import java.util.zip.ZipFile
import java.util.zip.ZipInputStream
import javax.swing.JFileChooser
import javax.swing.JMenu
import javax.swing.JMenuItem
import javax.swing.JOptionPane

object PresetMenu: JMenu("Presets") {
    init {
        font = comfortaa(SMALL)

        var items = 0
        val modelResources = javaClass.classLoader.getResource("models")
        if(modelResources != null) {
            val location = modelResources.toExternalForm()
            items = if(location.startsWith("jar:")) {
                modelResources.openStream().use {
                    val fileName = location.substring(9, location.indexOf("!"))
                    val file = ZipFile(fileName)
                    val zip = ZipInputStream(FileInputStream(File(fileName)))
                    return@use loadFromStream(file, zip)
                }
            } else {
                loadFromFile(File(modelResources.toURI()))
            }
        }
        if(items == 0) {
            val emptyItem = JMenuItem("No presets found")
            emptyItem.isEnabled = false
        }
    }

    private fun loadFromFile(file: File): Int {
        var items = 0
        if(file.isDirectory) {
            file.listFiles()!!.forEach {
                loadCommon(it.name, it.inputStream())
                items++
            }
        }
        return items
    }

    private fun loadFromStream(file: ZipFile, stream: ZipInputStream): Int {
        var items = 0
        var entry = stream.nextEntry
        while(entry != null) {
            val prefix = "models/"
            if(!entry.name.startsWith(prefix)) {
                entry = stream.nextEntry
                continue
            }

            if(!entry.isDirectory) {
                val name = entry.name.substring(prefix.length)
                loadCommon(name, file.getInputStream(entry))
                items++
            }
            entry = stream.nextEntry
        }
        return items
    }

    private fun loadCommon(name: String, data: InputStream) {
        val title = name.split(".")[0]
            .replace("_", " ")
            .replace("-", " ")
            .split(Regex("\\s+"))
            .joinToString(" ") { word ->
                word.replaceFirstChar {  char ->
                    if (char.isLowerCase()) char.titlecase(Locale.getDefault())
                    else char.toString()
                }
            }
        val item = JMenuItem(title)
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
            IO.import(data)
        }
        add(item)
    }
}