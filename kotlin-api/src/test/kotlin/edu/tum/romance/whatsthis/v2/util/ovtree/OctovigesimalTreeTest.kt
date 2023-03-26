package edu.tum.romance.whatsthis.v2.util.ovtree

import edu.tum.romance.whatsthis.v2.util.Some
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.assertThrows
import kotlin.IllegalStateException
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@Tag("v2")
internal class OctovigesimalTreeTest {
    @Test
    fun `verify indices`() {
        val tree = OctovigesimalTree<String>(3) { it }
        val data = Some.words("one punch man")

        assertEquals(3, data.quantity)
        val indices = tree[data]
        assertEquals(4, indices.size)
        assertContentEquals(intArrayOf(14, 15, 12, "one punch man".hashCode()), indices, "Indices should be correct")

        assertThrows<IllegalStateException>("Empty Data shouldn't have indices!") { tree[Some.empty()] }
    }

    @Test
    fun `test insertion`() {
        val tree = OctovigesimalTree<String>(3) { it }
        val data = Some.words("sword art online")
        assertEquals(0, tree.size)
        tree += data
        assertEquals(1, tree.size)
        assertEquals(data, tree[tree[data]])
        assertContentEquals(data, tree.some<String>(), "Tree should contain the data")
    }

    @Test
    fun `test replacement`() {
        val tree = OctovigesimalTree<String>(3) { it }
        val data = Some.words("sword art online")
        assertEquals(0, tree.size)
        tree += data
        assertEquals(1, tree.size)
        assertEquals(data, tree[tree[data]])
        tree[tree[data]] = Some.words("invalid data")
        assertEquals(1, tree.size)
        assertContentEquals(Some.words("invalid data"), tree.some<String>(), "Tree should contain the data")
    }

    @Test
    fun `test removal`() {
        val tree = OctovigesimalTree<String>(3) { it }
        val data = Some.words("sword art online")
        tree += data
        assertEquals(1, tree.size)
        tree -= tree[data]
        assertEquals(0, tree.size)
        assertTrue(tree.some<String>().ifNone { true } ?: false, "Tree should be empty")
    }

    @Test
    fun `test size and clear`() {
        val tree = OctovigesimalTree<String>(3) { it }
        tree += Some.words("sword art online")
        tree += Some.words("one punch man")
        tree += Some.words("attack on titan")
        tree += Some.words("a silent voice")
        tree += Some.words("the promised neverland")
        assertEquals(5, tree.size)
        assertTrue(tree.some<String>().ifNone { false } ?: true, "Tree should not be empty")
        tree.clear()
        assertEquals(0, tree.size)
        assertTrue(tree.some<String>().ifNone { true } ?: false, "Tree should be empty")
    }

    @Test
    fun `test invalid some-type`() {
        val tree = OctovigesimalTree<Boolean>(1) { it.toString() }
        val data = Some.of(true)
        tree += data
        assertEquals(1, tree.size)
        assertEquals(data, tree[tree[data]])
        assertThrows<IllegalStateException> { tree.some<Boolean>() }
    }

    @Test
    fun `test special types`() {
        val tree = OctovigesimalTree<String>(4) { it }
        val data = Some.words("send this 2 MAIL")
        tree += data
        assertEquals(1, tree.size)
        assertEquals(data, tree[tree[data]])
        assertContentEquals(data, tree.some<String>(), "Tree should contain the data")
    }
}