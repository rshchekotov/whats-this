package edu.tum.romance.whatsthis.io

import edu.tum.romance.whatsthis.nlp.WordVec
import kotlin.test.Test
import kotlin.test.BeforeTest
import kotlin.test.assertEquals

internal class StringSourceTest {
    @BeforeTest
    fun setup() {
        WordVec.clear()
    }

    @Test
    fun testSimpleUniquePhrase() {
        val resource = "This is a test"
        val vec = StringSource.textToVec(resource)

        /* Dictionary Checks */
        assertEquals(4, WordVec.dictionary.size, "Size of dictionary should be 4")
        assertEquals("this", WordVec.dictionary[0], "First element should be 'this'")
        assertEquals("is", WordVec.dictionary[1], "Second element should be 'is'")
        assertEquals("a", WordVec.dictionary[2], "Third element should be 'a'")
        assertEquals("test", WordVec.dictionary[3], "Fourth element should be 'test'")

        /* Vector Checks */
        assertEquals(4, vec.size, "Size of vector should be 4")
        assertEquals(1, vec.data[0], "First element should be 1")
        assertEquals(1, vec.data[1], "Second element should be 1")
        assertEquals(1, vec.data[2], "Third element should be 1")
        assertEquals(1, vec.data[3], "Fourth element should be 1")
    }

    @Test
    fun testSimplePhraseWithDuplicates() {
        val resource = "This is a test and this is a test"
        val vec = StringSource.textToVec(resource)

        /* Dictionary Checks */
        assertEquals(5, WordVec.dictionary.size, "Size of dictionary should be 4")
        assertEquals("this", WordVec.dictionary[0], "First element should be 'this'")
        assertEquals("is", WordVec.dictionary[1], "Second element should be 'is'")
        assertEquals("a", WordVec.dictionary[2], "Third element should be 'a'")
        assertEquals("test", WordVec.dictionary[3], "Fourth element should be 'test'")
        assertEquals("and", WordVec.dictionary[4], "Fifth element should be 'and'")

        /* Vector Checks */
        assertEquals(5, vec.size, "Size of vector should be 4")
        assertEquals(2, vec.data[0], "First element should be 2")
        assertEquals(2, vec.data[1], "Second element should be 2")
        assertEquals(2, vec.data[2], "Third element should be 2")
        assertEquals(2, vec.data[3], "Fourth element should be 2")
        assertEquals(1, vec.data[4], "Fifth element should be 1")
    }

    @Test
    fun testMultiplePhrases() {
        val alpha = "This is a test"
        val beta = "This and that"

        val vecAlpha = StringSource.textToVec(alpha)
        val vecBeta = StringSource.textToVec(beta)
        vecAlpha.update()

        /* Dictionary Checks */
        assertEquals(6, WordVec.dictionary.size, "Size of dictionary should be 6")
        assertEquals("this", WordVec.dictionary[0], "First element should be 'this'")
        assertEquals("is", WordVec.dictionary[1], "Second element should be 'is'")
        assertEquals("a", WordVec.dictionary[2], "Third element should be 'a'")
        assertEquals("test", WordVec.dictionary[3], "Fourth element should be 'test'")
        assertEquals("and", WordVec.dictionary[4], "Fifth element should be 'and'")
        assertEquals("that", WordVec.dictionary[5], "Sixth element should be 'that'")

        /* Vector Checks */
        assertEquals(6, vecAlpha.size, "Size of vector should be 6")
        assertEquals(1, vecAlpha.data[0], "First element should be 1")
        assertEquals(1, vecAlpha.data[1], "Second element should be 1")
        assertEquals(1, vecAlpha.data[2], "Third element should be 1")
        assertEquals(1, vecAlpha.data[3], "Fourth element should be 1")
        assertEquals(0, vecAlpha.data[4], "Fifth element should be 0")
        assertEquals(0, vecAlpha.data[5], "Sixth element should be 0")

        assertEquals(6, vecBeta.size, "Size of vector should be 6")
        assertEquals(1, vecBeta.data[0], "First element should be 1")
        assertEquals(0, vecBeta.data[1], "Second element should be 0")
        assertEquals(0, vecBeta.data[2], "Third element should be 0")
        assertEquals(0, vecBeta.data[3], "Fourth element should be 0")
        assertEquals(1, vecBeta.data[4], "Fifth element should be 1")
        assertEquals(1, vecBeta.data[5], "Sixth element should be 1")
    }
}