package edu.tum.romance.whatsthis.v2.data

import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.assertThrows
import java.io.File
import java.lang.IllegalStateException
import kotlin.test.Test
import kotlin.test.assertEquals


@Tag("v2")
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

    @Test
    fun testPDFLoading() {
        val path = javaClass.classLoader.getResource("data/document.pdf")!!.toURI()!!
        val file = File(path)
        val source = FileSource(file)
        val (text, title) = source.load()
        assertEquals("document", title, "Title should be 'document'")
        assertEquals("Hello World!\n1\n",
            text.replace("\r\n", "\n"),
            "Text should be 'Hello World!' and the page number.")
    }

    @Test
    fun invalidFileType() {
        val path = javaClass.classLoader.getResource("data/virus.exe")!!.toURI()!!
        val file = File(path)
        val source = FileSource(file)
        assertThrows<IllegalStateException> { source.load() }
    }
}