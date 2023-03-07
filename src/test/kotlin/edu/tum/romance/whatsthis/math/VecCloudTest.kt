package edu.tum.romance.whatsthis.math

import edu.tum.romance.whatsthis.io.TextData
import edu.tum.romance.whatsthis.nlp.Monitor
import kotlin.test.Test
import kotlin.test.assertEquals

class VecCloudTest {
    @Test
    fun clear() {
        val vecCloud = VecCloud()
        vecCloud + TextData("")
        assertEquals(1, vecCloud.cloud.size, "VecCloud should contain 1 vector")
        vecCloud.clear()
        assertEquals(0, vecCloud.cloud.size, "VecCloud should be empty")
    }

    @Test
    fun add(){
        val vecCloud = VecCloud(listOf())
        assertEquals(0, vecCloud.cloud.size, "VecCloud should be empty")
        vecCloud + TextData("a")
        assertEquals(1, vecCloud.cloud.size, "VecCloud should contain 1 vector")
        vecCloud + TextData("b")
        assertEquals(2, vecCloud.cloud.size, "VecCloud should contain 2 vectors")
    }

    @Test
    fun closestDistance() {
        Monitor.add(TextData("Some math expression"), "Math")
        Monitor.add(TextData("Other math expression"), "Math")
        val testData = TextData("Some other math expression")

        /* Add Test Data, so that the Monitor assigns it a WordVec */
        Monitor.add(testData, "Math")
        val cloud = Monitor["Math"]!!
        assertEquals(3, cloud.cloud.size, "VecCloud should contain 3 vectors")
        Monitor.remove(testData, "Math")

        assertEquals(2, cloud.cloud.size, "VecCloud should contain 2 vectors")

        assertEquals(0.0, cloud.closestDistance(cloud.cloud[0]), "Distance should be 0.0")
        assertEquals(1.0, cloud.closestDistance(testData), "Distance should be 1.0")
    }
}