package net.rolodophone.paraula.learning

import androidx.fragment.app.Fragment
import com.squareup.moshi.JsonClass
import dev.zacsweers.moshisealed.annotations.TypeLabel
import net.rolodophone.paraula.randomExample

@JsonClass(generateAdapter = true)
class ParaulaApi(val levels: List<Level>, val englishExamples: Set<String>, val catalanExamples: Set<String>)

@JsonClass(generateAdapter = true, generator = "sealed:type")
sealed class Level {

	companion object {
		const val RADIUS_DP = 20
		const val SMALL_RADIUS_DP = 17
	}

	abstract val x: Int
	abstract val y: Int

	abstract fun getScreens(): List<Fragment>
}

@JsonClass(generateAdapter = true)
class Phrase(val english: String, val altEnglish: Set<String>? = null, val catalan: String, val altCatalan: Set<String>? = null) {

	val allEnglish get() = altEnglish?.plus(english) ?: setOf(english)
	val allCatalan get() = altCatalan?.plus(catalan) ?: setOf(catalan)

	fun translate(text: String): String? {
		return when (text) {
			in allEnglish -> catalan
			in allCatalan -> english
			else -> null
		}
	}
}

@JsonClass(generateAdapter = true)
class Example(val english: String, val catalan: String)


@TypeLabel("vocab")
@JsonClass(generateAdapter = true)
class VocabLevel(override val x: Int, override val y: Int, val phrases: List<Phrase>): Level() {

	override fun getScreens(): List<Fragment> {
		val screens = mutableListOf<Fragment>()

		screens.add(NewPhraseFragment(phrases[0]))
		screens.add(TranslationFragment(phrases[0], phrases[0].catalan, randomExample(phrases[0].catalan)))

		return screens
	}


}

@TypeLabel("wip")
@JsonClass(generateAdapter = true)
class WipLevel(override val x: Int, override val y: Int): Level() {
	override fun getScreens() = listOf<Fragment>()
}