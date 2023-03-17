package edu.tum.romance.whatsthis.kui.event

import kotlin.reflect.KClass

annotation class EventHandler(vararg val classes: KClass<*> = [Any::class])