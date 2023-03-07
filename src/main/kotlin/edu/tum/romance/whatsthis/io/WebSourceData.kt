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
class WebSourceData(override val source: URL) : TextData<URL>() {
    override var text: String = text()

    private fun text(): String {
        val connection = source.openConnection()
        return when (connection.contentType.split(";")[0]) {
            "text/html" -> Jsoup.parse(source.readText()).body().text()
            "text/plain" -> source.readText()
            "application/pdf" -> getPDF(Loader.loadPDF(source.openStream()))
            else -> error("Unsupported Mimetype")
        }
    }
}