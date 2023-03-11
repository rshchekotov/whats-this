@file:Suppress("SpellCheckingInspection")

package edu.tum.romance.whatsthis.io

import edu.tum.romance.whatsthis.math.Vector
import edu.tum.romance.whatsthis.util.*
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper
import java.io.File
import java.net.URL

abstract class TextData<T> {
    abstract val source: T
    abstract var name: String
    abstract var text: String
    abstract var titleSuggestion: String

    var coefficient: Double = 1.0
    var vector: Vector? = null
    val tokens: List<WordCount>
        get() = vector()

    private fun vector(): List<WordCount> {
        var string = text
        if (string.isBlank()) return emptyList()

        val hash = string.hashCode()
        if (hash in cache) return cache[hash]!!

        var mathIndex = mathRegex.find(string)?.range?.first ?: -1
        while (mathIndex != -1) {
            val start = mathIndex
            var end = mathIndex + 15
            var depth = 1
            while (depth > 0) {
                when (string[end]) {
                    '{' -> depth++
                    '}' -> depth--
                }
                end++
            }
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

            if (token.isNotBlank() && token.length <= characterSizeFilter) return@map "CHAR"

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
        val result = tokens.groupingBy { it }.eachCount().toList()
        cache[hash] = result
        return result
    }

    companion object {
        const val ngram: UInt = 1u

        var characterSizeFilter = 1
        private val cache = mutableMapOf<Int, List<WordCount>>()

        operator fun invoke(file: File, name: String): TextData<File> {
            return FileSource(file, name)
        }

        operator fun invoke(url: URL, name: String): TextData<URL> {
            return WebSourceData(url, name)
        }

        operator fun invoke(string: String, name: String): TextData<String> {
            return InMemorySourceData(string, name)
        }

        @Suppress("unused")
        fun wiki(slug: String, name: String): TextData<URL> {
            return WebSourceData(URL("https://en.wikipedia.org/wiki/$slug"), name)
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