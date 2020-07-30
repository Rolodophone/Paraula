package net.rolodophone.paraula.learning

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import net.rolodophone.paraula.*

@Suppress("UNUSED")
class TranslationFragment : Fragment() {

	private lateinit var textView: TextView
	private lateinit var editText: EditText
	private lateinit var progressBar: ProgressBar

	private val vm: TranslationViewModel by lazy { getViewModel { TranslationViewModel((requireActivity() as LearningActivity).level.phrases.iterator()) } }


	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
		val root = inflater.inflate(R.layout.learning_translation_fragment, container, false) as ViewGroup

		progressBar = (requireActivity() as LearningActivity).progressBar

		textView = inflater.inflate(R.layout.learning_translation_text, root, false) as TextView
		vm.textViewText.observe(viewLifecycleOwner) {
			textView.text = it
		}
		root.addView(textView)

		editText = inflater.inflate(R.layout.learning_translation_text_input, root, false) as EditText
		editText.setOnEditorActionListener { _, _, _ -> submitTranslation() }
		root.addView(editText)

		return root
	}


	private fun nextPhrase() {

		// try to move on to the next phrase, else exit activity
		if (!vm.nextTranslationText()) finishLevel()
	}


	private fun submitTranslation(): Boolean {
		if (vm.phrase.translate(editText.text.toString()) == vm.phraseText) {
			//user got it correct

			nextPhrase()
			editText.text.clear()
			// TODO add sound effect
		}
		else {
			//user got it wrong

			//TODO
		}

		return true
	}


	private fun finishLevel() {
		requireActivity().finish()
	}
}