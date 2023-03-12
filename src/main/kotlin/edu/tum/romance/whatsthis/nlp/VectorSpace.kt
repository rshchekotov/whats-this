package edu.tum.romance.whatsthis.nlp

import edu.tum.romance.whatsthis.math.Distance
import edu.tum.romance.whatsthis.math.Vector

/**
 * A VectorSpace is a collection of vectors that are related to each other.
 * It is used to classify vectors into different groups.
 *
 * (*protected*) YOU SHOULD HAVE NO REASON TO USE THIS DIRECTLY
 *
 * Use the methods provided by [API] instead.
 *
 * The only reason this is not 'formally'
 * protected, is because Kotlin doesn't
 * support that.
 *
 * @property name The name of the vector space
 */
class VectorSpace(var name: String) {
    private val vectors = mutableListOf<Int>()

    // Computed Properties
    private var dirty = false
    private var lastNorm: Distance = Distance.Euclidean
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

    fun summary(norm: Distance, force: Boolean = false): Vector {
        if(!force && !dirty && norm == this.lastNorm) {
            return summary
        }
        dirty = false
        lastNorm = norm
        summary = Vector(0)
        if (vectors.isNotEmpty()) {
            for (ref in vectors) {
                val textVector = API.vectors[ref]
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

    fun decreaseRefsHigherThan(ref: Int) {
        if(ref in vectors) {
            vectors -= ref
        }
        for(i in 0 until vectors.size) {
            if(vectors[i] > ref) {
                vectors[i]--
            }
        }
    }

    fun flagDirty() {
        dirty = true
    }
}