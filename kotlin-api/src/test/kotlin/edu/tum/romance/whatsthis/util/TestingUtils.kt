package edu.tum.romance.whatsthis.util

import org.junit.jupiter.api.Assumptions.assumeFalse
import java.net.URL

fun assumeConnectivity(url: String) {
    try {
        URL(url).openConnection().connect()
    } catch (e: Exception) {
        assumeFalse(true, "No connectivity. Cannot reach '$url'")
    }
}

fun <T> benchmark(process: () -> T): Pair<T, Long> {
    val start = System.currentTimeMillis()
    val cachedLoad = process()
    val end = System.currentTimeMillis()
    return cachedLoad to end - start
}