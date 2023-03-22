package edu.tum.romance.whatsthis.v1.math.vec

/**
 * This interface represents a vector with the necessary
 * operations to perform calculations on it.
 */
interface Vector<T: Vector<T>> {
    /**
     * Returns the sum of this vector and the other vector.
     * It should be noted, that the result is a vector of
     * arbitrary type, but it is likelier to be of the same
     * type as the other vector due to calculation/optimization
     * constraints.
     *
     * @param other The other vector.
     *
     * @return The sum of this vector and the other vector.
     */
    operator fun plus(other: Vector<*>): Vector<*>
    /**
     * Returns the difference of this vector and the other vector.
     * It should be noted, that the result is a vector of
     * arbitrary type, but it is likelier to be of the same
     * type as the other vector due to calculation/optimization
     * constraints.
     *
     * @param other The other vector.
     *
     * @return The difference of this vector and the other vector.
     */
    operator fun minus(other: Vector<*>): Vector<*>

    /**
     * Get the value at the given index.
     *
     * @param index The index.
     *
     * @return The value at the given index.
     */
    operator fun get(index: Int): Double
    /**
     * Set the value at the given index.
     *
     * @param index The index.
     * @param value The value.
     */
    operator fun set(index: Int, value: Double)

    /**
     * Set the value at the given index.
     *
     * @param index The index.
     * @param value The value.
     */
    operator fun set(index: Int, value: Int)

    /**
     * Returns the indices one should iterate over to perform
     * operations on all (viable) elements of this vector.
     *
     * @return The indices one should iterate over to perform
     */
    fun indices(): Collection<Int>

    /**
     * Returns the unit vector of this vector.
     *
     * @return The unit vector of this vector.
     */
    fun unit(): T

    /**
     * Grows the vector to the given size.
     *
     * @param size The size to grow to.
     */
    fun growTo(size: Int)

    /**
     * Returns the size of this vector.
     *
     * @return The size of this vector.
     */
    fun size(): Int

    /**
     * Create a deep copy of this vector.
     *
     * @return A copy of this vector.
     */
    fun clone(): T
}