package edu.tum.romance.whatsthis.util.observer

class Observable<T>(initialValue: T) {
    private var data: T = initialValue
    var value: T
        get() = data
        set(value) {
            if(value == data) return
            data = value
            val old = this.value
            notifyObservers(old to value)
        }

    private val observers = mutableMapOf<Int, Observer<T>>()

    fun observe(id: Int, observer: Observer<T>) = observers.put(id, observer)
    fun stopObserving(id: Int) = observers.remove(id)
    private fun notifyObservers(change: Pair<T, T>) = observers.values.forEach { it.update(change) }
}

fun Observable<Boolean>.trigger() {
    this.value = !this.value
}