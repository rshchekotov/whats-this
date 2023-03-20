package edu.tum.romance.whatsthis.math.vec

interface Vector<T: Vector<T>> {
    operator fun plus(other: Vector<*>): Vector<*>
    operator fun minus(other: Vector<*>): Vector<*>
    operator fun get(index: Int): Double
    operator fun set(index: Int, value: Double)
    operator fun set(index: Int, value: Int)
    fun indices(): Collection<Int>

    fun unit(): T

    fun growTo(size: Int)
    fun size(): Int

    fun clone(): T
}