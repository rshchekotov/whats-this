package edu.tum.romance.whatsthis.v2.util.ovtree

import edu.tum.romance.whatsthis.v2.util.Some
import edu.tum.romance.whatsthis.v2.util.flatten

class OctovigesimalTree<T : Any>(private val depth: Int, private val mapper: (T) -> String) {
    val root: OctovigesimalElement<T> = OctovigesimalNode(depth)
    val size: Int
        get() = root.size

    init {
        if(depth == 0) error("For `depth = 0` please use a simple list!")
        if(depth < 1) error("Depth must be positive!")
    }

    operator fun plusAssign(data: Some<T>) {
        if(data.quantity != depth) error("Data must have depth $depth!")
        root += computeIndices(data) to data
    }

    operator fun minusAssign(index: IntArray) {
        if(index.size != depth + 1) error("Index must have depth $depth!")
        root -= index
    }

    operator fun get(index: IntArray): Some<T>? {
        if(index.size != depth + 1) error("Index must have depth $depth!")
        return root[index]
    }

    operator fun get(data: Some<T>): IntArray {
        return computeIndices(data)
    }

    operator fun set(index: IntArray, data: Some<T>) {
        if(index.size != depth + 1) error("Index must have depth $depth!")
        root[index] = data
    }

    fun clear() {
        root.clear()
    }

    @Suppress("UNCHECKED_CAST")
    inline fun <reified Y : Any> some() = when(Y::class) {
        String::class -> (root.toList() as List<Some<String>>).flatten()
        Int::class -> (root.toList() as List<Some<Int>>).flatten()
        else -> error("Cannot convert to ${Y::class}!")
    }

    private fun computeIndices(data: Some<T>): IntArray {
        @Suppress("IMPLICIT_NOTHING_TYPE_ARGUMENT_IN_RETURN_POSITION")
        data.ifNone { error("Cannot compute indices for no data!") }
        val vector =  data.ifSingle { intArrayOf(computeIndex(it), mapper(it).hashCode()) } ?:
            data.ifMany { items ->
                val initial = items.map { computeIndex(it) }.toIntArray()
                initial + items.joinToString(" ", transform = mapper).hashCode()
            }
        return vector ?: error("Should never happen!")
    }

    private fun computeIndex(data: T): Int {
        val text = mapper(data)
        if(text.isEmpty()) error("Cannot compute index for no data!")
        val char = text[0]
        if(char in 'a'..'z') return char - 'a'
        if(char in 'A'..'Z') return 26
        return 27
    }
}