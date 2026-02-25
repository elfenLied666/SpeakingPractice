package com.justclient.speakingpractice.data.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.justclient.speakingpractice.ui.fragments.NeedsReviewFragment
import com.justclient.speakingpractice.ui.fragments.PracticeHistoryFragment

class SpeakingPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    // У нас по-прежнему две вкладки
    override fun getItemCount(): Int = 2

    // Метод, который создает нужный фрагмент для нужной позиции
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> PracticeHistoryFragment()
            1 -> NeedsReviewFragment()
            else -> throw IllegalStateException("Invalid position $position")
        }
    }
}
