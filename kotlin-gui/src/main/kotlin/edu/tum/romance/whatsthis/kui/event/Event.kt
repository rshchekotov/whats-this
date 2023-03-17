package edu.tum.romance.whatsthis.kui.event

abstract class Event {
    private var cancelled = false
    @Suppress("unused")
    var isCancelled: Boolean
        set(value) { cancelled = value }
        get() = cancelled

    fun dispatch() {
        EventBus.dispatch(this)
    }
}