package edu.tum.romance.whatsthis.nlp

import edu.tum.romance.whatsthis.io.TextData
import edu.tum.romance.whatsthis.math.EuclideanDistance

@Suppress("unused")
object API {
    var norm = EuclideanDistance
    val spaces = VectorSpaceManager
    val vectors = WordVectorManager
    val vocabulary = VocabularyManager

    fun isEmpty(): Boolean {
        return vocabulary.isEmpty() && vectors.isEmpty() && spaces.isEmpty()
    }

    fun spaces(): List<String> {
        return spaces.spaces()
    }

    fun vectors() = vectors.names()

    fun spaceVectors(name: String?): List<String> {
        return if(name != null) {
            spaces[name]?.let { space -> space.vectors().map { WordVectorManager.name(it) } } ?: emptyList()
        } else {
            spaces.unclassified().map { WordVectorManager.name(it) }
        }
    }

    fun alterSpace(space: String, rename: String? = null) {
        if(rename != null) {
            if(space in spaces) {
                spaces[rename] = spaces[space]!!
                spaces -= space
            } else {
                spaces += rename
            }
        } else if(space !in spaces) {
            spaces += space
        }
    }

    fun addSample(sample: TextData<*>, space: String? = null) {
        /* Register new Vocabulary */
        for((word, _) in sample.tokens) {
            if(word !in vocabulary) {
                vocabulary += word
            }
        }
        /* Create Text Vector */
        val vector = VocabularyManager.emptyVector()
        for((word, count) in sample.tokens) {
            vector[vocabulary[word]] = count
        }
        sample.vector = vector
        /* Grow remaining vectors to vocabulary size */
        for(ref in vectors.names()) {
            vectors[ref]!!.vector!!.growTo(vector.size())
        }
        /* Register new Vector */
        val ref: Int
        if(sample in vectors) {
            vectors[sample.text] = sample
            ref = vectors.ref(sample.text)
        } else {
            ref = vectors.size()
            vectors += sample
        }
        /* Assign the Vector to a VectorSpace */
        when (space) {
            null -> spaces.unclassified(ref)
            in spaces -> spaces[space]!! += ref
            else -> {
                spaces += space
                spaces[space]!! += ref
            }
        }
        /* Recalculate Summaries & Significance if a Space has been altered */
        if(space != null) {
            recalculateSignificance()
        }
    }

    private fun recalculateSignificance() {
        val summaries = spaces.spaces().map { spaces[it]!!.name to spaces[it]!!.summary(norm) }
        val total = summaries.map { it.second }.reduce { a, b -> a + b }
        for((name, summary) in summaries) {
            val result = total.clone()
            for(i in 0 until result.size()) {
                result[i] = summary[i] / result[i]
            }
            spaces[name] = result.unit(norm)
        }
    }

    fun renameSample(old: String, new: String) {
        if(old in vectors) {
            vectors[new] = vectors[old]!!
        }
    }

    fun resample(name: String, space: String? = null) {
        val sample = vectors[name]
        if(sample != null) {
            addSample(sample, space)
        }
        // TODO: Log an error
    }

    fun clear() {
        spaces.clear()
        vectors.clear()
        vocabulary.clear()
    }
}