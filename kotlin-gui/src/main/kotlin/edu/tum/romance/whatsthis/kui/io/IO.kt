package edu.tum.romance.whatsthis.kui.io

import java.io.File

@Suppress("unused")
object IO {
    fun exportAsYamlSources(file: File) {
        SourceFormat.fromFile(file).write()
    }

    fun importAsYamlSources(file: File) {
        SourceFormat.fromFile(file).load()
    }

    fun exportAsCustomSources(file: File) {
        SourceFormat.fromFile(file, "DSL").write()
    }

    fun importFromCustomSources(file: File) {
        SourceFormat.fromFile(file, "DSL").load()
    }
}