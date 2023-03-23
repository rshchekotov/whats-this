package edu.tum.romance.whatsthis.v2.data

import java.net.URL
import java.util.*

class WebSource(url: String): DataSource<URL>(URL(url)) {
    override fun load(): Pair<String, String> {
        if(isCached()) return loadCached()

        val connection = source.openConnection()
        val defaultTitle = source.path.split("/").last()
        val data = when (connection.contentType.split(";")[0]) {
            "text/html" -> parseHTML(source.readText()).let { it.first to (it.second ?: defaultTitle) }
            "text/plain" -> source.readText() to defaultTitle
            "application/pdf" -> parsePDF(source.openStream()).let { it.first to (it.second ?: defaultTitle) }
            else -> error("Unsupported Mimetype")
        }

        // Generate UUID
        val uuid = UUID.randomUUID().toString()

        // Write to cache at source/web/[random]
        writeCache("source/web/${uuid}.txt", "${data.second}\n${data.first}")

        return data
    }

    private fun loadCached(): Pair<String, String> {
        val data = readCache()
        val lines = data.lines()
        val title = lines.first()
        val text = lines.drop(1).joinToString("\n")
        return text to title
    }
}