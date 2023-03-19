package edu.tum.romance.whatsthis.kui.io

import edu.tum.romance.whatsthis.kui.io.SourceFormat.Companion.DSL_EXT
import java.io.File

@Suppress("unused")
object IO {
    fun export(file: File) {
        when {
            file.endsWith(DSL_EXT) -> exportAsCustomSources(file)
            else -> exportAsYamlSources(file)
        }
    }

    fun import(file: File) {
        when {
            file.endsWith(DSL_EXT) -> importFromCustomSources(file)
            else -> importFromYamlSources(file)
        }
    }

    fun exportAsYamlSources(file: File) {
        SourceFormat.fromFile(file).write()
    }

    fun importFromYamlSources(file: File) {
        SourceFormat.fromFile(file).load()
    }

    fun exportAsCustomSources(file: File) {
        SourceFormat.fromFile(file, "DSL").write()
    }

    fun importFromCustomSources(file: File) {
        SourceFormat.fromFile(file, "DSL").load()
    }
}