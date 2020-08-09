package net.rolodophone.paraula.learning

import androidx.fragment.app.Fragment
import com.squareup.moshi.JsonClass
import dev.zacsweers.moshisealed.annotations.TypeLabel
import net.rolodophone.paraula.*
import kotlin.random.Random.Default.nextBoolean

@JsonClass(generateAdapter = true)
class Levels(val levels: List<Level>)

@JsonClass(generateAdapter = true)
class Examples(val english: Set<String>, val catalan: Set<String>)

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

	init {
		if (BuildConfig.DEBUG) {
			if (!phrases.all { phrase ->
				examples.catalan.any { phrase.catalan in it } &&
				examples.english.any { phrase.english in it }
			}) error("all phrases must have at least one example")
		}
	}

	override fun getScreens(): List<Fragment> {
		val screens = mutableListOf<Fragment>()

		// NewPhraseFragment and easy TranslationFragment for first 3
		for (phrase in phrases.subList(0, 3)) {
			screens.add(NewPhraseFragment(phrase))
			screens.add(TranslationFragment(phrase, phrase.catalan, randomExample(phrase.catalan)))
		}

		// 2 harder translations for each of first 3
		val firstTranslationScreens = mutableSetOf<Fragment>()
		for (phrase in phrases.subList(0, 3)) {
			if (nextBoolean()) firstTranslationScreens.add(TranslationFragment(phrase, phrase.catalan, phrase.catalan))
			else               firstTranslationScreens.add(TranslationFragment(phrase, phrase.catalan, randomExample(phrase.catalan)))
			if (nextBoolean()) firstTranslationScreens.add(TranslationFragment(phrase, phrase.english, phrase.english))
			else               firstTranslationScreens.add(TranslationFragment(phrase, phrase.english, randomExample(phrase.english)))
		}
		screens.addAll(firstTranslationScreens.shuffled())

		// NewPhraseFragment and easy TranslationFragment for 4 and 5
		for (phrase in phrases.subList(3, 5)) {
			screens.add(NewPhraseFragment(phrase))
			screens.add(TranslationFragment(phrase, phrase.catalan, randomExample(phrase.catalan)))
		}

		// another 2 harder translations for 1-3 along with 4 harder translations for 4-5
		val secondTranslationScreens = mutableSetOf<Fragment>()
		for (phrase in phrases.subList(0, 3)) {
			if (nextBoolean()) secondTranslationScreens.add(TranslationFragment(phrase, phrase.catalan, phrase.catalan))
			else               secondTranslationScreens.add(TranslationFragment(phrase, phrase.catalan, randomExample(phrase.catalan)))
			if (nextBoolean()) secondTranslationScreens.add(TranslationFragment(phrase, phrase.english, phrase.english))
			else               secondTranslationScreens.add(TranslationFragment(phrase, phrase.english, randomExample(phrase.english)))
		}
		for (phrase in phrases.subList(3, 5)) {
			repeat(2) {
				if (nextBoolean()) secondTranslationScreens.add(TranslationFragment(phrase, phrase.catalan, phrase.catalan))
				else               secondTranslationScreens.add(TranslationFragment(phrase, phrase.catalan, randomExample(phrase.catalan)))
				if (nextBoolean()) secondTranslationScreens.add(TranslationFragment(phrase, phrase.english, phrase.english))
				else               secondTranslationScreens.add(TranslationFragment(phrase, phrase.english, randomExample(phrase.english)))
			}
		}
		screens.addAll(secondTranslationScreens.shuffled())

		return screens
	}
}

@TypeLabel("wip")
@JsonClass(generateAdapter = true)
class WipLevel(override val x: Int, override val y: Int): Level() {
	override fun getScreens() = listOf<Fragment>()
}