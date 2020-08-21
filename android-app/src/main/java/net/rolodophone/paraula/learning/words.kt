package net.rolodophone.paraula.learning

import com.squareup.moshi.*
import dev.zacsweers.moshisealed.annotations.TypeLabel

@JsonClass(generateAdapter = true)
class NewWords(val newWords: List<Word>)

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