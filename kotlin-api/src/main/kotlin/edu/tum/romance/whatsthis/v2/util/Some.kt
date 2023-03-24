package edu.tum.romance.whatsthis.v2.util

@Suppress("UNCHECKED_CAST")
class Some<T : Any>(vararg data: T) {
    private val data: Any
    private val q: Int = data.size
    val quantity: Int
        get() = q

    init {
        when (q) {
            0 -> this.data = Unit
            1 -> this.data = data[0]
            else -> this.data = data
        }
    }

    fun <Y> ifNone(block: () -> Y?): Y? {
        return if (q == 0) block() else null
    }

    fun <Y> ifSingle(block: (T) -> Y?): Y? {
        return if (q == 1) block(data as T) else null
    }

    fun <Y> ifMany(block: (Array<T>) -> Y?): Y? {
        return if (q > 1) block(data as Array<T>) else null
    }

    companion object {
        fun <T : Any> empty() = Some<T>()
        fun <T : Any> of(vararg data: T) = Some(*data)
        fun <T : Any> fromArray(data: Array<T>) = Some(*data)
        fun words(phrase: String): Some<String> {
            return fromArray(phrase.split(Regex("\\s+")).toTypedArray())
        }
    }
}

inline fun <reified Y : Any> List<Some<Y>>.flatten(): Some<Y> {
    val data = mutableListOf<Y>()
    for (some in this) {
        if(some.ifNone { true } == true) continue
        some.ifSingle { data.add(it) }
        some.ifMany { data.addAll(it) }
    }
    return Some.fromArray(data.toTypedArray())
}