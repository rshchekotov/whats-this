package edu.tum.romance.whatsthis.ui

import java.awt.Font

object FontCache {
    private val cache = mutableMapOf<Pair<String, Float>, Font>()

    fun font(name: String, size: Float): Font {
        if(cache.containsKey(name to size)) {
            return cache[name to size]!!
        }

        javaClass.classLoader.getResourceAsStream("fonts/$name.ttf").use {
            val font: Font = Font.createFont(Font.TRUETYPE_FONT, it).deriveFont(size)
            cache[name to size] = font
            return font
        }
    }
}