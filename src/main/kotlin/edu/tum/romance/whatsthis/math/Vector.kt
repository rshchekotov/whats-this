package edu.tum.romance.whatsthis.math

class Vector(x: List<Double>){
    var data: MutableList<Double> = x.toMutableList()

    constructor(ints: Array<Int>) : this(ints.map { it.toDouble() })
    constructor(size: Int) : this(List(size) { 0.0 })

    val size: Int
        get() = data.size

    operator fun plusAssign(other: Vector) {
        data = data.zip(other.data) { a, b -> a + b }.toMutableList()
    }
    operator fun get(index: Int): Double {
        return data[index]
    }

    operator fun set(index: Int, value: Double) {
        data[index] = value
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

    fun unit(norm: Distance) {
        val length = norm(this)
        data = data.map { (it / length) }.toMutableList()
    }
}