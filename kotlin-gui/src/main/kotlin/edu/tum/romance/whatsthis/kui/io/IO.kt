package edu.tum.romance.whatsthis.kui.io

import edu.tum.romance.whatsthis.kui.io.SourceFormat.Companion.DSL_EXT
import edu.tum.romance.whatsthis.kui.io.SourceFormat.Companion.YAML_EXT
import java.io.File
import java.io.InputStream
import java.io.OutputStream

@Suppress("unused")
object IO {
    fun export(file: File) {
        when {
            file.endsWith(DSL_EXT) -> exportAsCustomSources(file)
            else -> exportAsYamlSources(file)
        }
    }

    fun export(outputStream: OutputStream, ext: String = "YAML") {
        SourceFormat.fromIOStream(null, outputStream, ext).write()
    }

    fun import(file: File) {
        when {
            file.endsWith(DSL_EXT) -> importFromCustomSources(file)
            else -> importFromYamlSources(file)
        }
    }

    fun import(inputStream: InputStream, ext: String = "YAML") {
        SourceFormat.fromIOStream(inputStream, null, ext).load()
    }

    fun exportAsYamlSources(file: File) {
        val out =
            if(file.absolutePath.endsWith(YAML_EXT)) file
            else File(file.absolutePath + YAML_EXT)
        SourceFormat.fromFile(out).write()
    }

    fun importFromYamlSources(file: File) {
        val out =
            if(file.absolutePath.endsWith(YAML_EXT)) file
            else File(file.absolutePath + YAML_EXT)
        SourceFormat.fromFile(out).load()
    }

    fun exportAsCustomSources(file: File) {
        val out =
            if(file.absolutePath.endsWith(DSL_EXT)) file
            else File(file.absolutePath + DSL_EXT)
        SourceFormat.fromFile(out, "DSL").write()
    }

    fun importFromCustomSources(file: File) {
        val out =
            if(file.absolutePath.endsWith(DSL_EXT)) file
            else File(file.absolutePath + DSL_EXT)
        SourceFormat.fromFile(out, "DSL").load()
    }
}