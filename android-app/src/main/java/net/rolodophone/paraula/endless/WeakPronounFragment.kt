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
import kotlin.random.Random.Default.nextInt

class WeakPronounFragment(private val pronoun: WeakPronoun): Fragment() {

	private val form = nextInt(4)

	@SuppressLint("SetTextI18n")
	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		val root = inflater.inflate(R.layout.learning_weak_pronoun_fragment, container, false) as ViewGroup

		val variables = listOf(pronoun.number, pronoun.person, pronoun.syntacticFunction, pronoun.gender, form)
		root.findViewById<TextView>(R.id.translationText).text = variables.joinToString(separator = " ")

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

		if (inputtedText == pronoun.catalanForms[form]) {
			activity.onCorrect()
			activity.nextScreen()
		}
		else {
			activity.onIncorrect()
		}
	}
}