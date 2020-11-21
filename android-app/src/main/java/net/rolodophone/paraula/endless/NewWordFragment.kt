package net.rolodophone.paraula.endless

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import net.rolodophone.paraula.R
import net.rolodophone.paraula.databinding.LearningNewWordFragmentBinding
import net.rolodophone.paraula.setWordProbabilities

class NewWordFragment(private val word: Translatable): Fragment() {

	@SuppressLint("SetTextI18n")
	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		val binding = DataBindingUtil.inflate<LearningNewWordFragmentBinding>(inflater, R.layout.learning_new_word_fragment, container, false)

		binding.catalanWords.text = word.catalan.joinToString(separator = "\n")
		binding.englishWords.text = word.english.joinToString(separator = "\n")
		if (word.catalanDisambiguation != null) binding.catalanDisambiguation.text = "(${word.catalanDisambiguation})"
		if (word.englishDisambiguation != null) binding.englishDisambiguation.text = "(${word.englishDisambiguation})"

		binding.button.setOnClickListener {
			with(requireActivity() as LearningActivity) {

				// increment indexOfNextNewWord
				val indexOfNextNewWord = getPreferences(Context.MODE_PRIVATE).getInt(LearningActivity.INDEX_OF_NEXT_NEW_WORD, 0)

				with(getPreferences(Context.MODE_PRIVATE).edit()) {
					putInt(LearningActivity.INDEX_OF_NEXT_NEW_WORD, indexOfNextNewWord + 1)
					putBoolean(LearningActivity.ENDLESS_COMPLETED_DIALOG_SEEN, false) // if this has been set to true, reset to false because I must've added more new words
					apply()
				}

				// add the word to seenWords
				seenWords.add(currentWord)
				setWordProbabilities(this, seenWords.map { it.probability })

				nextScreen()
			}
		}

		return binding.root
	}
}