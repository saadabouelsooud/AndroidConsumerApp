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
        binding.hveViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                // Because the fragment might or might not be created yet,
                // we need to check for the size of the fragmentManager
                // before accessing it.
                if (childFragmentManager.fragments.size > position) {
                    val fragment = childFragmentManager.fragments.get(position)
                    fragment.view?.let {
                        // Now we've got access to the fragment Root View
                        // we will use it to calculate the height and
                        // apply it to the ViewPager2
                        updatePagerHeightForChild(it, binding.hveViewPager)
                    }
                }
            }
        })
        val adapter = HorizontalPagerAdapter(this)
        binding.hveViewPager.apply {
            offscreenPageLimit = 1
        }
        binding.hveViewPager.adapter = adapter
    }

    // This function can sit in an Helper file, so it can be shared across your project.
    fun updatePagerHeightForChild(view: View, pager: ViewPager2 = binding.hveViewPager) {
        view.post {
            val wMeasureSpec =
                View.MeasureSpec.makeMeasureSpec(view.width, View.MeasureSpec.EXACTLY)
            val hMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            view.measure(wMeasureSpec, hMeasureSpec)

            if (pager.layoutParams.height != view.measuredHeight) {
                pager.layoutParams = (pager.layoutParams)
                    .also { lp ->
                        // applying Fragment Root View Height to
                        // the pager LayoutParams, so they match
                        lp.height = view.measuredHeight
                    }
            }
        }
    }
}