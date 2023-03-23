package edu.tum.romance.whatsthis.v2.data

/**
 * A data source that stores data in memory.
 *
 * This class is made exclusively for testing purposes.
 */
class InMemorySource(data: String, private val title: String): DataSource<String>(data) {
    override fun load(): Pair<String, String> {
        return Pair(source, title)
    }
}