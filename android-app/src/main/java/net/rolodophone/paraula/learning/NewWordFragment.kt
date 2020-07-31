package net.rolodophone.paraula.learning

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import net.rolodophone.paraula.R
import net.rolodophone.paraula.databinding.LearningNewWordFragmentBinding

class NewWordFragment: Fragment() {
	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		val binding = DataBindingUtil.inflate<LearningNewWordFragmentBinding>(inflater, R.layout.learning_new_word_fragment, container, false)

		//TODO set textView texts

		return binding.root
	}
}