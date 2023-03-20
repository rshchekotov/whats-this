package edu.tum.romance.whatsthis.math.tree

internal class OctovigesimalTreeNode<T>(val size: Int, mapper: (T) -> String): OctovigesimalTreeElement<T>(size, mapper) {
    private val children: MutableList<OctovigesimalTreeElement<T>> = MutableList(28) {
        if(size > 0) OctovigesimalTreeNode(size - 1, mapper) else OctovigesimalTreeLeaf(size, mapper)
    }
    override fun hasChildren() = true
    override fun getChildren(): MutableList<OctovigesimalTreeElement<T>> = children.toMutableList()
    override fun getChild(element: T): OctovigesimalTreeElement<T> {
        if(mapper(element).isEmpty()) error("Word is empty")
        val start = mapper(element)[0]
        if(start in 'a'..'z') return children[start - 'a']
        if(start in 'A'..'Z') return children[26]
        return children[27]
    }
    override fun getData() = children.flatMap { it.getData() }.toMutableList()
    override fun size() = children.sumOf { it.size() }
    override fun plusAssign(data: Array<T>) {
        getChild(wordToIndex(data)) += data
    }
    override fun contains(data: Array<T>) = getChild(wordToIndex(data)).contains(data)
    override fun indexOf(data: Array<out T>): Array<Int> {
        val index = getChild(wordToIndex(data)).indexOf(data)
        return if(index.isEmpty()) index else arrayOf(children.indexOf(getChild(wordToIndex(data)))) + index
    }
    override fun clear() = children.forEach { it.clear() }

    private fun wordToIndex(data: Array<out T>): T = data[data.size - size - 1]
}