package edu.tum.romance.whatsthis.math

import edu.tum.romance.whatsthis.nlp.Monitor

class IntVec(x: List<Int>){
    var data: List<Int> = x
    val size: Int
        get() = data.size

    fun update() {
        val vecList = ArrayList<Int>(Monitor.dictVec.dictionary.size)
        vecList.addAll(data)
        for(i in vecList.size until Monitor.dictVec.dictionary.size) {
            vecList.add(0)
        }
        data = vecList
    }
}