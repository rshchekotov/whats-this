package edu.tum.romance.whatsthis.nlp
import edu.tum.romance.whatsthis.math.Distance
import edu.tum.romance.whatsthis.math.EuclideanDistance
import edu.tum.romance.whatsthis.math.ManhattanDistance
import edu.tum.romance.whatsthis.math.Vector

interface Similarity {
    val dist: Distance

    operator fun invoke(a: Vector, cloud: String): Double

    @Suppress("unused")
    companion object {
        val Euclidean = EuclideanSimilarity
        val Manhattan = ManhattanSimilarity
        val ClosestNeighborEuclidean = ClosestNeighborEuclideanSimilarity
        val ClosestNeighborManhattan = ClosestNeighborManhattanSimilarity
    }
}

object EuclideanSimilarity: Similarity {

    override val dist: Distance
        get() = Distance.Euclidean

    override fun invoke(a: Vector, cloud: String): Double {
        val significance = API.spaces.significance(cloud)!!
        return EuclideanDistance(a, significance)
    }
}

object ManhattanSimilarity: Similarity {
    override val dist: Distance
        get() = Distance.Manhattan
    override fun invoke(a: Vector, cloud: String): Double {
        val significance = API.spaces.significance(cloud)!!
        return ManhattanDistance(a, significance)
    }
}

object ClosestNeighborEuclideanSimilarity: Similarity {

    override val dist: Distance
        get() = Distance.Euclidean

    override fun invoke(a: Vector, cloud: String): Double {
        var min = Double.MAX_VALUE
        if(!API.spaces.contains(cloud)) return Double.NaN
        for (vector in API.spaces[cloud]!!.vectors()) {
            val dist = EuclideanDistance(a, API.vectors[vector]!!.vector!!)
            if (dist < min) {
                min = dist
            }
        }
        return min
    }
}

object ClosestNeighborManhattanSimilarity: Similarity {
    override val dist: Distance
        get() = Distance.Manhattan

    override fun invoke(a: Vector, cloud: String): Double {
        var min = Double.MAX_VALUE
        if(!API.spaces.contains(cloud)) return Double.NaN
        for (vector in API.spaces[cloud]!!.vectors()) {
            val dist = EuclideanDistance(a, API.vectors[vector]!!.vector!!)
            if (dist < min) {
                min = dist
            }
        }
        return min
    }
}