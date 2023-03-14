package edu.tum.romance.whatsthis.io.data

import org.apache.pdfbox.Loader
import org.jsoup.Jsoup
import java.io.File

class FileSource(override var source: File, override var name: String): TextData<File>() {
    override var text: String = text()
    override var titleSuggestion: String = source.nameWithoutExtension
    private fun text(): String {
        return when (source.extension) {
            "html" -> Jsoup.parse(source.readText()).body().text()
            "pdf" -> getPDF(Loader.loadPDF(source))
            else -> source.readText()
        }
    }

    override fun deserialize(data: String) {
        val fragments = data.split(":")
        if(fragments.size != 3 || fragments[0] != id) error("Invalid data: $data")
        this.source = File(fragments[2])
        if(!this.source.exists()) error("File does not exist: ${this.source.absolutePath}")
        this.name = fragments[1]
    }

    override fun serialize(): String {
        return "$id:$name:${source.absolutePath}"
    }

    companion object {
        const val id = "file"
    }
}