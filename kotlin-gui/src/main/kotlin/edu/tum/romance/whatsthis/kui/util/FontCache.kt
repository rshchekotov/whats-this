package edu.tum.romance.whatsthis.kui.util

import java.awt.Font

object FontCache {
    const val SMALL = 16
    const val MEDIUM = 24
    const val HUGE = 32

    private val cache = mutableMapOf<Pair<String, Int>, Font>()

    operator fun get(name: String, size: Int): Font {
        if(name to size !in cache) {
            val fontStream = javaClass.classLoader.getResourceAsStream("fonts/$name.ttf")
                ?: try {
                    val font = Font(name, Font.PLAIN, size)
                    cache[name to size] = font
                    return font
                } catch(e: Exception) {
                    error("Font $name not found")
                }

            fontStream.use {
                val font: Font = Font.createFont(Font.TRUETYPE_FONT, it).deriveFont(size.toFloat())
                cache[name to size] = font
            }
        }
        return cache[name to size]!!
    }

    fun comfortaa(size: Int) = get("Comfortaa-Regular", size)
}