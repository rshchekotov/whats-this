package edu.tum.romance.whatsthis.math

import edu.tum.romance.whatsthis.math.vec.Vector
import java.math.BigDecimal
import java.math.MathContext
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

interface Distance {
    operator fun invoke(a: Vector<*>, b: Vector<*>): Double {
        return Implementation(a, b)
    }

    operator fun invoke(a: Vector<*>): Double {
        return Implementation(a)
    }

    @Suppress("unused")
    fun swapImplementation() {
        Implementation = when (Implementation) {
            EuclideanDistance -> ManhattanDistance
            else -> EuclideanDistance
        }
    }

    companion object {
        internal var Implementation: Distance = EuclideanDistance
    }

    /**
     * Euclidean distance
     */
    object EuclideanDistance: Distance {
        override fun invoke(a: Vector<*>, b: Vector<*>): Double {
            var sum = 0.0
            val diff = a - b
            for(i in diff.indices()) {
                sum += diff[i].pow(2.0)
            }
            return sqrt(sum)
        }

        override fun invoke(a: Vector<*>): Double {
            var sum: BigDecimal = BigDecimal.ZERO
            for(i in a.indices()) {
                sum += BigDecimal(a[i]).pow(2)
            }
            return sum.sqrt(MathContext.DECIMAL128).toDouble()
        }
    }

    /**
     * Manhattan distance
     */
    object ManhattanDistance: Distance {
        override fun invoke(a: Vector<*>, b: Vector<*>): Double {
            var sum = 0.0
            val diff = a - b
            for(i in diff.indices()) {
                sum += abs(diff[i])
            }
            return sum
        }

        override fun invoke(a: Vector<*>): Double {
            var sum = 0.0
            for(i in a.indices()) {
                sum += abs(a[i])
            }
            return sum
        }
    }
}