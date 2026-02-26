package com.justclient.speakingpractice.ui.fragments

import android.content.res.ColorStateList
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.launch
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.justclient.speakingpractice.R
import com.justclient.speakingpractice.data.models.MainViewModel
import com.justclient.speakingpractice.databinding.FragmentPracticeHistoryBinding
import com.justclient.speakingpractice.utils.GlobalConsts
import kotlinx.coroutines.launch
import kotlin.getValue

class PracticeHistoryFragment : Fragment() {

    private var _binding: FragmentPracticeHistoryBinding? = null
    private val binding get() = _binding!!

    var tpDisplay: Int? = null

    private val viewModel: MainViewModel by activityViewModels()


    var activeColorFull: Int = 0
    var inactiveColorFull = 0
    var activeColorText = 0
    var inactiveColorText = 0
    var activeColorMini = 0
    var inactiveColorMini = 0
    var inactiveColorMiniText = 0
    var activeColorMiniText = 0
    val alphaNoTrasparent = 1.0f
    val alphaTransparent = 0.4f
    val alphaTransparentCircle = 0.28f


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentPracticeHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activeColorFull = ContextCompat.getColor(requireContext(), R.color.lbl_act)
        inactiveColorFull = ContextCompat.getColor(requireContext(), R.color.lbl_no_act_full)
        activeColorText = ContextCompat.getColor(requireContext(), R.color.lbl_act_text)
        inactiveColorText = ContextCompat.getColor(requireContext(), R.color.lbl_no_act_text)
        activeColorMini = ContextCompat.getColor(requireContext(), R.color.lbl_mini_circle_act)
        inactiveColorMini = ContextCompat.getColor(requireContext(), R.color.lbl_mini_circle_no_act)
        inactiveColorMiniText = ContextCompat.getColor(requireContext(), R.color.lbl_mini_text_no_act)
        activeColorMiniText = ContextCompat.getColor(requireContext(), R.color.lbl_mini_text_act)
        tpDisplay = arguments?.getInt(GlobalConsts.KEY_BN, 1)
        if(tpDisplay == GlobalConsts.TP_ALL) {

        } else if(tpDisplay == GlobalConsts.TP_WORD) {

        } else if(tpDisplay == GlobalConsts.TP_SENTENCE) {

        } else {

        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.filteredWords.collect { wordsList ->
                //wordsAdapter.submitList(wordsList)
            }
        }

        binding.chipAll.setOnClickListener {
            showChipAll()
            //viewModel.setFilter(GlobalConsts.BN_ALL_WORDS)
            // Тут же можно добавить логику смены стиля метки (сделать ее активной)
        }

        binding.chipWords.setOnClickListener {
            showChipWords()
            //viewModel.setFilter(GlobalConsts.TP_WORD) // Фильтр по типу "Слово"
        }

        binding.chipSentences.setOnClickListener {
            showChipSentences()
            //viewModel.setFilter(GlobalConsts.TP_SENTENCE) // Фильтр по типу "Предложение"
        }
    }

    fun showChipAll() {
        if(tpDisplay == GlobalConsts.TP_ALL) return
        hideChipWords()
        hideChipSentences()
        binding.chipAll.setCardBackgroundColor(activeColorFull)
        binding.tvChipAll.setTextColor(activeColorText)
        binding.tvChipAll.alpha = alphaNoTrasparent
    }

    fun showChipWords() {
        if(tpDisplay == GlobalConsts.TP_WORD) return
        hideChipAll()
        hideChipSentences()
        binding.chipWords.backgroundTintList = ColorStateList.valueOf(activeColorFull)
        binding.wordsChipCount.backgroundTintList = ColorStateList.valueOf(activeColorMini)
        binding.wordsChipTtl.setTextColor(activeColorText)
        binding.wordsChipCount.setTextColor(activeColorMiniText)
        binding.wordsChipTtl.alpha = alphaNoTrasparent
        binding.wordsChipCount.alpha = alphaNoTrasparent
    }

    fun showChipSentences() {
        if(tpDisplay == GlobalConsts.TP_SENTENCE) return
        hideChipWords()
        hideChipAll()
        binding.chipSentences.backgroundTintList = ColorStateList.valueOf(activeColorFull)
        binding.sentencesChipCount.backgroundTintList = ColorStateList.valueOf(activeColorMini)
        binding.sentencesChipTtl.setTextColor(activeColorText)
        binding.sentencesChipCount.setTextColor(activeColorMiniText)
        binding.sentencesChipTtl.alpha = alphaNoTrasparent
        binding.sentencesChipCount.alpha = alphaNoTrasparent
    }

    fun hideChipAll() {
        binding.chipAll.setCardBackgroundColor(inactiveColorFull)
        binding.tvChipAll.setTextColor(inactiveColorText)
        binding.tvChipAll.alpha = alphaTransparent
    }

    fun hideChipWords() {
        binding.chipWords.backgroundTintList = ColorStateList.valueOf(inactiveColorFull)
        binding.wordsChipCount.backgroundTintList = ColorStateList.valueOf(inactiveColorMini)
        binding.wordsChipTtl.setTextColor(activeColorText)
        binding.wordsChipCount.setTextColor(inactiveColorMiniText)
        binding.wordsChipTtl.alpha = alphaTransparent
        binding.wordsChipCount.alpha = alphaTransparentCircle
    }

    fun hideChipSentences() {
        binding.chipSentences.backgroundTintList = ColorStateList.valueOf(inactiveColorFull)
        binding.sentencesChipCount.backgroundTintList = ColorStateList.valueOf(inactiveColorMini)
        binding.sentencesChipTtl.setTextColor(activeColorText)
        binding.sentencesChipCount.setTextColor(inactiveColorMiniText)
        binding.sentencesChipTtl.alpha = alphaTransparent
        binding.sentencesChipCount.alpha = alphaTransparentCircle
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}