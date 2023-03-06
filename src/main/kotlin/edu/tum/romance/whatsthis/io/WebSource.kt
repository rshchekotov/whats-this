package edu.tum.romance.whatsthis.io

import edu.tum.romance.whatsthis.math.IntVec
import org.jsoup.Jsoup
import java.net.URL

@Suppress("unused")
object WebSource : TextToVec<URL> {
    override fun textToVec(resource: URL): IntVec {
        /*
        * Check if server is reachable and if the resource
        * is available and has the Mimetype of an HTML.
        */
        val connection = resource.openConnection()
        connection.connect()

        return when (connection.contentType) {
            "text/html" -> parseHTML(resource.readText())
            "text/plain" -> StringSource.textToVec(resource.readText())
            else -> error("Unsupported Mimetype")
        }
    }

    private fun parseHTML(html: String): IntVec {
        val doc = Jsoup.parse(html)
        return StringSource.textToVec(doc.body().text())
    }
}