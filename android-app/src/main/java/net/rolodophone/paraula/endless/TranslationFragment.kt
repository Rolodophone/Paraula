package net.rolodophone.paraula.endless

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.fragment.app.Fragment
import net.rolodophone.paraula.R

class TranslationFragment(private val word: Word) : Fragment() {

	private val language = Language.values().random()

	@SuppressLint("SetTextI18n")
	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		val root = inflater.inflate(R.layout.learning_translation_fragment, container, false) as ViewGroup

		root.findViewById<TextView>(R.id.translationText).text = word.get(language).random()

		if (word.getDisambiguation(language) != null) {
			root.findViewById<TextView>(R.id.translationTextDisambiguation).text = "(${word.getDisambiguation(language)})"
		}

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

		val otherLanguage = when (language) {
			Language.CATALAN -> Language.ENGLISH
			Language.ENGLISH -> Language.CATALAN
		}

		if (inputtedText in word.get(otherLanguage)) {
			activity.onCorrect()
			activity.nextScreen()
		}
		else {
			activity.onIncorrect()
		}
	}
}