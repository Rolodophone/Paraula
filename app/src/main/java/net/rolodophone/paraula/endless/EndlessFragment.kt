package net.rolodophone.paraula.endless

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import net.rolodophone.paraula.R
import net.rolodophone.paraula.databinding.EndlessFragmentBinding

class EndlessFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<EndlessFragmentBinding>(inflater, R.layout.endless_fragment, container, false)

        binding.backButton.setOnClickListener { findNavController().navigateUp() }

        return binding.root
    }
}