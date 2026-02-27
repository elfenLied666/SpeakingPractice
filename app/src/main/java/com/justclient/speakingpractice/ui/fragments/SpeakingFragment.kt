package com.justclient.speakingpractice.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import com.justclient.speakingpractice.R
import com.justclient.speakingpractice.data.adapters.SpeakingPagerAdapter
import com.justclient.speakingpractice.databinding.FragmentSpeakingBinding
import com.justclient.speakingpractice.utils.GlobalConsts

class SpeakingFragment : Fragment() {

    private var _binding: FragmentSpeakingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSpeakingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var tpDisplay = arguments?.getInt(GlobalConsts.KEY_BN, 1)
        val pagerAdapter = SpeakingPagerAdapter(this, tpDisplay)
        binding.viewPager.adapter = pagerAdapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = if (position == 0) {
                requireContext().getString(R.string.practice_history)
            } else {
                requireContext().getString(R.string.needs_review)
            }
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}