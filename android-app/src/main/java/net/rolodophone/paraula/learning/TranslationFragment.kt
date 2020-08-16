package net.rolodophone.paraula.learning

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.*
import android.widget.*
import androidx.core.text.*
import androidx.fragment.app.Fragment
import net.rolodophone.paraula.*

class TranslationFragment(private val phrase: Phrase, private val language: Language = Language.random(), includeContext: Boolean = false) : Fragment() {

	private val example = if (includeContext) randomExample(phrase, language) else phrase.get(language)

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		val root = inflater.inflate(R.layout.learning_translation_fragment, container, false) as ViewGroup

		val textView = inflater.inflate(R.layout.learning_translation_fragment_text, root, false) as TextView

		val (contextBefore, contextAfter) = example.split(phrase.get(language), limit = 2)

		val newText = SpannableStringBuilder()
			.append(contextBefore)
			.bold { color(Color.WHITE) { append(phrase.get(language)) } }
			.append(contextAfter)

		textView.text = newText

		root.addView(textView)


		val editText = inflater.inflate(R.layout.learning_translation_fragment_input, container, false) as EditText

		editText.setOnEditorActionListener { view: TextView, _, _ ->
			submitTranslation(view.text.toString())
			true
		}

		root.addView(editText)

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