package net.rolodophone.paraula.learning

import com.squareup.moshi.*
import dev.zacsweers.moshisealed.annotations.TypeLabel

@JsonClass(generateAdapter = true)
class NewWords(val newWords: List<Word>)

@JsonClass(generateAdapter = true, generator = "sealed:type")
sealed class Word {
	abstract val info: String
	abstract val english: String
	abstract val catalan: String
}

@JsonClass(generateAdapter = true)
class SeenWord(val word: Word, var probability: Double)

@TypeLabel("noun")
@JsonClass(generateAdapter = true)
class Noun(override val catalan: String, val gender: Gender, override val english: String): Word() {
	enum class Gender {
		@Json(name = "masculine") MASCULINE, @Json(name = "feminine") FEMININE
	}

	override val info = when (gender) {
		Gender.MASCULINE -> "m."
		Gender.FEMININE -> "f."
	}
}

@TypeLabel("verb")
@JsonClass(generateAdapter = true)
class Verb(override val catalan: String, override val english: String): Word() {
	override val info = "v."
}

@TypeLabel("stressed_pronoun")
@JsonClass(generateAdapter = true)
class StressedPronoun(override val catalan: String, override val english: String, private val catalanFormat: String, private val englishFormat: String): Word() {
	override val info = "pron."

	fun getCatalanExample(seenWords: MutableSet<SeenWord>): String {
		val example = catalanFormat
		
		example.replace("<noun>", seenWords.filter { it.word is Noun }.random().word.catalan)
		example.replace("<verb>", seenWords.filter { it.word is Verb }.random().word.catalan)
		
		return example
	}

	fun getEnglishExample(seenWords: MutableSet<SeenWord>): String {
		val example = englishFormat

		example.replace("<noun>", seenWords.filter { it.word is Noun }.random().word.english)
		example.replace("<verb>", seenWords.filter { it.word is Verb }.random().word.english)

		return example
	}
}