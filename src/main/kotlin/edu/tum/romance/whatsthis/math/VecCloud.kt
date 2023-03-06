@file:Suppress("unused")

package edu.tum.romance.whatsthis.math

class VecCloud(c: List<IntVec>){
    var cloud: List<IntVec> = c

    fun closestDistance(to: IntVec, f: Distance = EuclideanDistance): Double {
        var min = Double.MAX_VALUE
        for(i in cloud.indices) {
            val dist = f.distance(cloud[i], to)
            if(dist < min) {
                min = dist
            }
        }
        return min
    }

    fun add(v: IntVec) {
        cloud += v
    }

    fun clear() {
        cloud = listOf()
    }
}