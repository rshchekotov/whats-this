package edu.tum.romance.whatsthis.nlp

import edu.tum.romance.whatsthis.math.IntVec
import edu.tum.romance.whatsthis.util.WordCount

@Suppress("unused")
object WordVec {
    var dictionary: List<String> = listOf()

    operator fun plus(newDictionary: List<WordCount>): WordVec {
        for(word in newDictionary) {
            if(!dictionary.contains(word.first)) {
                dictionary += word.first
            }
        }
        return this
    }

    fun clear() {
        dictionary = listOf()
    }

    fun createVec(text: List<WordCount>): IntVec {
        val vecList = ArrayList<Int>(dictionary.size)
        val wordVec = text.map{ it.first }

        for((index, word) in dictionary.withIndex()) {
            val i = wordVec.indexOf(word)
            if(i != -1){
                vecList.add(index, text[i].second)
            }else{
                vecList.add(index, 0)
            }
        }

        return IntVec(vecList)
    }
}