package edu.tum.romance.whatsthis.math

import kotlin.math.sqrt
import kotlin.test.Test
import kotlin.test.assertEquals
class VecCloudTest {
    @Test
    fun clear() {
        val vecCloud = VecCloud(listOf())
        assertEquals(0, vecCloud.cloud.size, "VecCloud should be empty")
        vecCloud.cloud = listOf(IntVec(listOf(1, 2, 3)), IntVec(listOf(1, 2, 9)))
        assertEquals(2, vecCloud.cloud.size, "VecCloud should contain 2 vectors")
        vecCloud.clear()
        assertEquals(0, vecCloud.cloud.size, "VecCloud should be empty")
    }

    @Test
    fun add(){
        val vecCloud = VecCloud(listOf())
        assertEquals(0, vecCloud.cloud.size, "VecCloud should be empty")
        vecCloud + IntVec(listOf(1, 2, 3))
        assertEquals(1, vecCloud.cloud.size, "VecCloud should contain 1 vector")
        vecCloud + IntVec(listOf(1, 2, 9))
        assertEquals(2, vecCloud.cloud.size, "VecCloud should contain 2 vectors")
    }

    @Test
    fun closestDistance(){
        val vecCloud = VecCloud(listOf(IntVec(listOf(1,1,1)), IntVec(listOf(2,4,1)),
                                       IntVec(listOf(3,0,0)), IntVec(listOf(4,90,3))))
        assertEquals(0.0, vecCloud.closestDistance(IntVec(listOf(1,1,1))), "Closest distance should be 0")
        assertEquals(1.0, vecCloud.closestDistance(IntVec(listOf(1,1,2))), "Closest distance should be 1")
        assertEquals(sqrt(2.0), vecCloud.closestDistance(IntVec(listOf(1,2,2))), "Closest distance should be sqrt(2)")
        assertEquals(sqrt(105.0), vecCloud.closestDistance(IntVec(listOf(5,80, 5))),  "Closest distance should be sqrt(105)")

        assertEquals( 0.0, vecCloud.closestDistance(IntVec(listOf(1,1,1)), ManhattanDistance), "Closest distance should be 0")
        assertEquals( 2.0, vecCloud.closestDistance(IntVec(listOf(1,2,2)), ManhattanDistance), "Closest distance should be 2")
        assertEquals( 40.0, vecCloud.closestDistance(IntVec(listOf(-16, 80, 13)), ManhattanDistance),  "Closest distance should be 40")
    }
}