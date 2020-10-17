package net.rolodophone.paraula.learning

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.core.text.*
import androidx.fragment.app.Fragment
import net.rolodophone.paraula.*

class TranslationFragment(private val phrase: Phrase, private val language: Language = Language.random(), includeContext: Boolean = false) : Fragment() {

	private val example = if (includeContext) randomExample(phrase, language) else phrase.get(language)

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		val root = inflater.inflate(R.layout.learning_translation_fragment, container, false) as ViewGroup


		val (contextBefore, contextAfter) = example.split(phrase.get(language), limit = 2)

		val newText = SpannableStringBuilder()
			.append(contextBefore)
			.bold { color(Color.WHITE) { append(phrase.get(language)) } }
			.append(contextAfter)

		root.findViewById<TextView>(R.id.translationText).text = newText


		val editText = root.findViewById<TextView>(R.id.translationTextInput)

		editText.setOnEditorActionListener { view: TextView, _, _ ->
			submitTranslation(view.text.toString())
			true
		}

		editText.showSoftInputOnFocus = false

		val ic = editText.onCreateInputConnection(EditorInfo())
		root.findViewById<Keyboard>(R.id.keyboard).inputConnection = ic

		editText.requestFocus()

		return root
	}


	private fun submitTranslation(inputtedText: String) {
		val activity = requireActivity() as LearningActivity

		if (phrase.translate(inputtedText) in phrase.getAll(language)) {
			activity.onCorrect()
			activity.nextScreen()
		}
		else {
			activity.onIncorrect()
		}
	}
}