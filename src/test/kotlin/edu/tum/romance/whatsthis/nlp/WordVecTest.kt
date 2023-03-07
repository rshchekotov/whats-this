package edu.tum.romance.whatsthis.nlp

import kotlin.test.Test
import kotlin.test.assertEquals

internal class WordVecTest{
    @Test
    fun clear() {
        Monitor.dictVec.dictionary = listOf("a", "b", "c")
        assertEquals(3, Monitor.dictVec.dictionary.size, "Monitor.dictVec dictionary should not be empty")
        Monitor.dictVec.clear()
        assertEquals(0, Monitor.dictVec.dictionary.size, "Monitor.dictVec dictionary should be empty")
    }

    @Test
    fun plus() {
        Monitor.dictVec.clear()
        Monitor.dictVec + listOf("a" to 1, "b" to 2, "c" to 3)
        assertEquals(3, Monitor.dictVec.dictionary.size, "Monitor.dictVec dictionary should contain 3 words")
        assertEquals("a", Monitor.dictVec.dictionary[0], "Monitor.dictVec dictionary should contain 'a'")
        assertEquals("b", Monitor.dictVec.dictionary[1], "Monitor.dictVec dictionary should contain 'b'")
        assertEquals("c", Monitor.dictVec.dictionary[2], "Monitor.dictVec dictionary should contain 'c'")

        Monitor.dictVec + listOf("a" to 1, "b" to 2, "d" to 1)
        assertEquals(4, Monitor.dictVec.dictionary.size, "Monitor.dictVec dictionary should contain 4 words")
        assertEquals("a", Monitor.dictVec.dictionary[0], "Monitor.dictVec dictionary should contain 'a'")
        assertEquals("b", Monitor.dictVec.dictionary[1], "Monitor.dictVec dictionary should contain 'b'")
        assertEquals("c", Monitor.dictVec.dictionary[2], "Monitor.dictVec dictionary should contain 'c'")
        assertEquals("d", Monitor.dictVec.dictionary[3], "Monitor.dictVec dictionary should contain 'd'")

        Monitor.dictVec.clear()
        Monitor.dictVec + listOf("a" to 0)
        assertEquals(1, Monitor.dictVec.dictionary.size, "Monitor.dictVec dictionary should contain 1 word")
        assertEquals("a", Monitor.dictVec.dictionary[0], "Monitor.dictVec dictionary should contain 'a'")

    }

    @Test
    fun createVec() {
        Monitor.dictVec.clear()
        Monitor.dictVec + listOf("a" to 1, "b" to 2, "c" to 3)
        val vec = Monitor.dictVec.createVec(listOf("a" to 1, "b" to 2, "c" to 3))
        assertEquals(3, vec.data.size, "Monitor.dictVec should contain 3 words")
        assertEquals(1, vec.data[0], "Monitor.dictVec should contain 1 'a'")
        assertEquals(2, vec.data[1], "Monitor.dictVec should contain 2 'b'")
        assertEquals(3, vec.data[2], "Monitor.dictVec should contain 3 'c'")

        Monitor.dictVec + listOf("a" to 1, "b" to 2, "d" to 1)
        val vec2 = Monitor.dictVec.createVec(listOf("a" to 1, "b" to 2, "c" to 3))
        assertEquals(4, vec2.data.size, "Monitor.dictVec should contain 4 words")
        assertEquals(1, vec2.data[0], "Monitor.dictVec should contain 1 'a'")
        assertEquals(2, vec2.data[1], "Monitor.dictVec should contain 2 'b'")
        assertEquals(3, vec2.data[2], "Monitor.dictVec should contain 3 'c'")
        assertEquals(0, vec2.data[3], "Monitor.dictVec should contain 0 'd'")
    }

    @Test
    fun update() {
        Monitor.dictVec.clear()
        Monitor.dictVec + listOf("a" to 1, "b" to 2, "c" to 3)
        val vec = Monitor.dictVec.createVec(listOf("a" to 1, "b" to 2, "c" to 3))
        assertEquals(3, vec.data.size, "Monitor.dictVec should contain 3 words")
        assertEquals(1, vec.data[0], "Monitor.dictVec should contain 1 'a'")
        assertEquals(2, vec.data[1], "Monitor.dictVec should contain 2 'b'")
        assertEquals(3, vec.data[2], "Monitor.dictVec should contain 3 'c'")

        vec.update()
        assertEquals(3, vec.data.size, "Monitor.dictVec should contain 3 words")
        assertEquals(1, vec.data[0], "Monitor.dictVec should contain 1 'a'")
        assertEquals(2, vec.data[1], "Monitor.dictVec should contain 2 'b'")
        assertEquals(3, vec.data[2], "Monitor.dictVec should contain 3 'c'")

        Monitor.dictVec + listOf("a" to 1, "b" to 2, "d" to 1)
        vec.update()
        assertEquals(4, vec.data.size, "Monitor.dictVec should contain 4 words")
        assertEquals(1, vec.data[0], "Monitor.dictVec should contain 1 'a'")
        assertEquals(2, vec.data[1], "Monitor.dictVec should contain 2 'b'")
        assertEquals(3, vec.data[2], "Monitor.dictVec should contain 3 'c'")
        assertEquals(0, vec.data[3], "Monitor.dictVec should contain 0 'd'")
    }
}