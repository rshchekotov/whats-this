package edu.tum.romance.whatsthis.nlp

import edu.tum.romance.whatsthis.math.Vector

object VectorSpaceManager {
    private val spaces = mutableListOf<Pair<VectorSpace, Vector>>()
    private val reverse = mutableMapOf<String, Int>()
    private val unclassified = mutableListOf<Int>()
    private var dirty = false

    operator fun plusAssign(space: VectorSpace) {
        reverse[space.name] = spaces.size
        spaces += (space to Vector(0))
        dirty = true
        for((it, _) in spaces) {
            it.flagDirty()
        }
    }

    operator fun plusAssign(name: String) {
        this += VectorSpace(name)
    }

    operator fun minusAssign(space: VectorSpace) {
        this -= space.name
    }

    operator fun minusAssign(name: String) {
        val index = reverse[name] ?: return
        spaces -= spaces[index]
        reverse.remove(name)
        dirty = true
    }

    operator fun get(name: String): VectorSpace? {
        val index = reverse[name] ?: return null
        return spaces[index].first
    }

    operator fun contains(name: String): Boolean {
        return name in reverse
    }

    operator fun set(name: String, space: VectorSpace) {
        val index = reverse[name] ?: return
        spaces[index] = (space to Vector(0))
        for((it, _) in spaces) {
            it.flagDirty()
        }
    }

    operator fun set(name: String, significance: Vector) {
        val index = reverse[name] ?: return
        spaces[index] = spaces[index].copy(second = significance)
        dirty = true
    }

    fun unclassified(): List<Int> = unclassified.toList()

    fun unclassified(ref: Int) {
        unclassified.add(ref)
        var found = false
        for((space, _) in spaces) {
            if(ref in space) {
                space -= ref
                found = true
            }
        }
        if(found) {
            for((space, _) in spaces) {
                space.flagDirty()
            }
        }
    }

    fun spaces(): List<String> {
        return reverse.map { it.key }.sorted()
    }

    fun significance(name: String): Vector? {
        val index = reverse[name] ?: return null
        return spaces[index].second
    }
    fun clear() {
        spaces.clear()
        reverse.clear()
        unclassified.clear()
    }

    fun isEmpty() = spaces.isEmpty()
}

