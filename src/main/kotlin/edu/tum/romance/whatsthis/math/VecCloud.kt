@file:Suppress("unused")

package edu.tum.romance.whatsthis.math

import edu.tum.romance.whatsthis.io.TextData

class VecCloud(c: List<TextData<*>>){
    var cloud: List<TextData<*>> = c

    constructor() : this(listOf())

    fun closestDistance(to: TextData<*>, f: Distance = EuclideanDistance): Double {
        var min = Double.MAX_VALUE
        for(i in cloud.indices) {
            if(cloud[i].vector == null || to.vector == null) continue
            val dist = f.distance(cloud[i].vector!!, to.vector!!)
            if(dist < min) {
                min = dist
            }
        }
        return min
    }

    operator fun plus(v: TextData<*>): VecCloud {
        cloud += v
        return this
    }

    operator fun minus(v: TextData<*>): VecCloud {
        cloud -= v
        return this
    }

    fun clear() {
        cloud = listOf()
    }
}