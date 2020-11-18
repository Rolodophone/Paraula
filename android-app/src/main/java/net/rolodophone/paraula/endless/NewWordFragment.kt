package net.rolodophone.paraula.endless

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import net.rolodophone.paraula.R
import net.rolodophone.paraula.databinding.LearningNewWordFragmentBinding

class NewWordFragment(private val word: Translatable): Fragment() {

	@SuppressLint("SetTextI18n")
	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		val binding = DataBindingUtil.inflate<LearningNewWordFragmentBinding>(inflater, R.layout.learning_new_word_fragment, container, false)

		binding.catalanWords.text = word.catalan.joinToString(separator = "\n")
		binding.englishWords.text = word.english.joinToString(separator = "\n")
		if (word.catalanDisambiguation != null) binding.catalanDisambiguation.text = "(${word.catalanDisambiguation})"
		if (word.englishDisambiguation != null) binding.englishDisambiguation.text = "(${word.englishDisambiguation})"

		binding.button.setOnClickListener {
			(requireActivity() as LearningActivity).nextScreen()
		}

		return binding.root
	}
}