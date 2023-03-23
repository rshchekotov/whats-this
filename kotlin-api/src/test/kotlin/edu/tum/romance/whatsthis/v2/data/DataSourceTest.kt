package edu.tum.romance.whatsthis.v2.data

import kotlin.test.Test
import kotlin.test.assertEquals

internal class DataSourceTest {
    @Test
    fun dataConstructorTest() {
        val data = "Hello World!"
        val source = InMemorySource(data, "Example")
        assertEquals(data, source.source)
        val loaded = source.load()
        assertEquals(data, loaded.first)
        assertEquals("Example", loaded.second)
    }
}