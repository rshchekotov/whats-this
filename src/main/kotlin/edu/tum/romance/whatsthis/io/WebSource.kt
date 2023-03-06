package edu.tum.romance.whatsthis.io

import edu.tum.romance.whatsthis.math.IntVec
import org.jsoup.Jsoup
import java.net.URL

@Suppress("unused")
object WebSource : DataSource<URL> {
    override fun textToVec(resource: URL): IntVec {
        return StringSource.textToVec(load(resource))
    }

    override fun load(resource: URL): String {
        val connection = resource.openConnection()
        return when (connection.contentType.split(";")[0]) {
            "text/html" -> Jsoup.parse(resource.readText()).body().text()
            "text/plain" -> resource.readText()
            else -> error("Unsupported Mimetype")
        }
    }
}