package edu.tum.romance.whatsthis.io.data

import org.apache.pdfbox.Loader
import org.jsoup.Jsoup
import java.io.File

class FileSource(override var source: File, override var name: String): TextData<File>() {
    override val sourceID: String = "file"
    override var text: String = text()
    override var titleSuggestion: String = source.nameWithoutExtension
    private fun text(): String {
        return when (source.extension) {
            "html" -> Jsoup.parse(source.readText()).body().text()
            "pdf" -> getPDF(Loader.loadPDF(source))
            else -> source.readText()
        }
    }

    override fun toString(): String {
        return this.source.absolutePath
    }
}