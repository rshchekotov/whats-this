package edu.tum.romance.whatsthis.math

class Vector(x: List<Double>): Cloneable {
    var data: MutableList<Double> = x.toMutableList()

    constructor(ints: Array<Int>) : this(ints.map { it.toDouble() })
    constructor(size: Int) : this(List(size) { 0.0 })

    operator fun plusAssign(other: Vector) {
        data = data.zip(other.data) { a, b -> a + b }.toMutableList()
    }
    operator fun plus(other: Vector): Vector {
        val vector = this.clone()
        vector += other
        return vector
    }
    operator fun get(index: Int): Double {
        return data[index]
    }

    operator fun set(index: Int, value: Double) {
        data[index] = value
    }

    operator fun set(index: Int, value: Int) {
        data[index] = value.toDouble()
    }

    fun set(vararg indexedValues: Pair<Int, Double>) {
        for ((index, value) in indexedValues) {
            data[index] = value
        }
    }

    fun growTo(size: Int) {
        while (data.size < size) {
            data.add(0.0)
        }
    }

    fun unit(norm: Distance): Vector {
        val vector = this.clone()
        val length = norm(vector)
        vector.data = vector.data.map { (it / length) }.toMutableList()
        return vector
    }

    fun size(): Int {
        return data.size
    }

    public override fun clone(): Vector {
        return Vector(data)
    }

    override fun equals(other: Any?): Boolean {
        if (other !is Vector) return false
        if(data.size != other.data.size) return false
        var b = true
        for (i in 0 until data.size) {
            b = b && (data[i] == other.data[i])
        }
        return b
    }

    override fun hashCode(): Int {
        var result = data.hashCode()
        result = 31 * result + data.hashCode()
        return result
    }
}