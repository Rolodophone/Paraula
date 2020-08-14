package net.rolodophone.paraula.endless

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import net.rolodophone.paraula.R
import net.rolodophone.paraula.databinding.EndlessFragmentBinding
import net.rolodophone.paraula.learning.LearningActivity

class EndlessFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<EndlessFragmentBinding>(inflater, R.layout.endless_fragment, container, false)

        binding.backButton.setOnClickListener { findNavController().navigateUp() }
        binding.playButton.setOnClickListener {
            context?.startActivity(Intent(context, LearningActivity::class.java).apply { putExtra(LearningActivity.SPECIAL_LEVEL_EXTRA, LearningActivity.ENDLESS) })
        }

        return binding.root
    }
}