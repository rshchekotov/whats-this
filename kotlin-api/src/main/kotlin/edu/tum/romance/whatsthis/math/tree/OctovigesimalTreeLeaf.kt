package edu.tum.romance.whatsthis.math.tree

import java.util.Comparator
import java.util.PriorityQueue

internal class OctovigesimalTreeLeaf<T>(val size: Int, mapper: (T) -> String): OctovigesimalTreeElement<T>(size, mapper) {
    private val children: PriorityQueue<Pair<String, Array<T>>> = PriorityQueue(Comparator.comparing(Pair<String, Array<T>>::first))
    override fun hasChildren() = children.isNotEmpty()
    override fun getChildren() = mutableListOf<OctovigesimalTreeElement<T>>()
    override fun getChild(element: T) = error("Leaf has no children")
    override fun getData() = children.map { it.second }.toMutableList()
    override fun plusAssign(data: Array<T>) {
        if(data.isEmpty()) error("Data is too short.")
        children.add(data.joinToString(" ", transform = mapper) to data)
    }
    override fun contains(data: Array<T>) = children.any { it.second.contentEquals(data) }
    override fun indexOf(data: Array<T>): Array<Int> {
        val index = children.indexOfFirst { it.second.contentEquals(data) }
        return if(index == -1) arrayOf() else arrayOf(index)
    }
}