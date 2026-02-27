package com.justclient.speakingpractice.data.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.justclient.speakingpractice.ui.fragments.NeedsReviewFragment
import com.justclient.speakingpractice.ui.fragments.PracticeHistoryFragment

class SpeakingPagerAdapter(fragment: Fragment, var tpDisplay: Int?) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> PracticeHistoryFragment(tpDisplay)
            1 -> NeedsReviewFragment()
            else -> throw IllegalStateException("Invalid position $position")
        }
    }
}
