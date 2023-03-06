package edu.tum.romance.whatsthis.math

import kotlin.test.Test
import kotlin.test.assertEquals

internal class WordVecTest {
    @Test
    fun clear() {
        WordVec.dictionary = listOf("a", "b", "c")
        assertEquals(3, WordVec.dictionary.size, "WordVec dictionary should not be empty")
        WordVec.clear()
        assertEquals(0, WordVec.dictionary.size, "WordVec dictionary should be empty")
    }

    @Test
    fun plus() {
        WordVec.clear()
        WordVec + listOf("a" to 1, "b" to 2, "c" to 3)
        assertEquals(3, WordVec.dictionary.size, "WordVec dictionary should contain 3 words")
        assertEquals("a", WordVec.dictionary[0], "WordVec dictionary should contain 'a'")
        assertEquals("b", WordVec.dictionary[1], "WordVec dictionary should contain 'b'")
        assertEquals("c", WordVec.dictionary[2], "WordVec dictionary should contain 'c'")

        WordVec + listOf("a" to 1, "b" to 2, "d" to 1)
        assertEquals(4, WordVec.dictionary.size, "WordVec dictionary should contain 4 words")
        assertEquals("a", WordVec.dictionary[0], "WordVec dictionary should contain 'a'")
        assertEquals("b", WordVec.dictionary[1], "WordVec dictionary should contain 'b'")
        assertEquals("c", WordVec.dictionary[2], "WordVec dictionary should contain 'c'")
        assertEquals("d", WordVec.dictionary[3], "WordVec dictionary should contain 'd'")

        WordVec.clear()
        WordVec + listOf("a" to 0)
        assertEquals(1, WordVec.dictionary.size, "WordVec dictionary should contain 1 word")
        assertEquals("a", WordVec.dictionary[0], "WordVec dictionary should contain 'a'")

    }

    @Test
    fun createVec() {
        WordVec.clear()
        WordVec + listOf("a" to 1, "b" to 2, "c" to 3)
        val vec = WordVec.createVec(listOf("a" to 1, "b" to 2, "c" to 3))
        assertEquals(3, vec.data.size, "WordVec should contain 3 words")
        assertEquals(1, vec.data[0], "WordVec should contain 1 'a'")
        assertEquals(2, vec.data[1], "WordVec should contain 2 'b'")
        assertEquals(3, vec.data[2], "WordVec should contain 3 'c'")

        WordVec + listOf("a" to 1, "b" to 2, "d" to 1)
        val vec2 = WordVec.createVec(listOf("a" to 1, "b" to 2, "c" to 3))
        assertEquals(4, vec2.data.size, "WordVec should contain 4 words")
        assertEquals(1, vec2.data[0], "WordVec should contain 1 'a'")
        assertEquals(2, vec2.data[1], "WordVec should contain 2 'b'")
        assertEquals(3, vec2.data[2], "WordVec should contain 3 'c'")
        assertEquals(0, vec2.data[3], "WordVec should contain 0 'd'")
    }

    @Test
    fun update() {
        WordVec.clear()
        WordVec + listOf("a" to 1, "b" to 2, "c" to 3)
        val vec = WordVec.createVec(listOf("a" to 1, "b" to 2, "c" to 3))
        assertEquals(3, vec.data.size, "WordVec should contain 3 words")
        assertEquals(1, vec.data[0], "WordVec should contain 1 'a'")
        assertEquals(2, vec.data[1], "WordVec should contain 2 'b'")
        assertEquals(3, vec.data[2], "WordVec should contain 3 'c'")

        WordVec.update(vec)
        assertEquals(3, vec.data.size, "WordVec should contain 3 words")
        assertEquals(1, vec.data[0], "WordVec should contain 1 'a'")
        assertEquals(2, vec.data[1], "WordVec should contain 2 'b'")
        assertEquals(3, vec.data[2], "WordVec should contain 3 'c'")

        WordVec + listOf("a" to 1, "b" to 2, "d" to 1)
        WordVec.update(vec)
        assertEquals(4, vec.data.size, "WordVec should contain 4 words")
        assertEquals(1, vec.data[0], "WordVec should contain 1 'a'")
        assertEquals(2, vec.data[1], "WordVec should contain 2 'b'")
        assertEquals(3, vec.data[2], "WordVec should contain 3 'c'")
        assertEquals(0, vec.data[3], "WordVec should contain 0 'd'")
    }
}