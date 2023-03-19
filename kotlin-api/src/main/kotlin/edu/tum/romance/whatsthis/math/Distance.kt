package edu.tum.romance.whatsthis.math

import java.math.BigDecimal
import java.math.MathContext
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

interface Distance {
    operator fun invoke(a: Vector, b: Vector): Double {
        return Implementation(a, b)
    }

    operator fun invoke(a: Vector): Double {
        return Implementation(a)
    }

    @Suppress("unused")
    fun swapImplementation() {
        Implementation = when (Implementation) {
            EuclideanDistance -> Manhattan
            else -> EuclideanDistance
        }
    }

    companion object {
        internal var Implementation: Distance = EuclideanDistance
        internal val Euclidean = EuclideanDistance
        internal val Manhattan = ManhattanDistance
    }
}

/**
 * Euclidean distance
 */
internal object EuclideanDistance: Distance {
    override fun invoke(a: Vector, b: Vector): Double {
        var sum = 0.0
        for(i in a.data.indices) {
            sum += (a.data[i] - b.data[i]).pow(2.0)
        }
        return sqrt(sum)
    }

    override fun invoke(a: Vector): Double {
        var sum: BigDecimal = BigDecimal.ZERO
        for(i in a.data.indices) {
            sum += BigDecimal(a.data[i]).pow(2)
        }
        return sum.sqrt(MathContext.DECIMAL128).toDouble()
    }
}

/**
 * Manhattan distance
 */
internal object ManhattanDistance: Distance {
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