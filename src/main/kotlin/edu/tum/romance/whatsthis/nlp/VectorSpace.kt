package edu.tum.romance.whatsthis.nlp

import edu.tum.romance.whatsthis.math.Distance
import edu.tum.romance.whatsthis.math.Vector

class VectorSpace(val name: String) {
    private val vectors = mutableListOf<Int>()

    // Computed Properties
    private var cache = mutableMapOf<Int, Vector>()
    private var summary: Vector = Vector(0)

    operator fun plusAssign(ref: Int) {
        for(space in VectorSpaceManager.spaces()) {
            val vectorSpace = VectorSpaceManager[space]!!
            if(ref in vectorSpace) {
                vectorSpace -= ref
            }
        }
        vectors += ref
    }

    operator fun minusAssign(ref: Int) {
        vectors -= ref
    }

    operator fun contains(ref: Int): Boolean = ref in vectors

    fun size() = vectors.size
    fun vectors() = vectors.toList()

    fun summary(norm: Distance): Vector {
        val hash = norm.hashCode() + vectors.hashCode()
        if (hash in cache) {
            return cache[hash]!!
        }
        summary = Vector(0)
        if (vectors.isNotEmpty()) {
            for (ref in vectors) {
                val textVector = WordVectorManager[ref]
                if (textVector != null) {
                    if (summary.size() == 0) {
                        summary = textVector.vector!!
                    } else {
                        summary.plusAssign(textVector.vector!!)
                    }
                }
            }
            summary = summary.unit(norm)
        }
        cache[hash] = summary
        return summary
    }
}