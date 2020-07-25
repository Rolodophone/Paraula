package net.rolodophone.paraula.learning

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.core.text.bold
import androidx.core.text.color
import androidx.fragment.app.Fragment
import net.rolodophone.paraula.R
import net.rolodophone.paraula.world.Example
import net.rolodophone.paraula.world.Level
import net.rolodophone.paraula.world.Phrase
import kotlin.random.Random.Default.nextBoolean

@Suppress("UNUSED")
class TranslationFragment : Fragment() {

	lateinit var level: Level

	lateinit var translationTextView: TextView
	lateinit var translationEditText: EditText

	var phraseIndex = -1
	lateinit var phrase: Phrase

	lateinit var phraseText: String

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
		val root = inflater.inflate(R.layout.learning_translation_fragment, container, false) as ViewGroup

		level = (requireActivity() as LearningActivity).level

		translationTextView = inflater.inflate(R.layout.learning_translation_text, root, false) as TextView
		root.addView(translationTextView)

		translationEditText = inflater.inflate(R.layout.learning_translation_text_input, root, false) as EditText
		root.addView(translationEditText)

		nextActivity()

		return root
	}

	private fun nextActivity() {
		phraseIndex++

		if (phraseIndex >= level.phrases.size) finishLevel()

		phrase = level.phrases[phraseIndex]
		val example = phrase.examples.plus(Example(phrase.english, phrase.catalan)).random() //add example containing just the phrase so sometimes you get the phrase alone
		//TODO make sure first time you see a word you definitely get some context

		val phraseText: String
		val exampleText: String

		if (nextBoolean()) {
			exampleText = example.english
			phraseText = phrase.english
		} else {
			exampleText = example.catalan
			phraseText = phrase.catalan
		}

		val exampleTextList = exampleText.split(phraseText, limit = 2)
		val exampleBefore = exampleTextList[0]
		val exampleAfter = exampleTextList[1]


		val textToDisplay = SpannableStringBuilder()
			.append(exampleBefore)
			.bold { color(Color.WHITE) { append(phraseText) } }
			.append(exampleAfter)

		translationTextView.text = textToDisplay.replace(0, 1, textToDisplay[0].toUpperCase().toString())
	}

	private fun submitTranslation() {
		if (phrase.translate(translationEditText.text.toString()) == phraseText) {
			//user got it correct

			nextActivity()
			translationEditText.text.clear()
			// TODO add sound effect
		}
		else {
			//user got it wrong

			//TODO
		}
	}

	private fun finishLevel() {
		requireActivity().finish()
	}
}