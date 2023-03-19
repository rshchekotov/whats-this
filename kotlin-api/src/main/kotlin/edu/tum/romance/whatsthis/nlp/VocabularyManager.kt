package edu.tum.romance.whatsthis.nlp

import edu.tum.romance.whatsthis.math.Vector

/**
 * This class is used to manage the vocabulary of the system.
 */
@Suppress("EqualsOrHashCode")
object VocabularyManager {
    private val vocabulary = mutableListOf<String>()
    private val reverse = mutableMapOf<Int, Int>()

    operator fun plusAssign(words: List<String>) {
        for (word in words) {
            this += word
        }
    }

    operator fun get(word: String): Int {
        return reverse[word.hashCode()] ?: -1
    }

    operator fun contains(word: String): Boolean {
        return word.hashCode() in reverse
    }

    operator fun plusAssign(word: String) {
        val hash = word.hashCode()
        if (reverse[hash] == null) {
            reverse[hash] = vocabulary.size
            vocabulary += word
        }
    }

    fun emptyVector() = Vector(size())

    fun size(): Int {
        return vocabulary.size
    }
    fun words(): List<String> {
        return vocabulary.toList()
    }

    override fun hashCode(): Int {
        return vocabulary.hashCode()
    }

    fun clear() {
        vocabulary.clear()
        reverse.clear()
    }

    fun isEmpty() = vocabulary.isEmpty()
}