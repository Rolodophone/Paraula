package net.rolodophone.paraula.learning

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import net.rolodophone.paraula.R
import net.rolodophone.paraula.databinding.LearningGenderFragmentBinding

class GenderFragment(private val noun: Noun): Fragment() {
	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		val binding = DataBindingUtil.inflate<LearningGenderFragmentBinding>(inflater, R.layout.learning_gender_fragment, container, false)

		binding.nounText.text = noun.catalan
		binding.mascButton.text = "el ${noun.catalan}"
		binding.femButton.text = "la ${noun.catalan}"

		binding.mascButton.setOnClickListener {
			val activity = requireActivity() as LearningActivity

			if (noun.gender == Gender.MASCULINE) {
				activity.onCorrect()
				activity.nextScreen()
			}
			else activity.onIncorrect()
		}

		binding.femButton.setOnClickListener {
			val activity = requireActivity() as LearningActivity

			if (noun.gender == Gender.FEMININE) {
				activity.onCorrect()
				activity.nextScreen()
			}
			else activity.onIncorrect()
		}

		return binding.root
	}
}