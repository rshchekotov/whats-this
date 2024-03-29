package edu.tum.romance.whatsthis.v1.nlp

import edu.tum.romance.whatsthis.v1.math.Distance
import edu.tum.romance.whatsthis.v1.math.vec.SparseVector
import edu.tum.romance.whatsthis.v1.math.vec.Vector

/**
 * A VectorSpace is a collection of vectors that are related to each other.
 * It is used to classify vectors into different groups.
 *
 * @property name The name of the vector space
 */
internal class VectorSpace(var name: String) {
    private val vectors = mutableListOf<Int>()

    // Computed Properties
    private var dirty = false
    private var lastNorm: Distance = Distance.Implementation
    private var summary: Vector<*> = SparseVector(0)

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

    fun summary(norm: Distance, force: Boolean = false): Vector<*> {
        if(!force && !dirty && norm == this.lastNorm) {
            return summary
        }
        dirty = false
        lastNorm = norm
        summary = SparseVector(0)
        if (vectors.isNotEmpty()) {
            for (ref in vectors) {
                val textVector = API.vectors[ref]
                if (textVector != null) {
                    summary = if (summary.size() == 0) {
                        textVector.vector!!.clone()
                    } else {
                        summary.growTo(textVector.vector!!.size())
                        summary + textVector.vector!!
                    }
                }
            }
            summary = summary.unit()
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