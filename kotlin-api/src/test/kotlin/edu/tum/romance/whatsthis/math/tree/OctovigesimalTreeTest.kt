package edu.tum.romance.whatsthis.math.tree

import edu.tum.romance.whatsthis.data.TextData
import edu.tum.romance.whatsthis.util.assumeConnectivity
import java.net.URL
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertFails

internal class OctovigesimalTreeTest {
    @Test
    fun testInsertionChecksAndOrder() {
        val tree = OctovigesimalTree<String>(2)
        tree += "another example".words()
        tree += "alpha example".words()
        assertFails("Tree should not allow the insertion of single-element vectors.") { tree += "example".words() }
        assertFails("Tree should not allow the insertion of 3-element vectors.") { tree += "another alpha example".words() }
        val data = tree.getData()
        assertContentEquals(arrayOf("alpha", "example"), data[0], "Tree should return data in natural order (0).")
        assertContentEquals(arrayOf("another", "example"), data[1], "Tree should return data in natural order (1).")
    }

    @Test
    fun testUnigram() {
        val tree = OctovigesimalTree<String>(1)
        tree += "america".words()
        tree += "russia".words()
        tree += "china".words()
        tree += "germany".words()
        tree += "france".words()

        val data = tree.getData()

        assertContentEquals(arrayOf(0, 0), tree.indexOf("america".words()), "Tree should return the correct index for a single-element vector. (0)")
        assertContentEquals(arrayOf(2, 0), tree.indexOf("china".words()), "Tree should return the correct index for a single-element vector. (1)")
        assertContentEquals(arrayOf(5, 0), tree.indexOf("france".words()), "Tree should return the correct index for a single-element vector. (2)")
        assertContentEquals(arrayOf(6, 0), tree.indexOf("germany".words()), "Tree should return the correct index for a single-element vector. (3)")
        assertContentEquals(arrayOf(17, 0), tree.indexOf("russia".words()), "Tree should return the correct index for a single-element vector. (4)")

        assertContentEquals(arrayOf("america"), data[0], "Tree should return data in natural order (0).")
        assertContentEquals(arrayOf("china"), data[1], "Tree should return data in natural order (1).")
        assertContentEquals(arrayOf("france"), data[2], "Tree should return data in natural order (2).")
        assertContentEquals(arrayOf("germany"), data[3], "Tree should return data in natural order (3).")
        assertContentEquals(arrayOf("russia"), data[4], "Tree should return data in natural order (4).")
    }

    @Test
    fun testTrigram() {
        val tree = OctovigesimalTree<String>(3)
        tree += "one punch man".words()
        tree += "the promised neverland".words()
        tree += "villain to kill".words()
        tree += "tower of god".words()
        tree += "the only necromancer".words()
        tree += "sword art online".words()
        tree += "the hero returns".words()
        tree += "return to player".words()

        val data = tree.getData()

        assertContentEquals(arrayOf(14, 15, 12, 0), tree.indexOf("one punch man".words()), "Tree should return the correct index for a 3-element vector. (0)")
        assertContentEquals(arrayOf(17, 19, 15, 0), tree.indexOf("return to player".words()), "Tree should return the correct index for a 3-element vector. (1)")
        assertContentEquals(arrayOf(18, 0, 14, 0), tree.indexOf("sword art online".words()), "Tree should return the correct index for a 3-element vector. (2)")
        assertContentEquals(arrayOf(19, 7, 17, 0), tree.indexOf("the hero returns".words()), "Tree should return the correct index for a 3-element vector. (3)")
        assertContentEquals(arrayOf(19, 14, 6, 0), tree.indexOf("tower of god".words()), "Tree should return the correct index for a 3-element vector. (6)")
        assertContentEquals(arrayOf(19, 14, 13, 0), tree.indexOf("the only necromancer".words()), "Tree should return the correct index for a 3-element vector. (4)")
        assertContentEquals(arrayOf(19, 15, 13, 0), tree.indexOf("the promised neverland".words()), "Tree should return the correct index for a 3-element vector. (5)")
        assertContentEquals(arrayOf(21, 19, 10, 0), tree.indexOf("villain to kill".words()), "Tree should return the correct index for a 3-element vector. (7)")

        assertContentEquals("one punch man".words(), data[0], "Tree should return data in natural order (0).")
        assertContentEquals("return to player".words(), data[1], "Tree should return data in natural order (1).")
        assertContentEquals("sword art online".words(), data[2], "Tree should return data in natural order (2).")
        assertContentEquals("the hero returns".words(), data[3], "Tree should return data in natural order (3).")
        assertContentEquals("tower of god".words(), data[4], "Tree should return data in natural order (6).")
        assertContentEquals("the only necromancer".words(), data[5], "Tree should return data in natural order (4).")
        assertContentEquals("the promised neverland".words(), data[6], "Tree should return data in natural order (5).")
        assertContentEquals("villain to kill".words(), data[7], "Tree should return data in natural order (7).")
    }

    @Test
    fun testTextDataTree() {
        val urls = arrayOf(
            URL("https://en.wikipedia.org/wiki/America"),
            URL("https://en.wikipedia.org/wiki/Russia"),
            URL("https://en.wikipedia.org/wiki/China"),
            URL("https://en.wikipedia.org/wiki/Germany"),
            URL("https://en.wikipedia.org/wiki/France")
        )
        for(url in urls) assumeConnectivity(url)

        val tree = OctovigesimalTree<TextData<*>>(1) { it.name.lowercase() }

        val elements = urls.map { TextData(it, it.path.substring(6)) }

        for(element in elements) tree += arrayOf(element)

        val data = tree.getData()
        assertContentEquals(arrayOf(elements[0]), data[0], "Tree should return data in natural order (0).")
        assertContentEquals(arrayOf(elements[2]), data[1], "Tree should return data in natural order (1).")
        assertContentEquals(arrayOf(elements[4]), data[2], "Tree should return data in natural order (2).")
        assertContentEquals(arrayOf(elements[3]), data[3], "Tree should return data in natural order (3).")
        assertContentEquals(arrayOf(elements[1]), data[4], "Tree should return data in natural order (4).")
    }

    private fun String.words() = this.split(" ").toTypedArray()
}