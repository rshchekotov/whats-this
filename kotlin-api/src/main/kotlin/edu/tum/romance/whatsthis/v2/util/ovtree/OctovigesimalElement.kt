package edu.tum.romance.whatsthis.v2.util.ovtree

import edu.tum.romance.whatsthis.v2.util.Some

abstract class OctovigesimalElement<T : Any>(val depth: Int) {
    abstract val size: Int
    abstract operator fun plusAssign(data: Pair<IntArray, Some<T>>)
    abstract operator fun minusAssign(index: IntArray)
    abstract operator fun get(index: IntArray): Some<T>?
    abstract operator fun set(index: IntArray, value: Some<T>)
    abstract fun clear()
    abstract fun toList(): List<Some<T>>
}