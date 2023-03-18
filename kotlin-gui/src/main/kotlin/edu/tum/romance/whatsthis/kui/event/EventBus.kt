package edu.tum.romance.whatsthis.kui.event

import org.reflections.Reflections
import org.reflections.scanners.Scanners
import java.lang.reflect.Method

object EventBus {
    private val listeners = mutableMapOf<Method, Any>()
    private val inactive = mutableMapOf<Method, Any>()

    init {
        val reflections = Reflections("edu.tum.romance.whatsthis", Scanners.MethodsAnnotated)
        val methods = reflections.getMethodsAnnotatedWith(EventHandler::class.java)
        methods.forEach { method ->
            method.declaringClass.kotlin.objectInstance?.let {
                listeners[method] = it
            }
        }
    }

    /**
     * Used for instances to subscribe to the event bus.
     * Object declarations are automatically subscribed to the event bus.
     *
     * @param listener The instance to subscribe
     */
    @Suppress("unused")
    fun subscribe(listener: Any) {
        if(listener in inactive.values) {
            for((method, obj) in inactive) {
                if(obj == listener) {
                    listeners[method] = obj
                    inactive.remove(method)
                }
            }
        }

        if(listener !in listeners.values) {
            val reflections = Reflections(listener.javaClass, Scanners.MethodsAnnotated)
            val methods = reflections.getMethodsAnnotatedWith(EventHandler::class.java)
            for(method in methods) listeners[method] = listener
        }
    }

    /**
     * Used for instances to unsubscribe from the event bus.
     * Should you unsubscribe an object declaration, you will
     * have to manually subscribe it again.
     *
     * @param listener The instance to unsubscribe
     */
    @Suppress("unused")
    fun unsubscribe(listener: Any) {
        for((method, obj) in listeners) {
            if(obj == listener) {
                inactive[method] = obj
                listeners.remove(method)
            }
        }
    }

    /**
     * Dispatches an event to all subscribed instances.
     *
     * @param event The event to dispatch
     */
    fun dispatch(event: Event) {
        for((method, listener) in listeners) {
            val annotation = method.getAnnotation(EventHandler::class.java)
            if(Any::class !in annotation.classes && event::class !in annotation.classes) continue

            when (method.parameterCount) {
                1 -> {
                    if(method.parameterTypes[0] != event::class.java) continue
                    method.invoke(listener, event)
                }
                0 -> method.invoke(listener)
                else -> error("Event handler method ${method.name} at ${listener.javaClass.simpleName} has wrong parameter count or type!")
            }
        }
    }
}