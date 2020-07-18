package net.rolodophone.paraula.world

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import net.rolodophone.paraula.R
import net.rolodophone.paraula.databinding.WorldFragmentBinding

class WorldFragment : Fragment() {
    lateinit var binding: WorldFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.world_fragment, container, false)

        binding.backButton.setOnClickListener { findNavController().navigateUp() }
        binding.worldView.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.world_background))

        Log.i("WorldFragment","onCreateView called")

        return binding.root
    }
}