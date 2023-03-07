package edu.tum.romance.whatsthis.nlp

import edu.tum.romance.whatsthis.io.TextData
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
        assertEquals(3, Monitor.dictVec.dictionary.size, "Monitor.dictVecxx dictionary shouldxx not be empty")
        Monitor.dictVec.clear()
        assertEquals(0, Monitor.dictVec.dictionary.size, "Monitor.dictVecxx dictionary shouldxx be empty")
    }

    @Test
    fun add(){
        Monitor.dictVec.dictionary = listOf("axx", "bxx", "cxx","dxx","exx")

        Monitor.add(TextData("axx bxx bxx cxx cxx cxx dxx dxx dxx dxx exx exx exx exx exx"),"cat")
        Monitor.add(TextData("axx axx axx axx axx bxx bxx bxx cxx cxx cxx cxx cxx cxx dxx dxx dxx dxx dxx dxx dxx exx"),"cat")
        Monitor.add(TextData("axx axx axx bxx bxx bxx bxx bxx cxx cxx cxx cxx cxx dxx dxx dxx dxx dxx dxx exx exx exx exx"), "cat")

        assertEquals(3, (Monitor.get("cat"))?.cloud?.size ?: 0, "Monitor shouldxx contain 3 words")

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

        Monitor.add(TextData("axx axx axx axx axx axx axx axx axx cxx dxx exx exx exx exx exx exx exx exx exx exx exx exx exx exx exx fxx fxx fxx fxx fxx fxx fxx fxx fxx fxx"),"dog")
        Monitor.add(TextData("axx axx axx axx axx axx axx axx axx axx axx bxx dxx dxx exx exx exx exx exx exx exx exx exx exx exx exx exx exx exx exx exx"),"dog")

        assertEquals(2, (Monitor.get("dog"))?.cloud?.size ?: 0, "Monitor shouldxx contain 2 words")

        assertEquals(6, Monitor.dictVec.dictionary.size, "Monitor.dictVecxx dictionary shouldxx contain 5 words")

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
        Monitor.dictVec.dictionary = listOf("axx", "bxx")
        assertEquals(2, Monitor.dictVec.dictionary.size, "Monitor.dictVec dictionary should contain 2 words")

        Monitor.addAll(listOf(Pair(TextData("axx bxx bxx cxx cxx cxx dxx dxx dxx dxx exx exx exx exx exx"),"cat"),
                Pair(TextData("axx axx axx axx axx bxx bxx bxx cxx cxx cxx cxx cxx cxx dxx dxx dxx dxx dxx dxx dxx exx"),"cat"),
                Pair(TextData("axx axx axx bxx bxx bxx bxx bxx cxx cxx cxx cxx cxx dxx dxx dxx dxx dxx dxx exx exx exx exx"), "cat")))

        assertEquals(5, Monitor.dictVec.dictionary.size, "Monitor.dictVecxx dictionary shouldxx contain 5 words")

        assertEquals(3, (Monitor.get("cat"))?.cloud?.size ?: 0, "Monitor shouldxx contain 3 words")

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
        Monitor.dictVec.dictionary = listOf("axx", "bxx", "cxx","dxx","exx")

        Monitor.add(TextData("axx bxx bxx cxx cxx cxx dxx dxx dxx dxx exx exx exx exx exx"),"cat")
        Monitor.add(TextData("axx axx axx axx axx bxx bxx bxx cxx cxx cxx cxx cxx cxx dxx dxx dxx dxx dxx dxx dxx exx"),"cat")
        Monitor.add(TextData("axx axx axx bxx bxx bxx bxx bxx cxx cxx cxx cxx cxx dxx dxx dxx dxx dxx dxx exx exx exx exx"), "cat")
        Monitor.add(TextData("axx axx axx axx axx axx axx axx axx cxx dxx exx exx exx exx exx exx exx exx exx exx exx exx exx exx exx fxx fxx fxx fxx fxx fxx fxx fxx fxx fxx"),"dog")
        Monitor.add(TextData("axx axx axx axx axx axx axx axx axx axx axx bxx dxx dxx exx exx exx exx exx exx exx exx exx exx exx exx exx exx exx exx exx"),"dog")

        assertEquals("cat", Monitor.findClosestCloud(TextData("axx bxx bxx cxx cxx cxx dxx dxx dxx dxx exx exx exx exx exx")), "Should find axx cat there")
        assertEquals("dog", Monitor.findClosestCloud(TextData("axx axx axx axx axx axx axx axx axx axx axx bxx dxx dxx exx exx exx exx exx exx exx exx exx exx exx exx exx exx exx exx exx")), "Should find axx dog there")

        assertEquals("cat", Monitor.findClosestCloud(TextData("axx axx axx axx bxx bxx cxx cxx cxx cxx dxx dxx dxx dxx dxx exx exx exx")), "Should find axx cat there")
        assertEquals("dog", Monitor.findClosestCloud(TextData("axx axx axx axx axx axx axx axx axx axx bxx dxx cxx dxx dxx exx exx exx exx exx exx exx exx exx exx exx exx exx exx exx exx")), "Should find axx dog there")
    }
}
