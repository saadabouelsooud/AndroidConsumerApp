package com.istnetworks.hivesdk.presentation.mainfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.istnetworks.hivesdk.R
import com.istnetworks.hivesdk.databinding.FragmentMainBinding
import com.istnetworks.hivesdk.presentation.mainfragment.adapter.HorizontalPagerAdapter

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater)
        initializeViewPager()

        return binding.root
    }

    private fun initializeViewPager() {
       // binding.hveViewPager.isUserInputEnabled = false
        val adapter = HorizontalPagerAdapter(this)
        binding.hveViewPager.apply {
            offscreenPageLimit = 1
        }
        binding.hveViewPager.adapter = adapter
    }


}