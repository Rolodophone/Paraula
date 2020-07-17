package net.rolodophone.paraula.title

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import net.rolodophone.paraula.R
import net.rolodophone.paraula.databinding.TitleFragmentBinding

class TitleFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = DataBindingUtil.inflate<TitleFragmentBinding>(inflater, R.layout.title_fragment, container, false)

        binding.worldButton.setOnClickListener { findNavController().navigate(R.id.action_titleFragment_to_worldFragment) }
        binding.endlessButton.setOnClickListener { findNavController().navigate(R.id.action_titleFragment_to_endlessFragment) }

        return binding.root
    }

}