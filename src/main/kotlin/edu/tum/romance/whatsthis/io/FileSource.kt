package edu.tum.romance.whatsthis.io

import org.apache.pdfbox.Loader
import org.jsoup.Jsoup
import java.io.File

class FileSource(override val source: File, override val name: String): TextData<File>() {
    override var text: String = text()
    override var titleSuggestion: String = source.nameWithoutExtension

    private fun text(): String {
        return when (source.extension) {
            "html" -> Jsoup.parse(source.readText()).body().text()
            "pdf" -> getPDF(Loader.loadPDF(source))
            else -> source.readText()
        }
    }
}