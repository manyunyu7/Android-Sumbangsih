package com.feylabs.sumbangsih.presentation._walkthrough

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class WalktroughAdapter(fm: FragmentManager, lf: Lifecycle) : FragmentStateAdapter(fm, lf) {

    private val fragmentList = arrayListOf<Fragment>(
        OnBoardFragment(1),
        OnBoardFragment(2),
        OnBoardFragment(3),
        OnBoardFragment(4),
    )

    override fun getItemCount(): Int = fragmentList.size

    override fun createFragment(position: Int): Fragment = fragmentList[position]
}