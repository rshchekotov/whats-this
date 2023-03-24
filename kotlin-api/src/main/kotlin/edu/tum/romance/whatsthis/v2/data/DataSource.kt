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
        val title = pdf.documentInformation.title
        pdf.close()
        return text to title
    }

    /**
     * Write the [data] to the [path] in the cache.
     * For Windows, the path is '%AppData%/.whatsthis/cache/sources/[path]'
     * For Linux, the path is '~/.whatsthis/cache/sources/[path]'
     * For Mac, the path is '~/Library/Application Support/.whatsthis/cache/sources/[path]'
     *
     * @param path The path to write the data to
     * @param data The data to write
     */
    fun writeCache(path: String, data: String) {
        val file = cacheBasePath().resolve(path)
        file.parentFile.mkdirs()
        file.writeText(data)
        cached = path
    }

    /**
     * Read the data from the [cached] in the cache.
     * For Windows, the path is '%AppData%/.whatsthis/cache/sources/[cached]'
     * For Linux, the path is '~/.whatsthis/cache/sources/[cached]'
     * For Mac, the path is '~/Library/Application Support/.whatsthis/cache/sources/[cached]'
     *
     * @return The data read
     */
    fun readCache(): String {
        if(cached == null) return ""
        return cacheBasePath().resolve(cached!!).readText()
    }

    /**
     * Check if the data is cached.
     *
     * @return True if the data is cached, false otherwise
     */
    fun isCached(): Boolean {
        return cached != null
    }

    companion object {
        private fun cacheBasePath(): File {
            val directory = if (System.getProperty("os.name").startsWith("Windows")) {
                val base = buildString {
                    append(System.getenv("AppData")).append(File.separatorChar)
                    append(".whatsthis").append(File.separatorChar)
                    append("cache").append(File.separatorChar)
                    append("sources")
                }
                File(base)
            } else if (System.getProperty("os.name").startsWith("Mac")) {
                TODO("If this crashed, that's great! It means the condition is right. Please message the Dev!")
            } else {
                val base = System.getProperty("user.home") + "/.whatsthis/cache/sources"
                File(base)
            }
            if(!directory.exists()) directory.mkdirs()
            return directory
        }

        fun clearCache() {
            cacheBasePath().listFiles()!!.forEach { it.deleteRecursively() }
        }
    }
}