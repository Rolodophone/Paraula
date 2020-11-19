package net.rolodophone.paraula.endless

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import net.rolodophone.paraula.R
import net.rolodophone.paraula.databinding.LearningNewWeakPronounFragmentBinding

class NewWeakPronounFragment(private val pronoun: WeakPronoun): Fragment() {

	@SuppressLint("SetTextI18n")
	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		val binding = DataBindingUtil.inflate<LearningNewWeakPronounFragmentBinding>(inflater, R.layout.learning_new_weak_pronoun_fragment, container, false)

		val variables = listOf(pronoun.numbers, pronoun.persons, pronoun.syntacticFunctions, pronoun.genders, pronoun.forms)
		val formattedVariables = variables.map { it.joinToString(separator = ",") { it.toString() } }

		binding.variables.text = formattedVariables.joinToString(separator = " ")
		binding.catalan.text = pronoun.catalan

		binding.button.setOnClickListener {
			(requireActivity() as LearningActivity).nextScreen()
		}

		return binding.root
	}
}