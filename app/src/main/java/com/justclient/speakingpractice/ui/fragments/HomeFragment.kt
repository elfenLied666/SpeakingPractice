package com.justclient.speakingpractice.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.justclient.speakingpractice.R
import com.justclient.speakingpractice.databinding.FragmentHomeBinding
import com.justclient.speakingpractice.utils.GlobalConsts

class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.frameLayout.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt(GlobalConsts.KEY_BN, GlobalConsts.TP_ALL)
            findNavController().navigate(R.id.action_homeFragment_to_speakingFragment, bundle)
        }
    }

}