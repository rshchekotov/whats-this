package edu.tum.romance.whatsthis.math

import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

interface Distance {
    operator fun invoke(a: IntVec, b: IntVec): Double
}

object EuclideanDistance: Distance {
    override fun invoke(a: IntVec, b: IntVec): Double {
        var sum = 0.0
        for(i in a.data.indices) {
            sum += (a.data[i] - b.data[i]).toDouble().pow(2.0)
        }
        return sqrt(sum)
    }
}

@Suppress("unused")
object ManhattanDistance: Distance {
    override fun invoke(a: IntVec, b: IntVec): Double {
        var sum = 0.0
        for(i in a.data.indices) {
            sum += abs(a.data[i] - b.data[i])
        }
        return sum
    }
}