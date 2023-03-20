package edu.tum.romance.whatsthis.math.tree

import java.util.Comparator
import java.util.TreeSet

internal class OctovigesimalTreeLeaf<T>(val size: Int, mapper: (T) -> String): OctovigesimalTreeElement<T>(size, mapper) {
    private val children: TreeSet<Pair<String, Array<T>>> = TreeSet(Comparator.comparing(Pair<String, Array<T>>::first))
    override fun hasChildren() = children.isNotEmpty()
    override fun getChildren() = mutableListOf<OctovigesimalTreeElement<T>>()
    override fun getChild(element: T) = error("Leaf has no children")
    override fun getData() = children.map { it.second }.toMutableList()
    override fun size() = children.size
    override fun plusAssign(data: Array<T>) {
        if(data.isEmpty()) error("Data is too short.")
        val key = data.joinToString(" ", transform = mapper)
        children.find { it.first == key }?.let { children.remove(it) }
        children += Pair(key, data)
    }
    override fun contains(data: Array<T>) = children.any { it.second.contentEquals(data) }
    override fun indexOf(data: Array<out T>): Array<Int> {
        val index = children.indexOfFirst { it.second.contentEquals(data) }
        return if(index == -1) arrayOf() else arrayOf(index)
    }
    override fun clear() = children.clear()
}