package edu.tum.romance.whatsthis.v2.data

import org.apache.pdfbox.Loader
import org.apache.pdfbox.text.PDFTextStripper
import org.jsoup.Jsoup
import java.io.File
import java.io.InputStream

abstract class DataSource<T>(var source: T) {
    var name: String? = null
    private var cached: String? = null

    /**
     * Load a pair of (text, title) from the source.
     */
    abstract fun load(): Pair<String, String>

    fun parseHTML(data: String): Pair<String, String?> {
        val parsed = Jsoup.parse(data)
        return Pair(parsed.body().text(), parsed.title().ifEmpty { null })
    }

    fun parsePDF(stream: InputStream): Pair<String, String?> {
        val pdf = Loader.loadPDF(stream)
        val stripper = PDFTextStripper()
        stripper.sortByPosition = true
        stripper.startPage = 1
        stripper.endPage = pdf.numberOfPages
        val text = stripper.getText(pdf)
        pdf.close()
        return text to pdf.documentInformation.title
    }

    /**
     * Write the [data] to the [path] in the cache.
     * For Windows, the path is '%AppData%/.whatsthis/[path]'
     * For Linux, the path is '~/.whatsthis/[path]'
     * For Mac, the path is '~/Library/Application Support/.whatsthis/[path]'
     *
     * @param path The path to write the data to
     * @param data The data to write
     */
    fun writeCache(path: String, data: String) {
        if(System.getProperty("os.name").startsWith("Windows")) {
            val base = System.getenv("AppData") + File.pathSeparator + ".whatsthis"
            File(base + File.pathSeparator + path)
        } else if(System.getProperty("os.name").startsWith("Mac")) {
            TODO("If this crashed, that's great! It means the condition is right. Please message the Dev!")
        } else {
            val base = System.getProperty("user.home") + "/.whatsthis"
            File(base + File.pathSeparator + path)
        }.writeText(data)
        cached = path
    }

    /**
     * Read the data from the [cached] in the cache.
     * For Windows, the path is '%AppData%/.whatsthis/[cached]'
     * For Linux, the path is '~/.whatsthis/[cached]'
     * For Mac, the path is '~/Library/Application Support/.whatsthis/[cached]'
     *
     * @return The data read
     */
    fun readCache(): String {
        return (if (System.getProperty("os.name").startsWith("Windows")) {
            val base = System.getenv("AppData") + File.pathSeparator + ".whatsthis"
            File(base + File.pathSeparator + cached)
        } else if (System.getProperty("os.name").startsWith("Mac")) {
            TODO("If this crashed, that's great! It means the condition is right. Please message the Dev!")
        } else {
            val base = System.getProperty("user.home") + "/.whatsthis"
            File(base + File.pathSeparator + cached)
        }).readText()
    }

    /**
     * Check if the data is cached.
     *
     * @return True if the data is cached, false otherwise
     */
    fun isCached(): Boolean {
        return cached != null
    }
}