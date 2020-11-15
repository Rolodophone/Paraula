package net.rolodophone.paraula.learning

import com.squareup.moshi.JsonClass
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

enum class Gender { MASCULINE, FEMININE }
enum class Person { FIRST, SECOND, THIRD }
enum class Number { SINGULAR, PLURAL }
enum class Formality { INFORMAL, FORMAL, RESPECTFUL }

@TypeLabel("noun")
@JsonClass(generateAdapter = true)
class Noun(override val catalan: String, val gender: Gender, override val english: String): Word() {
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
class StressedPronoun(override val catalan: String, override val english: String, val person: Person, val number: Number, val formality: Formality?, val gender: Gender?): Word() {
	override val info = "pron."
}