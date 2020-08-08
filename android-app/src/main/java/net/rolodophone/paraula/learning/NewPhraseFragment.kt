package net.rolodophone.paraula.learning

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import net.rolodophone.paraula.R
import net.rolodophone.paraula.databinding.LearningNewPhraseFragmentBinding

class NewPhraseFragment(private val phrase: Phrase): Fragment() {

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		val binding = DataBindingUtil.inflate<LearningNewPhraseFragmentBinding>(inflater, R.layout.learning_new_phrase_fragment, container, false)

		binding.englishMainWord.text = phrase.english
		binding.englishSynonymWords.text = phrase.altEnglish?.joinToString("\n")
		binding.catalanMainWord.text = phrase.catalan
		binding.catalanSynonymWords.text = phrase.altCatalan?.joinToString("\n")

		binding.button.setOnClickListener {
			(requireActivity() as LearningActivity).nextScreen()
		}

		return binding.root
	}
}