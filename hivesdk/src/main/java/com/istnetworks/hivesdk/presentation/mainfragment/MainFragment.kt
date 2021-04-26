package com.istnetworks.hivesdk.presentation.mainfragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.istnetworks.hivesdk.R
import com.istnetworks.hivesdk.data.repository.HiveSDKRepositoryImpl
import com.istnetworks.hivesdk.databinding.FragmentMainBinding
import com.istnetworks.hivesdk.presentation.mainfragment.adapter.HorizontalPagerAdapter
import com.istnetworks.hivesdk.presentation.surveyExtension.surveyTitleStyle
import com.istnetworks.hivesdk.presentation.viewmodel.HiveSDKViewModel
import com.istnetworks.hivesdk.presentation.viewmodel.factory.HiveSDKViewModelFactory

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private val viewModel: HiveSDKViewModel by activityViewModels {
        HiveSDKViewModelFactory(
            HiveSDKRepositoryImpl()
        )
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater)
        initializeViewPager()
        bindViews()
        return binding.root
    }

    private fun bindViews() {
        binding.tvSurveyTitle.text=viewModel.survey?.title
     //   binding.hveIvIcon.
        binding.tvSurveyTitle.surveyTitleStyle(viewModel.getSurveyTheme()?.surveyTitleStyle)
        binding.clParent.setBackgroundColor(Color.parseColor("#"+viewModel.getSurveyTheme()?.surveyBackgroundColor))
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