package edu.tum.romance.whatsthis.v1.math.vec

import edu.tum.romance.whatsthis.v1.math.Distance
import kotlin.math.abs

@Suppress("unused")
class DenseVector(x: List<Double>): Vector<DenseVector>, Cloneable {
    var data: MutableList<Double> = x.toMutableList()

    @Deprecated("Use SparseVector instead")
    constructor(ints: Array<Int>) : this(ints.map { it.toDouble() })
    @Deprecated("Use SparseVector instead")
    constructor(size: Int) : this(List(size) { 0.0 })
    override operator fun plus(other: Vector<*>): Vector<*> {
        val vector = this.clone()
        for(i in 0 until vector.data.size) {
            vector.data[i] += other[i]
        }
        return vector
    }
    override fun minus(other: Vector<*>): Vector<*> {
        val vector = this.clone()
        for(i in 0 until vector.data.size) {
            vector.data[i] -= other[i]
        }
        return vector
    }
    override operator fun get(index: Int): Double = data[index]

    override operator fun set(index: Int, value: Double) {
        data[index] = value
    }

    override operator fun set(index: Int, value: Int) {
        data[index] = value.toDouble()
    }

    override fun indices(): Collection<Int> {
        return data.indices.toList()
    }

    fun set(vararg indexedValues: Pair<Int, Double>) {
        for ((index, value) in indexedValues) {
            data[index] = value
        }
    }

    override fun growTo(size: Int) {
        while (data.size < size) {
            data.add(0.0)
        }
    }

    override fun unit(): DenseVector {
        val vector = this.clone()
        val length = Distance.Implementation(vector)
        vector.data = vector.data.map { (it / length) }.toMutableList()
        return vector
    }

    override fun size(): Int = data.size

    override fun clone(): DenseVector = DenseVector(data)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Vector<*>) return false
        if(data.size != other.size()) return false
        for (i in 0 until data.size) {
            if(abs(data[i] - other[i]) > 1.0e-13) return false
        }
        return true
    }

    override fun hashCode(): Int {
        var result = data.hashCode()
        result = 31 * result + data.hashCode()
        return result
    }
}