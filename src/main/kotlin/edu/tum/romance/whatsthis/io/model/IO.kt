package edu.tum.romance.whatsthis.io.model

import java.io.File

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