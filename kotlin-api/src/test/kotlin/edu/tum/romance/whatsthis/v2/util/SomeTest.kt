package edu.tum.romance.whatsthis.v2.util

import org.junit.jupiter.api.Tag
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@Tag("v2")
internal class SomeTest {
    @Test
    fun `test multiple`() {
        val some = Some.of("a", "b*tch")
        assertEquals(2, some.quantity, "Should be multiple!")
        assertFalse(some.ifNone { true } ?: false, "Should not be none!")
        assertFalse(some.ifSingle { true } ?: false, "Should not be single!")
        assertTrue(some.ifMany { true } ?: false, "Should be many!")
    }

    @Test
    fun `test single`() {
        val some = Some.of("a")
        assertEquals(1, some.quantity, "Should be single!")
        assertFalse(some.ifNone { true } ?: false, "Should not be none!")
        assertTrue(some.ifSingle { true } ?: false, "Should be single!")
        assertFalse(some.ifMany { true } ?: false, "Should not be many!")
    }

    @Test
    fun `test none`() {
        val some = Some.empty<String>()
        assertEquals(0, some.quantity, "Should be empty!")
        assertTrue(some.ifNone { true } ?: false, "Should be none!")
        assertFalse(some.ifSingle { true } ?: false, "Should not be single!")
        assertFalse(some.ifMany { true } ?: false, "Should not be many!")
    }

    @Test
    fun `test words`() {
        val phrase = "a word test"
        val some = Some.words(phrase)
        assertEquals(3, some.quantity, "Should be 3 words!")
        assertFalse(some.ifNone { true } ?: false, "Should not be none!")
        assertFalse(some.ifSingle { true } ?: false, "Should not be single!")
        assertTrue(some.ifMany { true } ?: false, "Should be many!")
    }

    @Test
    fun `test flatten`() {
        val someSome = listOf(Some.of("this"), Some.of("that", "and"), Some.empty())
        val some = someSome.flatten()
        assertEquals(3, some.quantity, "Should be 2 words!")
        assertFalse(some.ifNone { true } ?: false, "Should not be none!")
        assertFalse(some.ifSingle { true } ?: false, "Should not be single!")
        assertTrue(some.ifMany { true } ?: false, "Should be many!")
    }
}