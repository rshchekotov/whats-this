package edu.tum.romance.whatsthis.v2.data

import java.io.File

class FileSource(file: File): DataSource<File>(file) {
    override fun load(): Pair<String, String> {
        return when(source.extension) {
            "html" -> parseHTML(source.readText()).let { it.first to (it.second ?: source.nameWithoutExtension) }
            "txt" -> source.readText() to source.nameWithoutExtension
            "pdf" -> parsePDF(source.inputStream()).let { it.first to (it.second ?: source.nameWithoutExtension) }
            else -> error("Unsupported file type")
        }
    }
}