package edu.tum.romance.whatsthis.v2.util.ovtree

import edu.tum.romance.whatsthis.v2.util.Some
import java.util.TreeMap

class OctovigesimalLeaf<T : Any>(depth: Int): OctovigesimalElement<T>(depth) {
    private val tree: TreeMap<Int, Some<T>> = TreeMap()
    override val size: Int
        get() = tree.size
    override fun plusAssign(data: Pair<IntArray, Some<T>>) {
        val (indices, value) = data
        tree[indices[indices.size - 1]] = value
    }

    override fun minusAssign(index: IntArray) {
        tree -= index[index.size - 1]
    }

    override fun get(index: IntArray): Some<T>? {
        return tree[index[index.size - 1]]
    }

    override fun toList(): List<Some<T>> {
        return tree.values.toList()
    }

    override fun set(index: IntArray, value: Some<T>) {
        tree[index[index.size - 1]] = value
    }

    override fun clear() {
        tree.clear()
    }
}