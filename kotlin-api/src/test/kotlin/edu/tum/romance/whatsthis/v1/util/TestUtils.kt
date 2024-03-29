package edu.tum.romance.whatsthis.v1.util

import org.junit.jupiter.api.Assumptions.assumeTrue
import java.net.URL

fun assumeConnectivity(url: URL) = try {
    url.openConnection().connect()
} catch (e: Exception) {
    assumeTrue(false, "No internet connection.")
}