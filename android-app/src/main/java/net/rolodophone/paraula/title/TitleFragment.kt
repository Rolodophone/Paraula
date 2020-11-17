package net.rolodophone.paraula.title

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import net.rolodophone.paraula.R
import net.rolodophone.paraula.databinding.TitleFragmentBinding
import net.rolodophone.paraula.endless.LearningActivity

class TitleFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = DataBindingUtil.inflate<TitleFragmentBinding>(inflater, R.layout.title_fragment, container, false)

        binding.endlessButton.setOnClickListener { context?.startActivity(Intent(context, LearningActivity::class.java)) }

        return binding.root
    }

}