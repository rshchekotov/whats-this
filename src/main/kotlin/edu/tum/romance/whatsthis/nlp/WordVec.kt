package edu.tum.romance.whatsthis.nlp

import edu.tum.romance.whatsthis.io.TextData
import edu.tum.romance.whatsthis.math.Vector
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

    /**
     * Creates a vector from the given TextData and adds the words to the dictionary
     * @param data The TextData to create the vector from
     */
    fun createAndAddVec(data: TextData<*>) {
        for((word, _) in data.tokens) {
            if(!dictionary.contains(word)) {
                dictionary += word
            }
        }
        createVec(data)
    }

    /**
     * Creates a vector from the given TextData
     * @param data The TextData to create the vector from
     */
    fun createVec(data: TextData<*>) {
        val vecList = Array(dictionary.size) { 0 }
        val wordVec = data.tokens.map { it.first }

        for ((index, word) in dictionary.withIndex()) {
            val i = wordVec.indexOf(word)
            if (i != -1) {
                vecList[index] = data.tokens[i].second
            } else {
                vecList[index] = 0
            }
        }

        data.vector = Vector(vecList)
    }
}