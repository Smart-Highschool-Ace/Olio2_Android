package org.gsm.olio.view.fragment.guide

import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class GuidePageAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    private val fragmentList = listOf<Fragment>(FirstGuideFragment(),SecondeGuideFragment(),ThirdGuideFragment())

    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }
}