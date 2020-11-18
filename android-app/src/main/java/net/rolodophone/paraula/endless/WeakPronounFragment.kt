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

class WeakPronounFragment(pronoun: WeakPronoun): Fragment() {

	@SuppressLint("SetTextI18n")
	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		val root = inflater.inflate(R.layout.learning_translation_fragment, container, false) as ViewGroup

		root.findViewById<TextView>(R.id.translationText).text = verb.catalan.random() // TODO random? or a conjugation for each synonym?

		if (verb.catalanDisambiguation != null) {
			root.findViewById<TextView>(R.id.translationTextDisambiguation).text = "(${verb.catalanDisambiguation})"
		}

		// TODO show text before and after

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

//		if (TODO) {
//			activity.onCorrect()
//			activity.nextScreen()
//		}
//		else {
//			activity.onIncorrect()
//		}
	}
}