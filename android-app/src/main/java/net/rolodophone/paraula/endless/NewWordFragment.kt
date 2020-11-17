package net.rolodophone.paraula.endless

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import net.rolodophone.paraula.R
import net.rolodophone.paraula.databinding.LearningNewWordFragmentBinding

class NewWordFragment(private val word: Word): Fragment() {

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		val binding = DataBindingUtil.inflate<LearningNewWordFragmentBinding>(inflater, R.layout.learning_new_word_fragment, container, false)

		binding.catalanWords.text = word.catalan.joinToString(separator = "\n")
		binding.englishWords.text = word.english.joinToString(separator = "\n")

		binding.button.setOnClickListener {
			(requireActivity() as LearningActivity).nextScreen()
		}

		return binding.root
	}
}