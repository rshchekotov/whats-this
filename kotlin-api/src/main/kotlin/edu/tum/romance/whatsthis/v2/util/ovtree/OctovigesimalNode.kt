package edu.tum.romance.whatsthis.v2.util.ovtree

import edu.tum.romance.whatsthis.v2.util.Some

class OctovigesimalNode<T : Any>(depth: Int): OctovigesimalElement<T>(depth) {
    private val children: Array<OctovigesimalElement<T>> = if(depth == 1) {
        Array(28) { OctovigesimalLeaf(0) }
    } else {
        Array(28) { OctovigesimalNode(depth - 1) }
    }

    override val size: Int
        get() = children.sumOf { it.size }

    override fun plusAssign(data: Pair<IntArray, Some<T>>) {
        val (indices, _) = data
        val height = indices.size - depth - 1
        val index = indices[height]
        children[index] += data
    }

    override fun minusAssign(index: IntArray) {
        val height = index.size - depth - 1
        val i = index[height]
        children[i] -= index
    }

    override fun get(index: IntArray): Some<T>? {
        val height = index.size - depth - 1
        val i = index[height]
        return children[i][index]
    }

    override fun toList(): List<Some<T>> {
        return children.flatMap { it.toList() }
    }

    override fun set(index: IntArray, value: Some<T>) {
        val height = index.size - depth - 1
        val i = index[height]
        children[i][index] = value
    }

    override fun clear() {
        children.forEach { it.clear() }
    }
}