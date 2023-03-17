package edu.tum.romance.whatsthis.util.observer

fun interface Observer<T> {
    fun update(value: Pair<T, T>)
}