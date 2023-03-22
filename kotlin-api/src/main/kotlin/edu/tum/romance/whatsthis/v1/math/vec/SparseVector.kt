package edu.tum.romance.whatsthis.v1.math.vec

import edu.tum.romance.whatsthis.v1.math.Distance
import kotlin.math.abs

/**
 * A sparse vector implementation.
 *
 * This is NOT meant to be safe, but fast.
 *
 * ALL out-of-bounds checks are to be handled
 * by the end-user.
 */
class SparseVector: Vector<SparseVector> {
    var data: MutableMap<Int, Double> = mutableMapOf()
    var size = 0

    constructor(data: MutableMap<Int, Double>, size: Int) {
        this.data = data
        this.size = size
    }

    constructor(data: Map<Int, Double>) {
        if(data.isEmpty()) {
            this.data = mutableMapOf()
            this.size = 0
        } else {
            this.data = data.toMutableMap()
            this.size = data.maxOf { it.key } + 1
        }
    }

    constructor(size: Int) {
        this.size = size
    }

    override fun plus(other: Vector<*>): Vector<*> {
        val result = other.clone()
        for((key, value) in this.data) {
            result[key] = result[key] + value
        }
        return result
    }

    override fun minus(other: Vector<*>): Vector<*> {
        val result = other.clone()
        for((key, value) in this.data) {
            result[key] = value - result[key]
        }
        return result
    }

    override fun get(index: Int): Double {
        return data.getOrDefault(index, 0.0)
    }

    override fun set(index: Int, value: Double) {
        if(value == 0.0) {
            data.remove(index)
        } else {
            data[index] = value
        }
    }

    override fun set(index: Int, value: Int) {
        this[index] = value.toDouble()
    }

    override fun indices(): Collection<Int> {
        return data.keys
    }

    override fun unit(): SparseVector {
        val vector = this.clone()
        val length = Distance.Implementation(vector)
        for((key, value) in vector.data) {
            vector.data[key] = value / length
        }
        return vector
    }

    override fun growTo(size: Int) {
        this.size = size
    }

    override fun size(): Int = size

    override fun clone(): SparseVector = SparseVector(data.toMutableMap(), size)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Vector<*>) return false

        if (size != other.size()) return false
        for (i in 0 until size) {
            if (abs(this[i] - other[i]) > 1.0e-13) return false
        }
        return true
    }

    override fun hashCode(): Int {
        var result = data.hashCode()
        result = 31 * result + size
        return result
    }
}