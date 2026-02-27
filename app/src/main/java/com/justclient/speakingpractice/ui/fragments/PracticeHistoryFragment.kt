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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.justclient.speakingpractice.R
import com.justclient.speakingpractice.data.adapters.WordsAdapter
import com.justclient.speakingpractice.data.models.MainViewModel
import com.justclient.speakingpractice.databinding.DialogDeleteConfirmationBinding
import com.justclient.speakingpractice.databinding.FragmentPracticeHistoryBinding
import com.justclient.speakingpractice.utils.GlobalConsts
import kotlinx.coroutines.launch
import kotlin.getValue

class PracticeHistoryFragment(var tpDisplay: Int?) : Fragment() {

    private var _binding: FragmentPracticeHistoryBinding? = null
    private val binding get() = _binding!!

    //var tpDisplay: Int? = null
    private lateinit var wordsAdapter: WordsAdapter

    // Флаг, который отслеживает, находимся ли мы в режиме редактирования
    private var isEditMode = false

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
        if(tpDisplay == GlobalConsts.TP_ALL) {
            viewModel.setFilter(GlobalConsts.TP_ALL)
        } else if(tpDisplay == GlobalConsts.TP_WORD) {
            viewModel.setFilter(GlobalConsts.TP_WORD)
        } else if(tpDisplay == GlobalConsts.TP_SENTENCE) {
            viewModel.setFilter(GlobalConsts.TP_SENTENCE)
        } else {
            viewModel.setFilter(GlobalConsts.TP_ALL)
        }
        initResources()
        updateEditModeUI()
        setupRecyclerView()
        observeViewModel()

        /*viewLifecycleOwner.lifecycleScope.launch {
            viewModel.filteredWords.collect { wordsList ->
                //wordsAdapter.submitList(wordsList)
            }
        }*/

        binding.chipAll.setOnClickListener {
            //showChipAll()
            viewModel.setFilter(GlobalConsts.TP_ALL)
        }

        binding.chipWords.setOnClickListener {
            //showChipWords()
            viewModel.setFilter(GlobalConsts.TP_WORD)
        }

        binding.chipSentences.setOnClickListener {
            //showChipSentences()
            viewModel.setFilter(GlobalConsts.TP_SENTENCE)
        }

        binding.iconEdit.setOnClickListener {
            isEditMode = !isEditMode
            //if (!isEditMode) wordsAdapter.clearSelections()
            updateEditModeUI()
        }

        binding.iconDelete.setOnClickListener {
            val selectedIds = wordsAdapter.getSelectedItems()
            if (selectedIds.isNotEmpty()) {
                showDeleteConfirmationDialog {
                    selectedIds.forEach { id -> viewModel.deleteWord(id) }
                    isEditMode = false
                    wordsAdapter.clearSelections()
                    updateEditModeUI()
                }
            }
        }
    }

    private fun initResources() {
        activeColorFull = ContextCompat.getColor(requireContext(), R.color.lbl_act)
        inactiveColorFull = ContextCompat.getColor(requireContext(), R.color.lbl_no_act_full)
        activeColorText = ContextCompat.getColor(requireContext(), R.color.lbl_act_text)
        inactiveColorText = ContextCompat.getColor(requireContext(), R.color.lbl_no_act_text)
        activeColorMini = ContextCompat.getColor(requireContext(), R.color.lbl_mini_circle_act)
        inactiveColorMini = ContextCompat.getColor(requireContext(), R.color.lbl_mini_circle_no_act)
        inactiveColorMiniText = ContextCompat.getColor(requireContext(), R.color.lbl_mini_text_no_act)
        activeColorMiniText = ContextCompat.getColor(requireContext(), R.color.lbl_mini_text_act)
    }

    fun showChipAll(isFunCall: Boolean = false) {
        if(tpDisplay == GlobalConsts.TP_ALL && !isFunCall) return
        hideChipWords()
        hideChipSentences()
        binding.chipAll.setCardBackgroundColor(activeColorFull)
        binding.tvChipAll.setTextColor(activeColorText)
        binding.tvChipAll.alpha = alphaNoTrasparent
    }

    fun showChipWords(isFunCall: Boolean = false) {
        if(tpDisplay == GlobalConsts.TP_WORD && !isFunCall) return
        hideChipAll()
        hideChipSentences()
        binding.chipWords.backgroundTintList = ColorStateList.valueOf(activeColorFull)
        binding.wordsChipCount.backgroundTintList = ColorStateList.valueOf(activeColorMini)
        binding.wordsChipTtl.setTextColor(activeColorText)
        binding.wordsChipCount.setTextColor(activeColorMiniText)
        binding.wordsChipTtl.alpha = alphaNoTrasparent
        binding.wordsChipCount.alpha = alphaNoTrasparent
    }

    fun showChipSentences(isFunCall: Boolean = false) {
        if(tpDisplay == GlobalConsts.TP_SENTENCE && !isFunCall) return
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

    private fun setupRecyclerView() {
        wordsAdapter = WordsAdapter(
            onWordClick = { word, position ->
                if (isEditMode) {
                    updateEditModeUI()
                }
            },
            onDeleteClick = { word ->
                showDeleteConfirmationDialog {
                    viewModel.deleteWord(word.id)
                }
            },
            onVolumeClick = { word ->

            }
        )

        binding.historyRecyclerView.apply {
            adapter = wordsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.filteredWords.collect { wordsList ->
                wordsAdapter.submitList(wordsList)
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.currentFilter.collect { filter ->
                val actualFilter = filter ?: GlobalConsts.TP_ALL
                updateFilterChipUI(actualFilter)
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.wordsCount.collect { count ->
                    binding.wordsChipCount.text = count.toString()
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.sentencesCount.collect { count ->
                    binding.sentencesChipCount.text = count.toString()
                }
            }
        }
    }

    private fun updateEditModeUI() {
        if (isEditMode) {
            binding.iconDelete.visibility = View.VISIBLE //if (wordsAdapter.getSelectedItems().isNotEmpty()) View.VISIBLE else View.GONE
           // binding.iconEdit.setImageResource(R.drawable.ic_done)
        } else {
            binding.iconDelete.visibility = View.GONE
            binding.iconEdit.setImageResource(R.drawable.ic_edit_square)
        }
    }

    private fun updateFilterChipUI(selectedFilter: Int) {
        /*hideChipAll()
        hideChipWords()
        hideChipSentences()*/

        // Обновляем переменную класса
        tpDisplay = selectedFilter

        // "Включаем" нужный
        when (selectedFilter) {
            GlobalConsts.TP_ALL -> showChipAll(true)
            GlobalConsts.TP_WORD -> showChipWords(true)
            GlobalConsts.TP_SENTENCE -> showChipSentences(true)
        }
    }

    private fun showDeleteConfirmationDialog(onConfirm: () -> Unit) {
        val dialog = android.app.Dialog(requireContext())
        val dialogBinding = DialogDeleteConfirmationBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)

        // Делаем фон системного окна прозрачным, чтобы видеть наши закругленные углы
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        dialogBinding.btnCancel.setOnClickListener { dialog.dismiss() }
        dialogBinding.btnClose.setOnClickListener { dialog.dismiss() }

        dialogBinding.btnConfirmDelete.setOnClickListener {
            onConfirm()
            dialog.dismiss()
        }

        dialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}