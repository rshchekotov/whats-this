package edu.tum.romance.whatsthis.io

import org.apache.pdfbox.Loader
import org.jsoup.Jsoup
import java.net.URL

/**
 * WebSourceData is a class that can fetch text from
 * a URL and convert it to a list of Token-Frequency pairs.
 *
 * The following Mimetypes are supported:
 * - text/html
 * - text/plain
 * - application/pdf
 */
class WebSourceData(override val source: URL, override var name: String) : TextData<URL>() {
    override lateinit var text: String
    override lateinit var titleSuggestion: String

    init {
        val (text, title) = text()
        this.text = text
        this.titleSuggestion = title
    }


    private fun text(): Pair<String, String> {
        val connection = source.openConnection()
        return when (connection.contentType.split(";")[0]) {
            "text/html" -> {
                val parsed = Jsoup.parse(source.readText())
                Pair(parsed.body().text(), parsed.title())
            }
            "text/plain" -> {
                source.readText() to source.path.split("/").last()
            }
            "application/pdf" -> {
                val pdf = Loader.loadPDF(source.openStream())
                getPDF(pdf) to (pdf.documentInformation.title ?: source.path.split("/").last())
            }
            else -> error("Unsupported Mimetype")
        }
    }
}