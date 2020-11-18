package net.rolodophone.paraula.endless

import com.squareup.moshi.JsonClass
import dev.zacsweers.moshisealed.annotations.TypeLabel

@JsonClass(generateAdapter = true)
class NewWords(val newWords: List<Word>)


@JsonClass(generateAdapter = true)
class SeenWord(val word: Word, var probability: Double)


/**
 * A representation of a single meaning of a word. Words that could be
 * considered synonymous but actually mean slightly different things e.g. run and sprint should
 * be in separate objects. Expressions e.g. "the time is" should be of the Expression subclass.
 * All words that should come up in the translation exercise should implement `Translatable`.
 */
@JsonClass(generateAdapter = true, generator = "sealed:type")
sealed class Word

interface Translatable {
	/**
	 * The words in Catalan that mean exactly the same, ordered from most to least commonly used.
	 */
	val catalan: List<String>

	/**
	 * The words in English that mean exactly the same, ordered from most to least commonly used.
	 */
	val english: List<String>

	fun get(language: Language) = when (language) {
		Language.CATALAN -> catalan
		Language.ENGLISH -> english
	}

	/**
	 * Short clarification in Catalan of what meaning of the Catalan word this object refers to.
	 */
	val catalanDisambiguation: String?

	/**
	 * Short clarification in English of what meaning of the English word this object refers to.
	 */
	val englishDisambiguation: String?

	fun getDisambiguation(language: Language) = when (language) {
		Language.CATALAN -> catalanDisambiguation
		Language.ENGLISH -> englishDisambiguation
	}
}


enum class Gender { MASCULINE, FEMININE }
enum class Person { FIRST, SECOND, THIRD }
enum class Number { SINGULAR, PLURAL }
enum class Formality { INFORMAL, FORMAL }
enum class Language { CATALAN, ENGLISH }
enum class SyntacticFunction { DIRECT_OBJECT, INDIRECT_OBJECT, REFLEXIVE, NEUTER }
enum class Form { REINFORCED, ELIDED, FULL, REDUCED }

@TypeLabel("NOUN")
@JsonClass(generateAdapter = true)
class Noun(
	override val catalan: List<String>,
	override val english: List<String>,
	val gender: Gender,
	override val catalanDisambiguation: String? = null,
	override val englishDisambiguation: String? = null
): Word(), Translatable

@TypeLabel("VERB")
@JsonClass(generateAdapter = true)
class Verb(
	override val catalan: List<String>,
	override val english: List<String>,
	override val catalanDisambiguation: String? = null,
	override val englishDisambiguation: String? = null
): Word(), Translatable

@TypeLabel("STRONG_PRONOUN")
@JsonClass(generateAdapter = true)
class StrongPronoun(
	override val catalan: List<String>,
	override val english: List<String>,
	val person: Person,
	val number: Number,
	val gender: Gender?,
	val formality: Formality?,
	override val catalanDisambiguation: String? = null,
	override val englishDisambiguation: String? = null
): Word(), Translatable

@TypeLabel("EXPRESSION")
@JsonClass(generateAdapter = true)
class Expression(
	override val catalan: List<String>,
	override val english: List<String>,
	override val catalanDisambiguation: String? = null,
	override val englishDisambiguation: String? = null
): Word(), Translatable

@TypeLabel("WEAK_PRONOUN")
@JsonClass(generateAdapter = true)
class WeakPronoun(
	val catalan: String,
	val numbers: Set<Number>,
	val persons: Set<Person>,
	val syntacticFunctions: Set<SyntacticFunction>,
	val genders: Set<Gender>,
	val forms: Set<Form>
): Word()