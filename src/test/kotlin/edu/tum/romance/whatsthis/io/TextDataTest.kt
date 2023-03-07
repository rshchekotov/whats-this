package edu.tum.romance.whatsthis.io

import edu.tum.romance.whatsthis.util.assumeConnectivity
import java.net.URL
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class TextDataTest {
    @Test
    fun testLoadString() {
        val text = "Tea"
        val data = TextData(text)

        assertEquals(data.text, text, "Text should be equal")
        assertEquals(data.source, text, "Source should be equal")
        assertEquals(1, data.tokens.size, "Vector should have one element")
        assertEquals("tea", data.tokens[0].first, "Vector should contain 'tea'")
        assertEquals(1, data.tokens[0].second, "Vector should contain 'tea' once")
    }

    @Test
    fun testLoadURL() {
        val url = URL("https://en.wikipedia.org/wiki/Bayesian_network")
        assumeConnectivity(url)
        val data = TextData(url)

        assertEquals(data.source, url, "Source should be equal")
        assertTrue(data.text.isNotEmpty(), "Text should not be empty")
        assertTrue(data.tokens.isNotEmpty(), "Vector should not be empty")
        assertTrue(data.tokens.any { it.first == "bayesian" }, "Vector should contain 'bayesian'")
    }

    @Suppress("SpellCheckingInspection")
    @Test
    fun testCleaningAlgorithm() {
        val text = "Some math follows... {\\displaystyle x^2 - (\\frac{1}{2}\\pi)}. That's it!"
        val data = TextData(text)

        assertEquals(data.text, text, "Text should be equal")
        assertEquals(data.source, text, "Source should be equal")

        assertEquals(6, data.tokens.size, "Vector should have six elements")
        assertEquals("some", data.tokens[0].first, "Vector should contain 'some'")
        assertEquals(1, data.tokens[0].second, "Vector should contain 'some' once")
        assertEquals("math", data.tokens[1].first, "Vector should contain 'math'")
        assertEquals(1, data.tokens[1].second, "Vector should contain 'math' once")
        assertEquals("follows", data.tokens[2].first, "Vector should contain 'follows'")
        assertEquals(1, data.tokens[2].second, "Vector should contain 'follows' once")
        assertEquals("MATH", data.tokens[3].first, "Vector should contain 'MATH'")
        assertEquals(1, data.tokens[3].second, "Vector should contain 'MATH' once")
        assertEquals("thats", data.tokens[4].first, "Vector should contain 'thats'")
        assertEquals(1, data.tokens[4].second, "Vector should contain 'thats' once")
        assertEquals("it", data.tokens[5].first, "Vector should contain 'it'")
        assertEquals(1, data.tokens[5].second, "Vector should contain 'it' once")
    }
}