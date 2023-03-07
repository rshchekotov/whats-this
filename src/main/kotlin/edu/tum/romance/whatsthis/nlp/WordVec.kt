package edu.tum.romance.whatsthis.nlp

import edu.tum.romance.whatsthis.io.TextData
import edu.tum.romance.whatsthis.math.IntVec
import edu.tum.romance.whatsthis.util.WordCount

@Suppress("unused")
class WordVec {
    var dictionary: List<String> = listOf()

    fun clear() {
        dictionary = listOf()
    }

    operator fun plus(newDictionary: List<WordCount>): WordVec {
        for(word in newDictionary) {
            if(!dictionary.contains(word.first)) {
                dictionary += word.first
            }
        }
        return this
    }

    fun createAndAddVec(data: TextData<*>) {
        for((word, _) in data.tokens) {
            if(!dictionary.contains(word)) {
                dictionary += word
            }
        }
        createVec(data)
    }

    fun createVec(data: TextData<*>) {
        val vecList = ArrayList<Int>(dictionary.size)
        val wordVec = data.tokens.map { it.first }

        for ((index, word) in dictionary.withIndex()) {
            val i = wordVec.indexOf(word)
            if (i != -1) {
                vecList.add(index, data.tokens[i].second)
            } else {
                vecList.add(index, 0)
            }
        }

        data.vector = IntVec(vecList)
    }
}