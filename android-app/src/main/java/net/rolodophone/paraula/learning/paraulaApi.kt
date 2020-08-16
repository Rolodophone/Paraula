package net.rolodophone.paraula.learning

import androidx.fragment.app.Fragment
import com.squareup.moshi.*
import dev.zacsweers.moshisealed.annotations.TypeLabel
import net.rolodophone.paraula.*
import kotlin.random.Random.Default.nextBoolean

@JsonClass(generateAdapter = true)
class Levels(val levels: List<Level>)

@JsonClass(generateAdapter = true)
class Examples(val english: Set<String>, val catalan: Set<String>)

@JsonClass(generateAdapter = true)
class NewWords(val newWords: List<Word>)

@JsonClass(generateAdapter = true, generator = "sealed:type")
sealed class Level {

	companion object {
		const val RADIUS_DP = 20
		const val SMALL_RADIUS_DP = 17
	}

	abstract val x: Int
	abstract val y: Int

	abstract fun getExercises(): List<Fragment>
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

	fun get(language: Language) = when (language) {
		Language.ENGLISH -> english
		Language.CATALAN -> catalan
	}
	
	fun getAlt(language: Language) = when (language) {
		Language.ENGLISH -> altEnglish
		Language.CATALAN -> altCatalan
	}
	
	fun getAll(language: Language) = when (language) {
		Language.ENGLISH -> allEnglish
		Language.CATALAN -> allCatalan
	}
}

enum class Language {
	ENGLISH, CATALAN;

	companion object {
		fun random() = if (nextBoolean()) ENGLISH else CATALAN
	}
}

@JsonClass(generateAdapter = true)
class Example(val english: String, val catalan: String) {
	fun get(language: Language) = when (language) {
		Language.ENGLISH -> english
		Language.CATALAN -> catalan
	}
}


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

	override fun getExercises(): List<Fragment> {
		val exercises = mutableListOf<Fragment>()

		// NewPhraseFragment and easy TranslationFragment for first 3
		for (phrase in phrases.subList(0, 3)) {
			exercises.add(NewPhraseFragment(phrase))
			exercises.add(TranslationFragment(phrase, Language.CATALAN, includeContext = true))
		}

		// 2 harder translations for each of first 3
		val firstTranslationExercises = mutableSetOf<Fragment>()
		for (phrase in phrases.subList(0, 3)) {
			firstTranslationExercises.add(TranslationFragment(phrase, Language.CATALAN, includeContext = nextBoolean()))
			firstTranslationExercises.add(TranslationFragment(phrase, Language.ENGLISH, includeContext = nextBoolean()))
		}
		exercises.addAll(firstTranslationExercises.shuffled())

		// NewPhraseFragment and easy TranslationFragment for 4 and 5
		for (phrase in phrases.subList(3, 5)) {
			exercises.add(NewPhraseFragment(phrase))
			exercises.add(TranslationFragment(phrase, Language.CATALAN, includeContext = true))
		}

		// another 2 harder translations for 1-3 along with 4 harder translations for 4-5
		val secondTranslationExercises = mutableSetOf<Fragment>()
		for (phrase in phrases.subList(0, 3)) {
			secondTranslationExercises.add(TranslationFragment(phrase, Language.CATALAN, includeContext = nextBoolean()))
			secondTranslationExercises.add(TranslationFragment(phrase, Language.ENGLISH, includeContext = nextBoolean()))
		}
		for (phrase in phrases.subList(3, 5)) {
			repeat(2) {
				secondTranslationExercises.add(TranslationFragment(phrase, Language.CATALAN, includeContext = nextBoolean()))
				secondTranslationExercises.add(TranslationFragment(phrase, Language.ENGLISH, includeContext = nextBoolean()))
			}
		}
		exercises.addAll(secondTranslationExercises.shuffled())

		return exercises
	}
}

@TypeLabel("wip")
@JsonClass(generateAdapter = true)
class WipLevel(override val x: Int, override val y: Int): Level() {
	override fun getExercises() = listOf<Fragment>()
}

@JsonClass(generateAdapter = true, generator = "sealed:type")
sealed class Word

@JsonClass(generateAdapter = true)
class SeenWord(val word: Word, var probability: Double)

@TypeLabel("noun")
@JsonClass(generateAdapter = true)
class Noun(val catalan: String, val gender: Gender, val english: String): Word() {
	enum class Gender {
		@Json(name = "masculine") MASCULINE, @Json(name = "feminine") FEMININE
	}
}

@TypeLabel("verb")
@JsonClass(generateAdapter = true)
class Verb(val catalan: String, val english: String): Word()