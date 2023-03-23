package edu.tum.romance.whatsthis.v2.data

import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals

internal class FileSourceTest {
    @Test
    fun testHTMLLoading() {
        val path = javaClass.classLoader.getResource("data/basic.html")!!.toURI()!!
        val file = File(path)
        val source = FileSource(file)
        val (text, title) = source.load()
        assertEquals("Some very simple text", text)
        assertEquals("Basic", title)
    }

    @Test
    fun testPlainLoading() {
        val path = javaClass.classLoader.getResource("data/paragraph.txt")!!.toURI()!!
        val file = File(path)
        val source = FileSource(file)
        val (text, title) = source.load()
        assertEquals("One paragraph of simple words.", text)
        assertEquals("paragraph", title)
    }
}