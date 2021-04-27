package com.istnetworks.hivesdk.presentation.mainfragment.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.istnetworks.hivesdk.presentation.emojis.EmojiFragment
import com.istnetworks.hivesdk.presentation.multipleChoices.MultipleChoicesFragment
import com.istnetworks.hivesdk.presentation.nps.NpsFragment
import com.istnetworks.hivesdk.presentation.singleChoice.SingleChoiceFragment
import com.istnetworks.hivesdk.presentation.spinnerquestion.SpinnerQuestionFragment
import java.util.*

/**
 * Created by khairy on ن, 06/ماي/2019 at 03:15 م.
 *
 */
class HorizontalPagerAdapter(f: Fragment) : FragmentStateAdapter(f) {

    private val mFragmentList = mutableListOf<Fragment>( NpsFragment(),EmojiFragment(),
        SingleChoiceFragment(),
        MultipleChoicesFragment())
    fun addFragment(fragment: Fragment) {
        mFragmentList.add(fragment)
    }

    override fun getItemCount(): Int {
        return mFragmentList.size
    }


    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> SpinnerQuestionFragment.getInstance(0)
            1 -> EmojiFragment()
            2 -> SingleChoiceFragment.newInstance(1)
            3 -> MultipleChoicesFragment.newInstance(0)
            else -> Fragment()
        }
    }
}