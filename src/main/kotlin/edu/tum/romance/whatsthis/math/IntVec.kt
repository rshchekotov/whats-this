package edu.tum.romance.whatsthis.math

import edu.tum.romance.whatsthis.nlp.WordVec.dictionary

class IntVec(x: List<Int>){
    var data: List<Int> = x
    val size: Int
        get() = data.size

    fun update() {
        val vecList = ArrayList<Int>(dictionary.size)
        vecList.addAll(data)
        for(i in vecList.size until dictionary.size) {
            vecList.add(0)
        }
        data = vecList
    }
}