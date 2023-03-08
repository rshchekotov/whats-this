package edu.tum.romance.whatsthis.math

import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

interface Distance {
    operator fun invoke(a: Vector, b: Vector): Double
    operator fun invoke(a: Vector): Double
}

object EuclideanDistance: Distance {
    override fun invoke(a: Vector, b: Vector): Double {
        var sum = 0.0
        for(i in a.data.indices) {
            sum += (a.data[i] - b.data[i]).pow(2.0)
        }
        return sqrt(sum)
    }

    override fun invoke(a: Vector): Double {
        var sum = 0.0
        for(i in a.data.indices) {
            sum += a.data[i].pow(2.0)
        }
        return sqrt(sum)
    }
}

@Suppress("unused")
object ManhattanDistance: Distance {
    override fun invoke(a: Vector, b: Vector): Double {
        var sum = 0.0
        for(i in a.data.indices) {
            sum += abs(a.data[i] - b.data[i])
        }
        return sum
    }

    override fun invoke(a: Vector): Double {
        var sum = 0.0
        for(i in a.data.indices) {
            sum += abs(a.data[i])
        }
        return sum
    }
}