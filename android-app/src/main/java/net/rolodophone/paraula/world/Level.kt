package net.rolodophone.paraula.world

class Level(val x: Int, val y: Int, val phrases: List<Phrase>) {
    companion object {
        const val RADIUS_DP = 20
        const val SMALL_RADIUS_DP = 17
    }
}

class Phrase(val english: String, val catalan: String, val examples: Set<Example>) {
    fun translate(text: String): String? {
        return when (text) {
            english -> catalan
            catalan -> english
            else -> null
        }
    }
}

class Example(val english: String, val catalan: String)