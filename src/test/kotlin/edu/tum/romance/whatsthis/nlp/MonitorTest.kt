package edu.tum.romance.whatsthis.nlp

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
class MonitorTest {

    @BeforeTest
    fun clearM(){
        Monitor.clear()
    }

    @Test
    fun clear() {
        Monitor.dictVec.dictionary = listOf("a", "b", "c")
        assertEquals(3, Monitor.dictVec.dictionary.size, "Monitor.dictVec dictionary should not be empty")
        Monitor.dictVec.clear()
        assertEquals(0, Monitor.dictVec.dictionary.size, "Monitor.dictVec dictionary should be empty")
    }

    @Test
    fun add(){
        Monitor.dictVec.dictionary = listOf("a", "b", "c","d","e")
        Monitor.add(Pair(listOf("a" to 1, "b" to 2, "c" to 3, "d" to 4, "e" to 5),"cat"))
        Monitor.add(Pair(listOf("a" to 5, "b" to 3, "c" to 6, "d" to 7, "e" to 1),"cat"))
        Monitor.add(Pair(listOf("a" to 3, "b" to 5, "c" to 5, "d" to 6, "e" to 4),"cat"))

        assertEquals(3, (Monitor.get("cat"))?.cloud?.size ?: 0, "Monitor should contain 3 words")

        assertEquals(1, (Monitor.get("cat"))?.cloud?.get(0)?.data?.get(0)?: 0, "Monitor should contain 1 'a'")
        assertEquals(2, (Monitor.get("cat"))?.cloud?.get(0)?.data?.get(1)?: 0, "Monitor should contain 2 'b'")
        assertEquals(3, (Monitor.get("cat"))?.cloud?.get(0)?.data?.get(2)?: 0, "Monitor should contain 3 'c'")
        assertEquals(4, (Monitor.get("cat"))?.cloud?.get(0)?.data?.get(3)?: 0, "Monitor should contain 4 'd'")
        assertEquals(5, (Monitor.get("cat"))?.cloud?.get(0)?.data?.get(4)?: 0, "Monitor should contain 5 'e'")

        assertEquals(5, (Monitor.get("cat"))?.cloud?.get(1)?.data?.get(0)?: 0, "Monitor should contain 5 'a'")
        assertEquals(3, (Monitor.get("cat"))?.cloud?.get(1)?.data?.get(1)?: 0, "Monitor should contain 3 'b'")
        assertEquals(6, (Monitor.get("cat"))?.cloud?.get(1)?.data?.get(2)?: 0, "Monitor should contain 6 'c'")
        assertEquals(7, (Monitor.get("cat"))?.cloud?.get(1)?.data?.get(3)?: 0, "Monitor should contain 7 'd'")
        assertEquals(1, (Monitor.get("cat"))?.cloud?.get(1)?.data?.get(4)?: 0, "Monitor should contain 1 'e'")

        assertEquals(3, (Monitor.get("cat"))?.cloud?.get(2)?.data?.get(0)?: 0, "Monitor should contain 3 'a'")
        assertEquals(5, (Monitor.get("cat"))?.cloud?.get(2)?.data?.get(1)?: 0, "Monitor should contain 5 'b'")
        assertEquals(5, (Monitor.get("cat"))?.cloud?.get(2)?.data?.get(2)?: 0, "Monitor should contain 5 'c'")
        assertEquals(6, (Monitor.get("cat"))?.cloud?.get(2)?.data?.get(3)?: 0, "Monitor should contain 6 'd'")
        assertEquals(4, (Monitor.get("cat"))?.cloud?.get(2)?.data?.get(4)?: 0, "Monitor should contain 4 'e'")

        Monitor.add(Pair(listOf("a" to 9, "b" to 0, "c" to 1, "d" to 1, "e" to 15, "f" to 10),"dog"))
        Monitor.add(Pair(listOf("a" to 11, "b" to 1, "c" to 0, "d" to 2, "e" to 17),"dog"))

        assertEquals(2, (Monitor.get("dog"))?.cloud?.size ?: 0, "Monitor should contain 2 words")

        assertEquals(6, Monitor.dictVec.dictionary.size, "Monitor.dictVec dictionary should contain 5 words")

        assertEquals(9, (Monitor.get("dog"))?.cloud?.get(0)?.data?.get(0)?: 0, "Monitor should contain 9 'a'")
        assertEquals(0, (Monitor.get("dog"))?.cloud?.get(0)?.data?.get(1)?: 0, "Monitor should contain 0 'b'")
        assertEquals(1, (Monitor.get("dog"))?.cloud?.get(0)?.data?.get(2)?: 0, "Monitor should contain 1 'c'")
        assertEquals(1, (Monitor.get("dog"))?.cloud?.get(0)?.data?.get(3)?: 0, "Monitor should contain 1 'd'")
        assertEquals(15, (Monitor.get("dog"))?.cloud?.get(0)?.data?.get(4)?: 0, "Monitor should contain 15 'e'")
        assertEquals(10, (Monitor.get("dog"))?.cloud?.get(0)?.data?.get(5)?: 0, "Monitor should contain 10 'f'")

        assertEquals(11, (Monitor.get("dog"))?.cloud?.get(1)?.data?.get(0)?: 0, "Monitor should contain 11 'a'")
        assertEquals(1, (Monitor.get("dog"))?.cloud?.get(1)?.data?.get(1)?: 0, "Monitor should contain 1 'b'")
        assertEquals(0, (Monitor.get("dog"))?.cloud?.get(1)?.data?.get(2)?: 0, "Monitor should contain 0 'c'")
        assertEquals(2, (Monitor.get("dog"))?.cloud?.get(1)?.data?.get(3)?: 0, "Monitor should contain 2 'd'")
        assertEquals(17, (Monitor.get("dog"))?.cloud?.get(1)?.data?.get(4)?: 0, "Monitor should contain 17 'e'")
        assertEquals(0, (Monitor.get("dog"))?.cloud?.get(1)?.data?.get(5)?: 0, "Monitor should contain 0 'f'")

        assertEquals(1, (Monitor.get("cat"))?.cloud?.get(0)?.data?.get(0)?: 0, "Monitor should contain 1 'a'")
        assertEquals(2, (Monitor.get("cat"))?.cloud?.get(0)?.data?.get(1)?: 0, "Monitor should contain 2 'b'")
        assertEquals(3, (Monitor.get("cat"))?.cloud?.get(0)?.data?.get(2)?: 0, "Monitor should contain 3 'c'")
        assertEquals(4, (Monitor.get("cat"))?.cloud?.get(0)?.data?.get(3)?: 0, "Monitor should contain 4 'd'")
        assertEquals(5, (Monitor.get("cat"))?.cloud?.get(0)?.data?.get(4)?: 0, "Monitor should contain 5 'e'")
        assertEquals(0, (Monitor.get("cat"))?.cloud?.get(0)?.data?.get(5)?: 0, "Monitor should contain 0 'f'")

        assertEquals(5, (Monitor.get("cat"))?.cloud?.get(1)?.data?.get(0)?: 0, "Monitor should contain 5 'a'")
        assertEquals(3, (Monitor.get("cat"))?.cloud?.get(1)?.data?.get(1)?: 0, "Monitor should contain 3 'b'")
        assertEquals(6, (Monitor.get("cat"))?.cloud?.get(1)?.data?.get(2)?: 0, "Monitor should contain 6 'c'")
        assertEquals(7, (Monitor.get("cat"))?.cloud?.get(1)?.data?.get(3)?: 0, "Monitor should contain 7 'd'")
        assertEquals(1, (Monitor.get("cat"))?.cloud?.get(1)?.data?.get(4)?: 0, "Monitor should contain 1 'e'")
        assertEquals(0, (Monitor.get("cat"))?.cloud?.get(1)?.data?.get(5)?: 0, "Monitor should contain 0 'f'")

        assertEquals(3, (Monitor.get("cat"))?.cloud?.get(2)?.data?.get(0)?: 0, "Monitor should contain 3 'a'")
        assertEquals(5, (Monitor.get("cat"))?.cloud?.get(2)?.data?.get(1)?: 0, "Monitor should contain 5 'b'")
        assertEquals(5, (Monitor.get("cat"))?.cloud?.get(2)?.data?.get(2)?: 0, "Monitor should contain 5 'c'")
        assertEquals(6, (Monitor.get("cat"))?.cloud?.get(2)?.data?.get(3)?: 0, "Monitor should contain 6 'd'")
        assertEquals(4, (Monitor.get("cat"))?.cloud?.get(2)?.data?.get(4)?: 0, "Monitor should contain 4 'e'")
        assertEquals(0, (Monitor.get("cat"))?.cloud?.get(2)?.data?.get(5)?: 0, "Monitor should contain 0 'f'")
    }

    @Test
    fun addAll(){
        Monitor.dictVec.dictionary = listOf("a", "b")
        assertEquals(2, Monitor.dictVec.dictionary.size, "Monitor.dictVec dictionary should contain 2 words")

        Monitor.addAll(listOf(Pair(listOf("a" to 1, "b" to 2, "c" to 3, "d" to 4, "e" to 5),"cat"),
                Pair(listOf("a" to 5, "b" to 3, "c" to 6, "d" to 7, "e" to 1),"cat"),
                Pair(listOf("a" to 3, "b" to 5, "c" to 5, "d" to 6, "e" to 4),"cat"),))

        assertEquals(5, Monitor.dictVec.dictionary.size, "Monitor.dictVec dictionary should contain 5 words")

        assertEquals(3, (Monitor.get("cat"))?.cloud?.size ?: 0, "Monitor should contain 3 words")

        assertEquals(1, (Monitor.get("cat"))?.cloud?.get(0)?.data?.get(0)?: 0, "Monitor should contain 1 'a'")
        assertEquals(2, (Monitor.get("cat"))?.cloud?.get(0)?.data?.get(1)?: 0, "Monitor should contain 2 'b'")
        assertEquals(3, (Monitor.get("cat"))?.cloud?.get(0)?.data?.get(2)?: 0, "Monitor should contain 3 'c'")
        assertEquals(4, (Monitor.get("cat"))?.cloud?.get(0)?.data?.get(3)?: 0, "Monitor should contain 4 'd'")
        assertEquals(5, (Monitor.get("cat"))?.cloud?.get(0)?.data?.get(4)?: 0, "Monitor should contain 5 'e'")

        assertEquals(5, (Monitor.get("cat"))?.cloud?.get(1)?.data?.get(0)?: 0, "Monitor should contain 5 'a'")
        assertEquals(3, (Monitor.get("cat"))?.cloud?.get(1)?.data?.get(1)?: 0, "Monitor should contain 3 'b'")
        assertEquals(6, (Monitor.get("cat"))?.cloud?.get(1)?.data?.get(2)?: 0, "Monitor should contain 6 'c'")
        assertEquals(7, (Monitor.get("cat"))?.cloud?.get(1)?.data?.get(3)?: 0, "Monitor should contain 7 'd'")
        assertEquals(1, (Monitor.get("cat"))?.cloud?.get(1)?.data?.get(4)?: 0, "Monitor should contain 1 'e'")

        assertEquals(3, (Monitor.get("cat"))?.cloud?.get(2)?.data?.get(0)?: 0, "Monitor should contain 3 'a'")
        assertEquals(5, (Monitor.get("cat"))?.cloud?.get(2)?.data?.get(1)?: 0, "Monitor should contain 5 'b'")
        assertEquals(5, (Monitor.get("cat"))?.cloud?.get(2)?.data?.get(2)?: 0, "Monitor should contain 5 'c'")
        assertEquals(6, (Monitor.get("cat"))?.cloud?.get(2)?.data?.get(3)?: 0, "Monitor should contain 6 'd'")
        assertEquals(4, (Monitor.get("cat"))?.cloud?.get(2)?.data?.get(4)?: 0, "Monitor should contain 4 'e'")
    }

    @Test
    fun findClosestCloud(){
        Monitor.dictVec.dictionary = listOf("a", "b", "c","d","e")

        Monitor.add(Pair(listOf("a" to 1, "b" to 2, "c" to 3, "d" to 4, "e" to 5),"cat"))
        Monitor.add(Pair(listOf("a" to 5, "b" to 3, "c" to 6, "d" to 7, "e" to 1),"cat"))
        Monitor.add(Pair(listOf("a" to 3, "b" to 5, "c" to 5, "d" to 6, "e" to 4),"cat"))
        Monitor.add(Pair(listOf("a" to 9, "b" to 0, "c" to 1, "d" to 1, "e" to 15, "f" to 10),"dog"))
        Monitor.add(Pair(listOf("a" to 11, "b" to 1, "c" to 0, "d" to 2, "e" to 17),"dog"))

        assertEquals("cat", Monitor.findClosestCloud(listOf("a" to 1, "b" to 2, "c" to 3, "d" to 4, "e" to 5)), "Should find a cat there")
        assertEquals("dog", Monitor.findClosestCloud(listOf("a" to 10, "b" to 1, "c" to 1, "d" to 2, "e" to 16)), "Should find a dog there")

        assertEquals("cat", Monitor.findClosestCloud(listOf("a" to 4, "b" to 2, "c" to 4, "d" to 5, "e" to 3)), "Should find a cat there")
        assertEquals("dog", Monitor.findClosestCloud(listOf("a" to 10, "b" to 1, "c" to 1, "d" to 2, "e" to 16, "f" to 5)), "Should find a dog there")
    }

}