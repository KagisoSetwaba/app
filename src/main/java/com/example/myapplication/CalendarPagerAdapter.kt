package com.example.myapplication

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class CalendarPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private val fragments = listOf(DailyFragment(), WeeklyFragment(), MonthlyFragment())

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getCount(): Int {
        return fragments.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "Daily"
            1 -> "Weekly"
            2 -> "Monthly"
            else -> null
        }
    }

    fun getCurrentFragment(position: Int): Fragment {
        return fragments[position]
    }
}