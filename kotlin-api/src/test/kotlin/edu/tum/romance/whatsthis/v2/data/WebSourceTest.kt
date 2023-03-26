package edu.tum.romance.whatsthis.v2.data

import edu.tum.romance.whatsthis.util.assumeConnectivity
import edu.tum.romance.whatsthis.util.benchmark
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.RepetitionInfo
import org.junit.jupiter.api.Tag
import kotlin.test.*

private val urls = arrayOf(
    "https://en.wikipedia.org/wiki/Tensor",
    "https://en.wikipedia.org/wiki/Linear_algebra",
    "https://en.wikipedia.org/wiki/Engineering",
    "https://en.wikipedia.org/wiki/Associative_property",
    "https://en.wikipedia.org/wiki/Bracket",
    "https://en.wikipedia.org/wiki/Punctuation",
    "https://en.wikipedia.org/wiki/Online_chat",
    "https://en.wikipedia.org/wiki/Song_dynasty",
    "https://en.wikipedia.org/wiki/Chinese_language",
    "https://en.wikipedia.org/wiki/Chinese_characters",
    "https://en.wikipedia.org/wiki/Japan",
    "https://en.wikipedia.org/wiki/Tokyo",
    "https://en.wikipedia.org/wiki/Yokohama",
    "https://en.wikipedia.org/wiki/Osaka",
    "https://en.wikipedia.org/wiki/Nagoya",
    "https://en.wikipedia.org/wiki/Sapporo",
    "https://en.wikipedia.org/wiki/Fukuoka",
    "https://en.wikipedia.org/wiki/Kobe",
    "https://en.wikipedia.org/wiki/Kyoto",
    "https://en.wikipedia.org/wiki/Hokkaido",
    "https://en.wikipedia.org/wiki/Honshu",
    "https://en.wikipedia.org/wiki/Shikoku",
    "https://en.wikipedia.org/wiki/Kyushu",
    "https://en.wikipedia.org/wiki/Okinawa_Island"
)
private const val TESTS_PER_URL = 100
private const val URLS = 24
private var progress = 0

@Tag("v2")
internal class WebSourceTest {
    @BeforeTest
    fun setUp() {
        DataSource.clearCache()
        println("Cache cleared...")
    }

    @Tag("CI_EXCLUDE")
    @RepeatedTest(TESTS_PER_URL * URLS)
    fun testWebLoadingPerformance(repetitionInfo: RepetitionInfo) {
        assertEquals(URLS, urls.size, "Number of URLs should match number of tests! Rewrite the test!")

        val url = urls[progress]
        if(repetitionInfo.currentRepetition % TESTS_PER_URL == 0) progress++
        val name = url.split("/").last().replace("_", " ") + " - Wikipedia"
        assumeConnectivity(url)
        val source = WebSource(url)

        assertFalse(source.isCached(), "Source should not be cached")
        val (initialLoad, initialTime) = benchmark { source.load() }
        println("Initial Web load took ${initialTime}ms")
        assertEquals(name, initialLoad.second, "Title should be correct")

        assertTrue(source.isCached(), "Source should now be cached")
        val (cachedLoad, cachedTime) = benchmark { source.load() }
        println("Cached Web load took ${cachedTime}ms")
        assertEquals(name, cachedLoad.second, "Title should be correct")

        assertTrue(2.5 * cachedTime < initialTime, "Cached load should be at least 2.5x faster")
        println("---")
    }

    @Test
    fun testWebLoading() {
        val url = urls[0]
        val name = url.split("/").last().replace("_", " ") + " - Wikipedia"
        assumeConnectivity(url)
        val source = WebSource(url)

        assertFalse(source.isCached(), "Source should not be cached")
        val (initialLoad, initialTime) = benchmark { source.load() }
        println("Initial Web load took ${initialTime}ms")
        assertEquals(name, initialLoad.second, "Title should be correct")

        assertTrue(source.isCached(), "Source should now be cached")
        val (cachedLoad, cachedTime) = benchmark { source.load() }
        println("Cached Web load took ${cachedTime}ms")
        assertEquals(name, cachedLoad.second, "Title should be correct")
    }
}