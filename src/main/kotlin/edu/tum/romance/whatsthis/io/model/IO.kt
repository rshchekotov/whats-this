package edu.tum.romance.whatsthis.io.model

import java.io.File

object IO {
    @Suppress("unused") // To be done
    fun exportAsModel(file: File) {
        TODO(file.absolutePath)
    }

    @Suppress("unused") // To be done
    fun importFromModel(file: File) {
        TODO(file.absolutePath)
    }

    fun exportAsSources(file: File) {
        SourceFormat.fromFile(file).write()
    }

    fun importFromSources(file: File) {
        SourceFormat.fromFile(file).load()
    }
}