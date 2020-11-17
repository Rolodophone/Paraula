package net.rolodophone.paraula.endless

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import net.rolodophone.paraula.R
import net.rolodophone.paraula.databinding.LearningGenderFragmentBinding

class GenderFragment(private val noun: Noun): Fragment() {
	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		val binding = DataBindingUtil.inflate<LearningGenderFragmentBinding>(inflater, R.layout.learning_gender_fragment, container, false)

		val catalan = noun.catalan.first()
		binding.nounText.text = catalan
		binding.mascButton.text = "el $catalan"
		binding.femButton.text = "la $catalan"

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