package edu.tum.romance.whatsthis.nlp

import edu.tum.romance.whatsthis.io.TextData

object WordVectorManager {
    private val vectors = mutableListOf<TextData<*>>()
    private val reverse = mutableMapOf<String, Int>()
    private var dirty = false

    operator fun plusAssign(vector: TextData<*>) {
        vectors += vector
        reverse[vector.name] = vectors.size - 1
        dirty = true
    }

    operator fun minusAssign(vector: TextData<*>) {
        vectors -= vector
        reverse.remove(vector.name)
        // TODO: Move indices of all vectors after the removed one
        dirty = true
    }

    operator fun get(name: String): TextData<*>? {
        return reverse[name]?.let { vectors[it] }
    }

    operator fun get(index: Int): TextData<*>? {
        return vectors.getOrNull(index)
    }

    /**
     * Replaces the vector with the given name with the given vector.
     * If the name does not exist, the vector is added.
     *
     * @param name The name of the vector to replace / create
     * @param vector The vector to replace the old one with
     */
    operator fun set(name: String, vector: TextData<*>) {
        val index = reverse[name]
        if (index != null) {
            vectors[index] = vector
        } else {
            reverse[name] = vectors.size
            vectors += vector
        }
        dirty = true
    }

    /**
     * Rename the vector with the given name to the given name.
     * If a vector with the new name already exists it is
     * overwritten (deleted and replaced).
     *
     * @param name The name of the vector to rename
     * @param ref The new name of the vector
     */
    operator fun set(name: String, ref: String) {
        val index = reverse[name]
        if (index != null) {
            if(ref in reverse) {
                val replace = reverse[ref]!!
                this -= vectors[replace]
                reverse -= name
                reverse[ref] = index
            }
        }
    }

    operator fun contains(vector: TextData<*>): Boolean {
        return vector.name in this
    }

    operator fun contains(name: String): Boolean {
        return reverse[name] != null
    }

    fun size() = vectors.size

    fun ref(name: String): Int {
        return reverse[name] ?: -1
    }

    fun name(ref: Int): String {
        return vectors[ref].name
    }

    fun names(): List<String> {
        return vectors.map { it.name }.sorted()
    }

    fun clear() {
        vectors.clear()
        reverse.clear()
    }

    fun isEmpty() = vectors.isEmpty()
}