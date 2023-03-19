package edu.tum.romance.whatsthis.nlp

import edu.tum.romance.whatsthis.data.TextData
import edu.tum.romance.whatsthis.math.Distance
import org.apache.logging.log4j.LogManager

@Suppress("unused")
object API {
    private var norm = Distance.Euclidean
    private var logger = LogManager.getLogger(API::class.java)
    internal val spaces = VectorSpaceManager
    internal val vectors = WordVectorManager
    internal val vocabulary = VocabularyManager

    /**
     * Create a sample in the API.
     *
     * @param sample TextData object containing the sample
     * @param space Name of the space to assign the sample to
     * or null to assign it to create a variable sample.
     */
    fun addSample(sample: TextData<*>, space: String? = null) {
        /* Register new Vocabulary */
        for((word, _) in sample.tokens) {
            if(word !in vocabulary) {
                vocabulary += word
            }
        }
        /* Create Text Vector */
        val vector = vocabulary.emptyVector()
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
        if(sample.name in vectors.names()) {
            vectors[sample.name] = sample
            ref = vectors.ref(sample.name)
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
        /* Recalculate Summaries & Significance */
        recalculateSignificance()
    }

    /**
     * Get a sample from the API.
     *
     * @param name Name of the sample to get
     *
     * @return TextData object containing the sample, if it exists.
     */
    fun getSample(name: String): TextData<*>? = vectors[name]

    /**
     * Rename a sample in the API.
     *
     * @param old Old name of the sample
     * @param new New name of the sample
     */
    fun renameSample(old: String, new: String) {
        if(old in vectors) {
            vectors.rename(old, new)
        }
    }

    /**
     * Delete a sample from the API.
     *
     * @param name Name of the sample to delete
     */
    fun deleteSample(name: String) {
        val ref = vectors.ref(name)
        if(ref == -1) {
            return
        }

        if(ref in spaces.unclassified()) {
            spaces.removeUnclassified(ref)
        }
        vectors -= vectors[name]!!
        spaces.deleteRef(ref)
        recalculateSignificance()
    }

    /**
     * Reassign a sample in the API.
     *
     * @param name Name of the sample to reassign
     * @param space Name of the space to assign the sample to
     * or null to assign it to create a variable sample.
     */
    fun resample(name: String, space: String? = null) {
        val sample = vectors[name]
        if(sample != null) {
            val ref = vectors.ref(name)
            if(space == null) {
                if(ref !in spaces.unclassified()) {
                    spaces.unclassified(ref)
                }
            } else {
                val spaceObj = spaces[space]
                if(spaceObj != null) {
                    spaceObj += ref
                } else {
                    spaces += space
                    spaces[space]!! += ref
                }
            }
        }
        logger.error("Sample $name does not exist! Please verify your front-end code.")
    }

    /**
     * Retrieve all vectors of the API, which are assigned
     * to some space.
     * Useful if you want to display unfiltered classified
     * vectors in a UI.
     *
     * @return List of vector names
     */
    fun classified(): List<String> {
        return vectors.names().filter { WordVectorManager.ref(it) !in spaces.unclassified() }
    }

    /**
     * Retrieve all vectors of the API, which are assigned
     * to a specific space or to no space at all.
     *
     * @param name Name of the space to retrieve vectors from
     * or null to retrieve all unclassified vectors
     * @return List of vector names
     */
    fun spaceVectors(name: String? = null): List<String> {
        return if(name != null) {
            spaces[name]?.let { space -> space.vectors().map { vectors.name(it) } } ?: emptyList()
        } else {
            spaces.unclassified().map { vectors.name(it) }
        }
    }

    /**
     * Retrieve all space names of the API as a list of strings.
     * Useful for displaying the available spaces in a GUI.
     *
     * @return List of space names
     */
    fun spaces(): List<String> {
        return spaces.spaces()
    }

    /**
     * Create or rename a space in the API.
     * This function is useful to create empty spaces
     * or to rename existing spaces and is thus reserved
     * for UI use.
     *
     * @param space Name of the space to create or rename
     * @param rename New name of the space, or null to keep the old name
     */
    fun alterSpace(space: String, rename: String? = null): Int {
        if(rename != null) {
            return if(space in spaces) {
                spaces.rename(space, rename)
                2
            } else {
                spaces += rename
                1
            }
        } else if(space !in spaces) {
            spaces += space
            return 1
        }
        return 0
    }

    /**
     * Delete a space from the API.
     *
     * @param space Name of the space to delete
     */
    fun deleteSpace(space: String) {
        if(space !in spaces) {
            return
        }
        for(ref in spaces[space]!!.vectors()) {
            spaces.unclassified(ref)
        }
        spaces -= space
        recalculateSignificance()
    }

    /**
     * Recalculate the significance of all spaces,
     * which is used to compute vector distances.
     */
    private fun recalculateSignificance(force: Boolean = false) {
        if(spaces.isEmpty()) {
            return
        }
        val summaries = spaces.spaces().map { spaces[it]!!.name to spaces[it]!!.summary(norm, force) }
        val total = summaries.map { it.second }.reduce { a, b -> a + b }
        for((name, summary) in summaries) {
            val result = total.clone()
            for(i in 0 until result.size()) {
                if(result[i] == 0.0) {
                    continue
                }
                result[i] = summary[i] / result[i]
            }
            spaces[name] = result.unit(norm)
        }
    }

    /**
     * Compute the distance between a sample and all spaces.
     *
     * @param value Name of the sample to compute the distance for
     *
     * @return List of distances between the sample and all spaces
     */
    fun distances(value: String): List<Double> {
        val data = vectors[value] ?: return emptyList()
        recalculateSignificance(true)
        val vector = data.vector!!.unit(norm)
        val norms = spaces.spaces().map {
            val significance = spaces.significance(it)!!
            this.norm(vector, significance)
        }
        val total = norms.sum()
        return norms.map { (total - it) / total }
    }

    /**
     * Check whether the API contains any data, useful to check whether
     * a model can be safely imported without overwriting existing data.
     *
     * @return True if the API is empty, false otherwise
     */
    fun isEmpty(): Boolean {
        return vocabulary.isEmpty() && vectors.isEmpty() && spaces.isEmpty()
    }

    /**
     * Clear all data from the API.
     */
    fun clear() {
        spaces.clear()
        vectors.clear()
        vocabulary.clear()
    }

    fun words(): List<String> {
        return vocabulary.words()
    }
}

/**
 * POJO representation of classified text-data objects..
 */
typealias NLPModel = MutableList<Pair<String?, List<() -> TextData<*>>>>