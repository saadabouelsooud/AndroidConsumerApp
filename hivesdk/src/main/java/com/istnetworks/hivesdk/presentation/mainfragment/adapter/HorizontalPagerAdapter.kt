package com.istnetworks.hivesdk.presentation.mainfragment.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.istnetworks.hivesdk.presentation.emojis.EmojiFragment
import com.istnetworks.hivesdk.presentation.nps.NpsFragment
import java.util.*

/**
 * Created by khairy on ن, 06/ماي/2019 at 03:15 م.
 *
 */
class HorizontalPagerAdapter(f: Fragment) : FragmentStateAdapter(f) {

    private val mFragmentList = mutableListOf<Fragment>( NpsFragment(),EmojiFragment())
    fun addFragment(fragment: Fragment) {
        mFragmentList.add(fragment)
    }

    override fun getItemCount(): Int {
        return mFragmentList.size
    }


    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> NpsFragment()
            1 -> EmojiFragment()
            else -> Fragment()
        }
    }
}