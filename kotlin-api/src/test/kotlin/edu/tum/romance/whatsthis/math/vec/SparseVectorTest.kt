package edu.tum.romance.whatsthis.math.vec

import kotlin.math.sqrt
import kotlin.test.Test
import kotlin.test.assertEquals

internal class SparseVectorTest {
    @Test
    fun testBasicOps() {
        val a = SparseVector(3)
        a[0] = 1.0
        a[1] = 2.0
        a[2] = 3.0

        val b = SparseVector(3)
        b[0] = 4.0
        b[1] = 5.0
        b[2] = 6.0

        val c = a + b
        assertEquals(5.0, c[0])
        assertEquals(7.0, c[1])
        assertEquals(9.0, c[2])

        val d = a - b
        assertEquals(-3.0, d[0])
        assertEquals(-3.0, d[1])
        assertEquals(-3.0, d[2])

        val e = d.unit()
        for(i in 0 until 3) {
            assertEquals(-3.0 / sqrt(27.0), e[i], "Unit Vector is not correct at index $i")
        }
    }

    @Test
    fun testDirtyClean() {
        val a = SparseVector(3)
        a[0] = 1.0
        a[1] = 0.0
        a[2] = 3.0
        assertEquals(2, a.indices().size)

        val b = SparseVector(3)
        b[0] = 0.0
        b[1] = 5.0
        b[2] = 0.0
        assertEquals(1, b.indices().size)

        val c = a + b
        assertEquals(1.0, c[0])
        assertEquals(5.0, c[1])
        assertEquals(3.0, c[2])
        assertEquals(3, c.indices().size)

        val d = SparseVector(3)
        d[0] = -1.0
        d[1] = 0.0
        d[2] = -3.0

        val e = d + a
        assertEquals(0.0, e[0])
        assertEquals(0.0, e[1])
        assertEquals(0.0, e[2])
        assertEquals(0, e.indices().size)
    }
}