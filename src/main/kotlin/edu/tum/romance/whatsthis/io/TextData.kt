@file:Suppress("SpellCheckingInspection")

package edu.tum.romance.whatsthis.io

import edu.tum.romance.whatsthis.util.WordCount
import org.apache.pdfbox.Loader
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper
import org.jsoup.Jsoup
import java.io.File
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

class FileSource(override val source: File): TextData<File>() {
    override var text: String = text()

    private fun text(): String {
        return when (source.extension) {
            "html" -> Jsoup.parse(source.readText()).body().text()
            "pdf" -> getPDF(Loader.loadPDF(source))
            else -> source.readText()
        }
    }
}

class InMemorySourceData(override val source: String): TextData<String>() {
    override var text: String = source
}

abstract class TextData<T> {
    abstract val source: T
    abstract var text: String
    var coefficient: Double = 1.0
    val vector: List<WordCount>
        get() = vector()

    private fun vector(): List<WordCount> {
        var string = text
        if (string.isBlank()) return emptyList()

        var mathIndex = mathRegex.find(string)?.range?.first ?: -1
        while(mathIndex != -1) {
            val start = mathIndex
            var char = string[mathIndex + 15]
            var depth = 1
            while (depth > 0) {
                when (char) {
                    '{' -> depth++
                    '}' -> depth--
                }
                char = string[++mathIndex + 15]
            }
            val end = mathIndex + 15
            string = string.replaceRange(start, end, " \u0000math\u0000 ")
            mathIndex = mathRegex.find(string, start)?.range?.first ?: -1
        }

        var tokens = string.split(tokenSplitRegex)

        // Transform all tokens to lowercase
        tokens = tokens.filter(String::isNotBlank).map {
            var token = it.lowercase()
            // Special cases
            when {
                token.matches(mailRegex) -> return@map "EMAIL"
                token.matches(urlRegex) -> return@map "URL"
                token.matches(isbnRegex) -> return@map "ISBN"
                token.matches(dateRegex) -> return@map "DATE"
                token == "\u0000math\u0000" -> return@map "MATH"
                !token[0].isUpperCase() -> {
                    // Remove punctuation
                    token = token.replace(specialCharRegex, "")
                }
            }

            if(token.isNotBlank() && token.length <= characterSizeFilter) return@map "CHAR"

            if (token.matches(yearRegex)) return@map "YEAR"
            if (token.matches(numberRegex)) return@map "NUMBER"

            token
        }.filter(String::isNotBlank)

        if (ngram > 1u) {
            // Create ngrams
            val ngrams = mutableListOf<String>()
            for (i in 0..(tokens.size - ngram.toInt())) {
                ngrams.add(tokens.subList(i, i + ngram.toInt()).joinToString(" "))
            }
            tokens = ngrams
        }

        // Group tokens by their value and count their occurrences
        return tokens.groupingBy { it }.eachCount().toList()
    }

    companion object {
        const val ngram: UInt = 1u

        var characterSizeFilter = 1

        val tokenSplitRegex = Regex("[\\s/]+")

        val specialCharRegex = Regex("[.,!?:;\"'\\[\\](){}#@$%^&*+=<>`~]")
        val yearRegex = Regex("1\\d{3}|2[0-2]\\d{2}")
        val numberRegex = Regex("\\d+")
        val mathRegex = Regex("\\{\\s*\\\\displaystyle")
        val isbnRegex = Regex("([\\dX]{13}|[\\d\\-X]{17}|[\\dX]{10}|[\\d\\-X]{13})").with(specialCharRegex.any())
        val dateRegex = Regex("\\d{4}-\\d{2}-\\d{2}").with(specialCharRegex.any())
        val mailRegex = Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}").with(specialCharRegex.any())
        val urlRegex = Regex("https?://(www\\.)?[-a-zA-Z0-9@:%._+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_+.~#?&/=]*)").with(specialCharRegex.any())

        operator fun invoke(file: File): TextData<File> {
            return FileSource(file)
        }
        operator fun invoke(url: URL): TextData<URL> {
            return WebSourceData(url)
        }
        operator fun invoke(string: String): TextData<String> {
            return InMemorySourceData(string)
        }

        fun getPDF(pdDocument: PDDocument): String {
            val stripper = PDFTextStripper()
            stripper.sortByPosition = true
            stripper.startPage = 1
            stripper.endPage = pdDocument.numberOfPages
            return stripper.getText(pdDocument)
        }
    }
}

fun Regex.with(other: Regex) = Regex(this.pattern + other.pattern)
fun Regex.any() = Regex("(?:${this.pattern})*")