package edu.tum.romance.whatsthis.nlp

import edu.tum.romance.whatsthis.io.TextData
import edu.tum.romance.whatsthis.math.EuclideanDistance
import edu.tum.romance.whatsthis.math.Vector
import kotlin.test.*


internal class APITest {
    @Test
    fun testCloudSignificance() {
        val simpleSample = TextData("this is a simple sample", "Simple Sample")
        val shortSample = TextData("this is a short sample", "Short Sample")
        val longSample = TextData("this is a long sample", "Long Sample")

        API.addSample(simpleSample, "Example")
        API.addSample(shortSample, "Example")
        API.addSample(longSample, "Example")
        assertEquals(3, API.vectors.size())
        assertEquals(7, API.vocabulary.size())

        val splineMath =
            TextData("""This is an excerpt from the 'Cubic Spline'-Wikipedia page {\displaystyle p(t) = 2t^3 - 3t^2 + 1}""", "Cubic Spline")
        val interpolationMath =
            TextData("""Generally, linear interpolation takes two data points, say {\displaystyle (x_a,y_a)} and {\displaystyle (x_b,y_b)}, and the interpolant is given by: {\displaystyle y = y_a + (y_b - y_a)\frac{x-x_a}{x_b-x_a}} at the point {\displaystyle (x,y)}""", "Interpolation")

        API.addSample(splineMath, "Math")
        API.addSample(interpolationMath, "Math")

        val v = listOf(
            "this", "is", "CHAR", "simple", "sample", "short", "long", "an", "excerpt",
            "from", "the", "cubic", "spline-wikipedia", "page", "MATH", "generally",
            "linear", "interpolation", "takes", "two", "data", "points", "say", "and",
            "interpolant", "given", "by", "at", "point"
        )
        assert(v.size == v.toSet().size) { "Vocabulary contains duplicates, please check the tests!" }
        assertEquals(v.size, API.vocabulary.size(), "Vocabulary size does not match!")
        assertEquals(v, API.vocabulary.words(), "Vocabulary does not match!")

        val knownClouds = listOf("Example", "Math")
        assert(knownClouds.size == knownClouds.toSet().size) { "Vector Spaces contain duplicates, please check the tests!" }
        val clouds = API.spaces()
        assertEquals(knownClouds.size, clouds.size, "Vector Space size does not match!")
        assertEquals(knownClouds, clouds, "Vector Spaces do not match!")
        assertEquals(3, API.spaces[knownClouds[0]]!!.size(), "Vector Space '${knownClouds[0]}' size does not match!")
        assertEquals(2, API.spaces[knownClouds[1]]!!.size(), "Vector Space '${knownClouds[1]}' size does not match!")

        var sampleSummary = Vector(arrayOf(3, 3, 3, 1, 3, 1, 1))
        sampleSummary.growTo(API.vocabulary.size())
        sampleSummary = sampleSummary.unit(EuclideanDistance)
        assertContentEquals(sampleSummary.data, API.spaces["Example"]!!.summary(EuclideanDistance).data, "Summary of 'Example' does not match!")

        val mathSummary = Vector(API.vocabulary.size())
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
        assertContentEquals(mathSummary.data, API.spaces["Math"]!!.summary(EuclideanDistance).data, "Summary of 'Math' does not match!")

        val sampleSignificance = Vector(API.vocabulary.size())
        var significance = { it: String ->
            sampleSummary[v[it]] / (mathSummary[v[it]] + sampleSummary[v[it]])
        }
        for (i in 0 until sampleSignificance.size()) {
            sampleSignificance[i] = significance(v[i])
        }
        sampleSignificance.unit(EuclideanDistance)
        val actualSampleSignificance = API.spaces.significance("Example")!!.data
        assertContentEquals(
            sampleSignificance.data,
            actualSampleSignificance,
            "Significance of 'Simple Sample' does not match!"
        )

        val mathSignificance = Vector(Array(API.vocabulary.size()) { 1 })
        significance = { it: String ->
            mathSummary[v[it]] / (mathSummary[v[it]] + sampleSummary[v[it]])
        }
        for (i in 0 until mathSignificance.size()) {
            mathSignificance[i] = significance(v[i])
        }
        mathSignificance.unit(EuclideanDistance)
        val actualMathSignificance = API.spaces.significance("Math")!!.data
        assertContentEquals(
            mathSignificance.data,
            actualMathSignificance,
            "Significance of 'Math' does not match!"
        )
    }
}

/**
 * Short-cut for 'indexOf'.
 * I dislike long lines, deal with it.
 */
private operator fun List<String>.get(value: String) = indexOf(value)