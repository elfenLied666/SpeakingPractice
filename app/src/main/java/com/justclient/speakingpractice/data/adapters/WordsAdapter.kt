package com.justclient.speakingpractice.data.adapters

import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.AnimationDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.justclient.speakingpractice.R
import com.justclient.speakingpractice.data.classes.Word
import com.justclient.speakingpractice.databinding.ItemPracticeWordBinding

class WordsAdapter(
    private val onWordClick: (Word, Int) -> Unit,
    private val onDeleteClick: (Word) -> Unit,
    private val onVolumeClick: (Word) -> Unit
) : ListAdapter<Word, WordsAdapter.WordViewHolder>(WordDiffCallback()) {

    private val selectedItemIds = mutableSetOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val binding = ItemPracticeWordBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WordViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        val word = getItem(position)
        holder.bind(word)
    }

    fun getSelectedItems(): List<String> {
        return selectedItemIds.toList()
    }

    fun clearSelections() {
        selectedItemIds.clear()
        notifyDataSetChanged()
    }

    inner class WordViewHolder(private val binding: ItemPracticeWordBinding) : RecyclerView.ViewHolder(binding.root) {

        private val selectedStrokeColor: Int = ContextCompat.getColor(binding.root.context, R.color.item_card_border_color) // Ваш синий цвет для рамки
        private val unselectedStrokeColor: Int = ContextCompat.getColor(binding.root.context, R.color.white)
        private val selectedStrokeWidth: Int = (3 * binding.root.resources.displayMetrics.density).toInt()
        private val unselectedStrokeWidth: Int = (1 * binding.root.resources.displayMetrics.density).toInt()

        fun bind(word: Word) {
            binding.enWordText.text = word.enWord
            binding.spWordText.text = word.spWord

            val isSelected = selectedItemIds.contains(word.id)
            val animationDrawable = binding.volumeIcon.drawable as? AnimationDrawable
            if (isSelected) {
                binding.cardRoot.strokeColor = selectedStrokeColor
                binding.cardRoot.strokeWidth = selectedStrokeWidth
                animationDrawable?.start()
            } else {
                binding.cardRoot.strokeColor = unselectedStrokeColor
                binding.cardRoot.strokeWidth = unselectedStrokeWidth
                animationDrawable?.stop()
                animationDrawable?.selectDrawable(0)
            }
           // binding.selectionBorder.visibility = if (isSelected) View.VISIBLE else View.GONE
            binding.root.setOnClickListener {
                if (selectedItemIds.contains(word.id)) {
                    selectedItemIds.remove(word.id)
                } else {
                    selectedItemIds.add(word.id)
                }
                notifyItemChanged(adapterPosition)
                onWordClick(word, adapterPosition)
            }
            binding.deleteButton.setOnClickListener {
                if (selectedItemIds.contains(word.id)) {
                    selectedItemIds.remove(word.id)
                }
                onDeleteClick(word)
            }
            binding.volumeButton.setOnClickListener {
                val icon = binding.volumeIcon.drawable
                if (icon is AnimatedVectorDrawable) {
                    icon.start()
                }
                onVolumeClick(word)
            }
        }
    }
}

class WordDiffCallback : DiffUtil.ItemCallback<Word>() {
    override fun areItemsTheSame(oldItem: Word, newItem: Word): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Word, newItem: Word): Boolean {
        return oldItem == newItem
    }
}
