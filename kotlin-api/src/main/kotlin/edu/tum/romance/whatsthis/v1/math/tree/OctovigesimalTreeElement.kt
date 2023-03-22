package edu.tum.romance.whatsthis.v1.math.tree

internal abstract class OctovigesimalTreeElement<T>(val depth: Int, val mapper: (T) -> String) {
    abstract fun hasChildren(): Boolean
    abstract fun getChildren(): MutableList<OctovigesimalTreeElement<T>>
    abstract fun getChild(element: T): OctovigesimalTreeElement<T>
    abstract fun getData(): MutableList<Array<T>>
    abstract fun size(): Int
    abstract operator fun plusAssign(data: Array<T>)
    abstract operator fun contains(data: Array<T>): Boolean
    abstract fun indexOf(data: Array<out T>): Array<Int>
    abstract fun clear()
}