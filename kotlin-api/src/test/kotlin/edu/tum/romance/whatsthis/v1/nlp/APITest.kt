package edu.tum.romance.whatsthis.v1.nlp

import edu.tum.romance.whatsthis.v1.data.TextData
import edu.tum.romance.whatsthis.v1.math.Distance
import edu.tum.romance.whatsthis.v1.math.vec.SparseVector
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.TestMethodOrder
import kotlin.test.*

@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
internal class APITest {
    @BeforeTest
    fun clean() {
        API.clear()
    }

    @Test
    @Order(1)
    fun testAddSingleSample() {
        API.addSample(TextData("this is a simple sample", "Simple Sample"), "Example")
        assertEquals(1, API.vectors.size(), "There should be one Vector!")
        assertEquals(5, API.vocabulary.size(), "Vocabulary size does not match!")
        assertEquals(1, API.spaces().size, "There should be one Vector Space!")
        assertTrue("Example" in API.spaces(), "Vector Space 'Example' does not exist!")
        assertEquals(1, API.spaces["Example"]!!.size(), "There should be one Vector in Vector Space 'Example'!")
        assertNotNull(API.vectors["Simple Sample"]!!.vector, "Vector 'Simple Sample' does not exist!")
        assertEquals(5, API.vectors["Simple Sample"]!!.vector!!.size(), "Vector 'Simple Sample' size does not match!")
        (0 until 5).forEach { assertEquals(1.0, API.vectors["Simple Sample"]!!.vector!![it], "Vector 'Simple Sample' does not match!") }

        val vec = SparseVector(arrayOf(1, 1, 1, 1, 1)
            .mapIndexed { index, value -> index to value.toDouble() }.toMap()).unit()
        assertEquals(vec, API.spaces["Example"]!!.summary(Distance.Implementation), "Summary of 'Example' does not match!")
        assertEquals(vec, API.spaces.significance("Example"), "Significance of 'Example' does not match!")
    }

    @Test
    @Order(2)
    fun testTwoSamples() {
        API.addSample(TextData("this is a simple sample", "Simple Sample"), "Example")
        assertEquals(1, API.vectors.size(), "There should be one Vector!")
        assertEquals(5, API.vocabulary.size(), "Vocabulary size does not match!")
        assertEquals(1, API.spaces().size, "There should be one Vector Space!")
        assertTrue("Example" in API.spaces(), "Vector Space 'Example' does not exist!")
        assertEquals(1, API.spaces["Example"]!!.size(), "There should be one Vector in Vector Space 'Example'!")
        assertNotNull(API.vectors["Simple Sample"]!!.vector, "Vector 'Simple Sample' does not exist!")
        assertEquals(5, API.vectors["Simple Sample"]!!.vector!!.size(), "Vector 'Simple Sample' size does not match!")
        (0 until 5).forEach { assertEquals(1.0, API.vectors["Simple Sample"]!!.vector!![it], "Vector 'Simple Sample' has been altered!") }

        API.addSample(TextData("this is a short sample", "Short Sample"), "Example")
        assertEquals(2, API.vectors.size(), "There should be two Vectors!")
        assertEquals(6, API.vocabulary.size(), "Vocabulary size does not match!")
        assertEquals(1, API.spaces().size, "There should be one Vector Space!")
        assertTrue("Example" in API.spaces(), "Vector Space 'Example' does not exist!")
        assertEquals(2, API.spaces["Example"]!!.size(), "There should be two Vectors in Vector Space 'Example'!")
        assertNotNull(API.vectors["Simple Sample"]!!.vector, "Vector 'Simple Sample' does not exist!")
        assertEquals(6, API.vectors["Simple Sample"]!!.vector!!.size(), "Vector 'Simple Sample' size does not match!")
        (0 until 5).forEach { assertEquals(1.0, API.vectors["Simple Sample"]!!.vector!![it], "Vector 'Simple Sample' has been altered!") }
        assertEquals(0.0, API.vectors["Simple Sample"]!!.vector!![5], "Vector 'Simple Sample' does not match!")
        assertNotNull(API.vectors["Short Sample"]!!.vector, "Vector 'Short Sample' does not exist!")
        assertEquals(6, API.vectors["Short Sample"]!!.vector!!.size(), "Vector 'Short Sample' size does not match!")
        (0 until 6).forEach {
            val expected = if (it == 3) 0.0 else 1.0
            assertEquals(expected, API.vectors["Short Sample"]!!.vector!![it], "Vector 'Short Sample' does not match!")
        }

        val vec = SparseVector(arrayOf(2, 2, 2, 1, 2, 1)
            .mapIndexed { index, value -> index to value.toDouble() }.toMap()).unit()
        val unit = SparseVector(arrayOf(1, 1, 1, 1, 1, 1)
            .mapIndexed { index, value -> index to value.toDouble() }.toMap()).unit()
        assertEquals(vec, API.spaces["Example"]!!.summary(Distance.Implementation), "Summary of 'Example' does not match!")
        assertEquals(unit, API.spaces.significance("Example"), "Significance of 'Example' does not match!")
    }

    @Test
    @Order(3)
    fun testTwoSpaces() {
        val simpleSample = TextData("this is a simple sample", "Simple Sample")
        val splineSample = TextData(
            """This is an excerpt from the 'Cubic Spline'-Wikipedia page {\displaystyle p(t) = 2t^3 - 3t^2 + 1}""",
            "Cubic Spline"
        )
        API.addSample(simpleSample, "Example")
        API.addSample(splineSample, "Math")
        assertEquals(2, API.vectors.size(), "There should be two Vectors!")
        assertEquals(13, API.vocabulary.size(), "Vocabulary size does not match!")
        assertEquals(2, API.spaces().size, "There should be two Vector Spaces!")
        assertTrue("Example" in API.spaces(), "Vector Space 'Example' does not exist!")
        assertTrue("Math" in API.spaces(), "Vector Space 'Math' does not exist!")
        assertEquals(1, API.spaces["Example"]!!.size(), "There should be one Vector in Vector Space 'Example'!")
        assertEquals(1, API.spaces["Math"]!!.size(), "There should be one Vector in Vector Space 'Math'!")
        assertNotNull(API.vectors["Simple Sample"]!!.vector, "Vector 'Simple Sample' does not exist!")
        assertEquals(13, API.vectors["Simple Sample"]!!.vector!!.size(), "Vector 'Simple Sample' size does not match!")
        (0 until 5).forEach { assertEquals(1.0, API.vectors["Simple Sample"]!!.vector!![it], "Vector 'Simple Sample' does not match!") }
        (5 until 13).forEach { assertEquals(0.0, API.vectors["Simple Sample"]!!.vector!![it], "Vector 'Simple Sample' does not match!") }
        assertNotNull(API.vectors["Cubic Spline"]!!.vector, "Vector 'Cubic Spline' does not exist!")
        assertEquals(13, API.vectors["Cubic Spline"]!!.vector!!.size(), "Vector 'Cubic Spline' size does not match!")
        (0 until 2).forEach { assertEquals(1.0, API.vectors["Cubic Spline"]!!.vector!![it], "Vector 'Cubic Spline' does not match!") }
        (2 until 5).forEach { assertEquals(0.0, API.vectors["Cubic Spline"]!!.vector!![it], "Vector 'Cubic Spline' does not match!") }
        (5 until 13).forEach { assertEquals(1.0, API.vectors["Cubic Spline"]!!.vector!![it], "Vector 'Cubic Spline' does not match!") }

        assertEquals(
            API.vectors["Simple Sample"]!!.vector!!.clone().unit(), API.spaces["Example"]!!.summary(
            Distance.Implementation), "Summary of 'Example' does not match!")
        assertEquals(
            API.vectors["Cubic Spline"]!!.vector!!.clone().unit(), API.spaces["Math"]!!.summary(
            Distance.Implementation), "Summary of 'Math' does not match!")
    }

    @Test
    @Order(4)
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

        var sampleSummary = SparseVector(arrayOf(3, 3, 3, 1, 3, 1, 1)
            .mapIndexed { i, value -> i to value.toDouble() }.toMap())
        sampleSummary.growTo(API.vocabulary.size())
        sampleSummary = sampleSummary.unit()

        assertEquals(sampleSummary, API.spaces["Example"]!!.summary(Distance.Implementation), "Summary of 'Example' does not match!")

        var mathSummary = SparseVector(mutableMapOf(
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
        ), API.vocabulary.size())

        mathSummary = mathSummary.unit()

        assertEquals(mathSummary, API.spaces["Math"]!!.summary(Distance.Implementation), "Summary of 'Math' does not match!")

        var sampleSignificance = SparseVector(API.vocabulary.size())
        var significance = { it: String ->
            sampleSummary[v[it]] / (mathSummary[v[it]] + sampleSummary[v[it]])
        }
        for (i in 0 until sampleSignificance.size()) {
            sampleSignificance[i] = significance(v[i])
        }

        sampleSignificance = sampleSignificance.unit()
        val actualSampleSignificance = API.spaces.significance("Example")!!
        assertEquals(sampleSignificance, actualSampleSignificance, "Significance of 'Simple Sample' does not match!")

        var mathSignificance = SparseVector((0 until API.vocabulary.size()).associateWith { _ -> 1.0 })
        significance = { it: String ->
            mathSummary[v[it]] / (mathSummary[v[it]] + sampleSummary[v[it]])
        }
        for (i in 0 until mathSignificance.size()) {
            mathSignificance[i] = significance(v[i])
        }

        mathSignificance = mathSignificance.unit()
        val actualMathSignificance = API.spaces.significance("Math")!!
        assertEquals(mathSignificance, actualMathSignificance, "Significance of 'Math' does not match!")
    }

    @Test
    @Order(5)
    fun testReassignment() {
        val sample = TextData("this is a simple sample", "Simple Sample")
        API.addSample(sample, "Example")
        assertEquals(1, API.spaces["Example"]!!.size(), "Sample was not added to the vector space!")
        val vec = SparseVector(arrayOf(1, 1, 1, 1, 1)
            .mapIndexed { i, value -> i to value.toDouble() }.toMap()).unit()
        assertEquals(vec, API.spaces["Example"]!!.summary(Distance.Implementation), "Summary of 'Example' does not match!")
        assertFalse("Math" in API.spaces, "Math should not be in the vector spaces!")
        API.addSample(sample, "Math")
        assertTrue("Math" in API.spaces, "Math should now be in the vector spaces!")
        assertEquals(1, API.spaces["Math"]!!.size(), "Sample was not moved to the vector space 'Math'!")
        assertEquals(0, API.spaces["Example"]!!.size(), "Sample was not removed from the vector space 'Example'!")
        assertEquals(vec, API.spaces["Math"]!!.summary(Distance.Implementation), "Summary of 'Math' does not match!")
        assertEquals(SparseVector(0), API.spaces["Example"]!!.summary(Distance.Implementation), "Summary of 'Example' does not match!")
    }

    @Test
    @Order(6)
    fun testAssignmentFromVariable() {
        val sample = TextData("this is a simple sample", "Simple Sample")
        API.addSample(sample)
        assertEquals(0, API.spaces().size, "There should be no vector spaces!")
        assertEquals(1, API.spaces.unclassified().size, "There should be one unclassified sample!")
        API.addSample(sample, "Example")
        assertEquals(1, API.spaces().size, "There should be one vector space!")
        assertEquals(0, API.spaces.unclassified().size, "There should be no unclassified samples!")
        assertTrue("Example" in API.spaces, "Example should be in the vector spaces!")
        assertEquals(1, API.spaces["Example"]!!.size(), "Sample was not added to the vector space!")
    }

    @Test
    @Order(7)
    fun testAssignmentRevocation() {
        val sample = TextData("this is a simple sample", "Simple Sample")
        API.addSample(sample, "Example")
        assertEquals(1, API.spaces().size, "There should be one vector space!")
        assertEquals(0, API.spaces.unclassified().size, "There should be no unclassified samples!")
        assertTrue("Example" in API.spaces, "Example should be in the vector spaces!")
        assertEquals(1, API.spaces["Example"]!!.size(), "Sample was not added to the vector space!")
        API.addSample(sample)
        assertEquals(1, API.spaces().size, "There should be one vector space!")
        assertEquals(1, API.spaces.unclassified().size, "There should be one unclassified sample!")
        assertTrue("Example" in API.spaces, "Example should remain in the vector spaces!")
        assertEquals(0, API.spaces["Example"]!!.size(), "Sample was not removed from the vector space!")
    }

    @Test
    @Order(8)
    fun testSpaceRenaming() {
        val sample = TextData("this is a simple sample", "Simple Sample")
        API.addSample(sample, "Example")
        assertEquals(1, API.spaces().size, "There should be one vector space!")
        assertEquals(0, API.spaces.unclassified().size, "There should be no unclassified samples!")
        assertTrue("Example" in API.spaces, "Example should be in the vector spaces!")
        assertEquals(1, API.spaces["Example"]!!.size(), "Sample was not added to the vector space!")
        API.alterSpace("Example", "Math")
        assertEquals(1, API.spaces().size, "There should be one vector space!")
        assertEquals(0, API.spaces.unclassified().size, "There should be no unclassified samples!")
        assertTrue("Math" in API.spaces, "Math should be in the vector spaces!")
        assertEquals(1, API.spaces["Math"]!!.size(), "Sample was not added to the vector space!")
        assertFalse("Example" in API.spaces, "Example should not be in the vector spaces!")
    }

    @Test
    @Order(9)
    fun testBasicSpaceDeletion() {
        val sample = TextData("this is a simple sample", "Simple Sample")
        API.addSample(sample, "Example")
        assertEquals(1, API.spaces().size, "There should be one vector space!")
        assertEquals(0, API.spaces.unclassified().size, "There should be no unclassified samples!")
        assertTrue("Example" in API.spaces, "Example should be in the vector spaces!")
        assertEquals(1, API.spaces["Example"]!!.size(), "Sample was not added to the vector space!")
        API.deleteSpace("Example")
        assertEquals(0, API.spaces().size, "There should be no vector spaces!")
        assertEquals(1, API.spaces.unclassified().size, "There should be one unclassified sample!")
        assertFalse("Example" in API.spaces, "Example should not be in the vector spaces!")
        assertSame(sample, API.vectors[API.spaces.unclassified()[0]], "Sample was not moved to the unclassified samples!")
    }

    @Test
    @Order(10)
    fun testBasicSampleDeletion() {
        val sample = TextData("this is a simple sample", "Simple Sample")
        API.addSample(sample, "Example")
        assertEquals(1, API.spaces().size, "There should be one vector space!")
        assertEquals(0, API.spaces.unclassified().size, "There should be no unclassified samples!")
        assertTrue("Example" in API.spaces, "Example should be in the vector spaces!")
        assertEquals(1, API.spaces["Example"]!!.size(), "Sample was not added to the vector space!")
        API.deleteSample("Simple Sample")
        assertEquals(1, API.spaces().size, "There should be one vector space!")
        assertEquals(0, API.spaces.unclassified().size, "There should be no unclassified samples!")
        assertTrue("Example" in API.spaces, "Example should be in the vector spaces!")
        assertEquals(0, API.spaces["Example"]!!.size(), "Sample was not removed from the vector space!")
    }
}

/**
 * Short-cut for 'indexOf'.
 * I dislike long lines, deal with it.
 */
private operator fun List<String>.get(value: String) = indexOf(value)