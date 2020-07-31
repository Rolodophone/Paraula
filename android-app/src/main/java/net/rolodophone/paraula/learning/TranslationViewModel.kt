package net.rolodophone.paraula.learning

import android.graphics.Color
import android.text.SpannableStringBuilder
import androidx.core.text.*
import androidx.lifecycle.*
import net.rolodophone.paraula.world.*
import kotlin.random.Random

class TranslationViewModel(private val phrasesIterator: Iterator<Phrase>): ViewModel() {

	lateinit var phrase: Phrase
	lateinit var phraseText: String

	private val _textViewText = MutableLiveData<CharSequence>()
	val textViewText get() = _textViewText as LiveData<CharSequence>

	init {
		nextTranslationText()
	}


	fun nextTranslationText(): Boolean {

		if (phrasesIterator.hasNext()) {
			phrase = phrasesIterator.next()

			//add example containing just the phrase so sometimes you get the phrase alone
			val example = phrase.examples.plus(Example(phrase.english, phrase.catalan)).random()

			val exampleText: String

			if (Random.nextBoolean()) {
				exampleText = example.english
				phraseText = phrase.english
			} else {
				exampleText = example.catalan
				phraseText = phrase.catalan
			}

			val (exampleBefore, exampleAfter) = exampleText.split(phraseText, limit = 2)

			val textToDisplay = SpannableStringBuilder()
				.append(exampleBefore)
				.bold { color(Color.WHITE) { append(phraseText) } }
				.append(exampleAfter)

			_textViewText.value = textToDisplay.replace(0, 1, textToDisplay[0].toUpperCase().toString())

			return true
		}

		else return false
	}
}