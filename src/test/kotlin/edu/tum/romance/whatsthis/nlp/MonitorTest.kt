package edu.tum.romance.whatsthis.nlp

import edu.tum.romance.whatsthis.io.TextData
import edu.tum.romance.whatsthis.math.EuclideanDistance
import edu.tum.romance.whatsthis.math.Vector
import kotlin.test.*

internal class MonitorTest {

    @Test
    fun testClear(){
        Monitor.clear()
        assertEquals(0, Monitor.vocabulary.size, "Vocabulary size does not match!")
        assertTrue( Monitor.cloudKeys().isEmpty() , "Clouds size does not match!" )
    }

    @Test
    fun testAdd(){
        Monitor.clear()
        val simpleSample = TextData("this is a simple sample")
        Monitor.add("Simple Sample", simpleSample, "Example")

        assertEquals(5, Monitor.vocabulary.size, "Vocabulary size does not match!")
        assertEquals(1, Monitor.cloudKeys().size, "Clouds size does not match!")
        assertEquals(1, Monitor["Example"]!!.size, "Cloud 'Example' size does not match!")
        assertEquals(0, Monitor.unclassified().size, "Unclassified size does not match!")
        assertEquals("Simple Sample", Monitor["Example"]!![0], "Cloud 'Example' does not match!")

        val shortSample = TextData("this is a short sample")
        Monitor.add("Short Sample", shortSample)

        assertEquals(6, Monitor.vocabulary.size, "Vocabulary size does not match!")
        assertEquals(1, Monitor.cloudKeys().size, "Clouds size does not match!")
        assertEquals(1, Monitor["Example"]!!.size, "Cloud 'Example' size does not match!")
        assertEquals(1, Monitor.unclassified().size, "Unclassified size does not match!")

        val longSample = TextData("this is a long sample")
        val longSample2 = TextData("this is a long sample")

        Monitor.add("Long Sample", longSample, "Example")
        Monitor.add("Long Sample 2", longSample2)

        assertEquals(7, Monitor.vocabulary.size, "Vocabulary size does not match!")
        assertEquals(1, Monitor.cloudKeys().size, "Clouds size does not match!")
        assertEquals(2, Monitor["Example"]!!.size, "Cloud 'Example' size does not match!")
        assertEquals(2, Monitor.unclassified().size, "Unclassified size does not match!")

    }

    @Test
    fun testAssignRevoke(){
        Monitor.clear()
        val shortSample = TextData("this is a short sample")
        val longSample = TextData("this is a long sample")

        Monitor.add("Short Sample", shortSample, "Example")
        Monitor.add("Long Sample", longSample, "Example")

        Monitor.assign("Long Sample", "Example 2")
        assertEquals(1, Monitor["Example"]!!.size, "Cloud 'Example' size does not match!")
        assertEquals(1, Monitor["Example 2"]!!.size, "Cloud 'Example 2' size does not match!")
        assertEquals(0, Monitor.unclassified().size, "Unclassified size does not match!")

        Monitor.revoke("Long Sample", "Example 2")
        assertEquals(1, Monitor["Example"]!!.size, "Cloud 'Example' size does not match!")
        assertEquals(0, Monitor["Example 2"]!!.size, "Cloud 'Example 2' size does not match!")
        assertEquals(1, Monitor.unclassified().size, "Unclassified size does not match!")

        Monitor.assign("Long Sample", "Example")
        assertEquals(2, Monitor["Example"]!!.size, "Cloud 'Example' size does not match!")
        assertEquals(0, Monitor["Example 2"]!!.size, "Cloud 'Example 2' size does not match!")
        assertEquals(0, Monitor.unclassified().size, "Unclassified size does not match!")
    }

    @Test
    fun cloudManipulation(){
        Monitor.clear()
        val simpleSample = TextData("this is a simple sample")
        val shortSample = TextData("this is a short sample")
        val longSample = TextData("this is a long sample")

        Monitor.add("Simple Sample", simpleSample, "Example")
        Monitor.add("Short Sample", shortSample, "Example")
        Monitor.add("Long Sample", longSample, "Example")

        val splineMath =
            TextData("""This is an excerpt from the 'Cubic Spline'-Wikipedia page {\displaystyle p(t) = 2t^3 - 3t^2 + 1}""")
        val interpolationMath =
            TextData("""Generally, linear interpolation takes two data points, say {\displaystyle (x_a,y_a)} and {\displaystyle (x_b,y_b)}, and the interpolant is given by: {\displaystyle y = y_a + (y_b - y_a)\frac{x-x_a}{x_b-x_a}} at the point {\displaystyle (x,y)}""")

        Monitor.add("Cubic Spline", splineMath, "Math")
        Monitor.add("Interpolation", interpolationMath, "Math")

        assertEquals(2, Monitor.cloudKeys().size, "Clouds size does not match!")
        assertEquals(2, Monitor["Math"]!!.size, "Cloud 'Math' size does not match!")
        assertEquals(3, Monitor["Example"]!!.size, "Cloud 'Example' size does not match!")
        assertTrue(Monitor["Math 2"] == null, "Cloud 'Math 2' is not null!")
        assertTrue(Monitor["Math 3"] == null, "Cloud 'Math 2' is not null!")

        Monitor.createCloud("Math 2")

        assertEquals(3, Monitor.cloudKeys().size, "Clouds size does not match!")
        assertEquals(2, Monitor["Math"]!!.size, "Cloud 'Math' size does not match!")
        assertEquals(3, Monitor["Example"]!!.size, "Cloud 'Example' size does not match!")
        assertEquals(0, Monitor["Math 2"]!!.size, "Cloud 'Math 2' size does not match!")
        assertTrue(Monitor["Math 3"] == null, "Cloud 'Math 2' is not null!")

        Monitor.renameCloud("Math 2", "Math 3")

        assertEquals(3, Monitor.cloudKeys().size, "Clouds size does not match!")
        assertEquals(2, Monitor["Math"]!!.size, "Cloud 'Math' size does not match!")
        assertEquals(3, Monitor["Example"]!!.size, "Cloud 'Example' size does not match!")
        assertEquals(0, Monitor["Math 3"]!!.size, "Cloud 'Math 2' size does not match!")

    }

    @Test
    fun testSimpleFunctions(){
        Monitor.clear()

        assertEquals(0, Monitor.cloudKeys().size, "Clouds are not empty!")
        assertEquals(0, Monitor.vocabulary.size, "Vocabulary is not empty!")
        assertEquals(0, Monitor.unclassified().size, "Unclassified is not empty!")

        assertTrue(Monitor.isEmpty(), "Monitor is not empty!")

        val simpleSample = TextData("this is a simple sample")
        Monitor.add("Simple Sample", simpleSample, "Example")

        assertFalse(Monitor.isEmpty(), "Monitor is empty!")
        assertTrue(Monitor.isClassified("Simple Sample"), "Sample is not classified!")

        Monitor.revoke("Simple Sample", "Example")
        assertFalse(Monitor.isEmpty(), "Monitor is empty!")
        assertFalse(Monitor.isClassified("Simple Sample"), "Sample is classified!")
    }


    @Test
    fun testCloudSignificance() {
        Monitor.clear()
        val simpleSample = TextData("this is a simple sample")
        val shortSample = TextData("this is a short sample")
        val longSample = TextData("this is a long sample")

        Monitor.add("Simple Sample", simpleSample, "Example")
        Monitor.add("Short Sample", shortSample, "Example")
        Monitor.add("Long Sample", longSample, "Example")

        val splineMath =
            TextData("""This is an excerpt from the 'Cubic Spline'-Wikipedia page {\displaystyle p(t) = 2t^3 - 3t^2 + 1}""")
        val interpolationMath =
            TextData("""Generally, linear interpolation takes two data points, say {\displaystyle (x_a,y_a)} and {\displaystyle (x_b,y_b)}, and the interpolant is given by: {\displaystyle y = y_a + (y_b - y_a)\frac{x-x_a}{x_b-x_a}} at the point {\displaystyle (x,y)}""")

        Monitor.add("Cubic Spline", splineMath, "Math")
        Monitor.add("Interpolation", interpolationMath, "Math")

        val v = listOf(
            "this", "is", "CHAR", "simple", "sample", "short", "long", "an", "excerpt",
            "from", "the", "cubic", "spline-wikipedia", "page", "MATH", "generally",
            "linear", "interpolation", "takes", "two", "data", "points", "say", "and",
            "interpolant", "given", "by", "at", "point"
        )
        assert(v.size == v.toSet().size) { "Vocabulary contains duplicates, please check the tests!" }
        val vocabulary = Monitor.vocabulary
        assertEquals(v.size, vocabulary.size, "Vocabulary size does not match!")
        assertEquals(v, vocabulary.toList(), "Vocabulary does not match!")

        val knownClouds = listOf("Example", "Math")
        assert(knownClouds.size == knownClouds.toSet().size) { "Clouds contain duplicates, please check the tests!" }
        val clouds = Monitor.cloudKeys()
        assertEquals(knownClouds.size, clouds.size, "Clouds size does not match!")
        assertEquals(knownClouds.toSet(), clouds, "Clouds do not match!")
        assertEquals(3, Monitor[knownClouds[0]]!!.size, "Cloud '${knownClouds[0]}' size does not match!")
        assertEquals(2, Monitor[knownClouds[1]]!!.size, "Cloud '${knownClouds[1]}' size does not match!")

        val sampleSummary = Vector(arrayOf(3, 3, 3, 1, 3, 1, 1))
        sampleSummary.growTo(vocabulary.size)
        sampleSummary.unit(EuclideanDistance)
        assertContentEquals(sampleSummary.data, Monitor.summary("Example").data, "Summary of 'Example' does not match!")

        val mathSummary = Vector(vocabulary.size)
        mathSummary.set(
            v["this"] to 1.0, v["is"] to 2.0, v["an"] to 1.0,
            v["excerpt"] to 1.0, v["from"] to 1.0, v["the"] to 3.0,
            v["cubic"] to 1.0, v["spline-wikipedia"] to 1.0,
            v["page"] to 1.0, v["MATH"] to 5.0,
            v["generally"] to 1.0, v["linear"] to 1.0,
            v["interpolation"] to 1.0, v["takes"] to 1.0,
            v["two"] to 1.0, v["data"] to 1.0,
            v["points"] to 1.0, v["say"] to 1.0,
            v["and"] to 2.0, v["interpolant"] to 1.0,
            v["given"] to 1.0, v["by"] to 1.0,
            v["at"] to 1.0, v["point"] to 1.0
        )
        mathSummary.unit(EuclideanDistance)
        assertContentEquals(mathSummary.data, Monitor.summary("Math").data, "Summary of 'Math' does not match!")

        val sampleSignificance = Vector(vocabulary.size)
        var significance = { it: String ->
            sampleSummary[v[it]] / (mathSummary[v[it]] + sampleSummary[v[it]])
        }
        for (i in 0 until sampleSignificance.size) {
            sampleSignificance[i] = significance(v[i])
        }
        sampleSignificance.unit(EuclideanDistance)
        val actualSampleSignificance = Monitor.significance("Example").data
        assertContentEquals(
            sampleSignificance.data,
            actualSampleSignificance,
            "Significance of 'Simple Sample' does not match!"
        )

        val mathSignificance = Vector(Array(vocabulary.size) { 1 })
        significance = { it: String ->
            mathSummary[v[it]] / (mathSummary[v[it]] + sampleSummary[v[it]])
        }
        for (i in 0 until mathSignificance.size) {
            mathSignificance[i] = significance(v[i])
        }
        mathSignificance.unit(EuclideanDistance)
        assertContentEquals(mathSignificance.data, Monitor.significance("Math").data, "Significance of 'Cubic Spline' does not match!")

        // Test edge cases (empty clouds)
        assertTrue(Monitor.emptyVector.same(Monitor.summary("Example_2")), "Summary of 'Example 2' does not match!")
        assertTrue(Monitor.emptyVector.same(Monitor.significance("Example_2")), "Significance of 'Example 2' does not match!")

        Monitor.createCloud("Example_3")
        assertTrue(Monitor.emptyVector.same(Monitor.summary("Example_3")), "Significance of 'Example 3' does not match!")
        assertTrue(Monitor.emptyVector.same(Monitor.significance("Example_3")), "Significance of 'Example 3' does not match!")
    }
}

/**
 * Short-cut for 'indexOf'.
 * I dislike long lines, deal with it.
 */
private operator fun List<String>.get(value: String) = indexOf(value)