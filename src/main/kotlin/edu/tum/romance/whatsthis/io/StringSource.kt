package edu.tum.romance.whatsthis.io

import edu.tum.romance.whatsthis.math.IntVec
import edu.tum.romance.whatsthis.nlp.WordVec
import edu.tum.romance.whatsthis.util.WordCount
import java.util.*

object StringSource: TextToVec<String> {
    private val delimiter = Regex("\\s+")
    override fun textToVec(resource: String): IntVec {
        val tokens = resource.split(delimiter)
            .map { it.lowercase(Locale.getDefault()) }
        val wordMap: List<WordCount> = tokens.groupingBy { it }
            .eachCount().toList()

        WordVec + wordMap
        return WordVec.createVec(wordMap)
    }
}