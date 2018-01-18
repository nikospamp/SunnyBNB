package com.example.ptuxiaki.sunnybnb.ui.Reservations

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

/**
 * Created by Pampoukidis on 18/1/2018.
 */
class ViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private val mFm = fm
    private val mFragmentItems: ArrayList<Fragment> = ArrayList()
    private val mFragmentTitles: ArrayList<String> = ArrayList()

    fun addFragments(fragment: Fragment, fragmentTitle: String) {
        mFragmentItems.add(fragment)
        mFragmentTitles.add(fragmentTitle)
    }

    override fun getItem(position: Int): Fragment =
            mFragmentItems[position]


    override fun getCount(): Int =
            mFragmentItems.size

    override fun getPageTitle(position: Int): CharSequence? =
            mFragmentTitles[position]


}