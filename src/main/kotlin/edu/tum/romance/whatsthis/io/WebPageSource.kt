package edu.tum.romance.whatsthis.io

import edu.tum.romance.whatsthis.math.IntVec
import org.apache.pdfbox.Loader
import org.jsoup.Jsoup
import java.net.URL

@Suppress("unused")
object WebPageSource : DataSource<URL> {
    override fun textToVec(resource: URL): IntVec {
        return StringSource.textToVec(load(resource))
    }

    override fun load(resource: URL): String {
        val connection = resource.openConnection()
        return when (connection.contentType.split(";")[0]) {
            "text/html" -> Jsoup.parse(resource.readText()).body().text()
            "text/plain" -> resource.readText()
            "application/pdf" -> PdfFileSource.load(Loader.loadPDF(resource.openStream()))
            else -> error("Unsupported Mimetype")
        }
    }
}