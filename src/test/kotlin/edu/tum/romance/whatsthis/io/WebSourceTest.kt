package edu.tum.romance.whatsthis.io

import edu.tum.romance.whatsthis.nlp.WordVec
import edu.tum.romance.whatsthis.util.assumeConnectivity
import org.junit.jupiter.api.assertDoesNotThrow
import java.net.URL
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

internal class WebSourceTest {
    @BeforeTest
    fun setup() {
        WordVec.clear()
    }

    @Test
    fun testWikiCar() {
        val url = URL("https://en.wikipedia.org/wiki/Car")
        assumeConnectivity(url)
        val vec = assertDoesNotThrow("Web Source should not fail.") { WebSource.textToVec(url) }
        assertEquals(vec.size, WordVec.dictionary.size, "Vector size does not match dictionary size")
    }

    @Test
    fun testWikiTwoArticles() {
        val alpha = URL("https://en.wikipedia.org/wiki/Alpha")
        val beta = URL("https://en.wikipedia.org/wiki/Beta")
        assumeConnectivity(alpha)
        val alphaVec = assertDoesNotThrow("Web Source (for Alpha) should not fail.") { WebSource.textToVec(alpha) }
        val betaVec = assertDoesNotThrow("Web Source (for Beta) should not fail.") { WebSource.textToVec(beta) }
        alphaVec.update()

        assertEquals(alphaVec.size, WordVec.dictionary.size, "Vector size does not match dictionary size")
        assertEquals(betaVec.size, WordVec.dictionary.size, "Vector size does not match dictionary size")
    }

    @Test
    fun testPlain() {
        val fuzz = URL("https://raw.githubusercontent.com/Bo0oM/fuzz.txt/master/fuzz.txt")
        assumeConnectivity(fuzz)
        val vec = assertDoesNotThrow("Web Source should not fail.") { WebSource.textToVec(fuzz) }
        assertEquals(vec.size, WordVec.dictionary.size, "Vector size does not match dictionary size")
    }
}