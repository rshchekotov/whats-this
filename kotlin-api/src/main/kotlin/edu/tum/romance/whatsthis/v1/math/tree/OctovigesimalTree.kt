package edu.tum.romance.whatsthis.v1.math.tree

/**
 * An implementation of an octovigesimal tree.
 * The first 26 children are for words starting with
 * a latin letter, the 27th is for special tokens and
 * the 28th is for the remainder.
 *
 * At the leaves, the data is stored in a priority queue
 * sorted by the string representation of the data.
 */
class OctovigesimalTree<T>(
    private val depth: Int,
    mapper: (T) -> String = { it.toString() }
) {
    private val root = OctovigesimalTreeNode(depth - 1, mapper)

    fun getData() = root.getData()

    fun size() = root.size()

    operator fun plusAssign(data: Array<T>) {
        ensureDepth(data)
        root += data
    }

    operator fun contains(data: Array<T>): Boolean {
        ensureDepth(data)
        return root.contains(data)
    }

    fun indexOf(data: Array<out T>): Array<Int> {
        ensureDepth(data)
        return root.indexOf(data)
    }

    private fun ensureDepth(data: Array<out T>) {
        if(data.size != depth) error("Data size does not match tree depth")
    }
    fun clear() = root.clear()
}