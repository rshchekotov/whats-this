package edu.tum.romance.whatsthis.nlp

import edu.tum.romance.whatsthis.math.Distance
import edu.tum.romance.whatsthis.math.EuclideanDistance
import edu.tum.romance.whatsthis.math.Vector

class VectorSpace(val name: String) {
    private val vectors = mutableListOf<Int>()

    // Computed Properties
    private var dirty = false
    private var lastNorm: Distance = EuclideanDistance
    private var summary: Vector = Vector(0)

    operator fun plusAssign(ref: Int) {
        if(ref in VectorSpaceManager.unclassified()) {
            VectorSpaceManager.classified(ref)
        } else {
            for(space in VectorSpaceManager.spaces()) {
                val vectorSpace = VectorSpaceManager[space]!!
                if(ref in vectorSpace) {
                    vectorSpace -= ref
                }
                vectorSpace.flagDirty()
            }
        }
        vectors += ref
    }

    operator fun minusAssign(ref: Int) {
        dirty = true
        vectors -= ref
    }

    operator fun contains(ref: Int): Boolean = ref in vectors

    fun size() = vectors.size
    fun vectors() = vectors.toList()

    fun summary(norm: Distance): Vector {
        if(!dirty && norm == this.lastNorm) {
            return summary
        }
        dirty = false
        lastNorm = norm
        summary = Vector(0)
        if (vectors.isNotEmpty()) {
            for (ref in vectors) {
                val textVector = WordVectorManager[ref]
                if (textVector != null) {
                    if (summary.size() == 0) {
                        summary = textVector.vector!!.clone()
                    } else {
                        summary.plusAssign(textVector.vector!!)
                    }
                }
            }
            summary = summary.unit(norm)
        }
        return summary
    }

    fun flagDirty() {
        dirty = true
    }
}