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
import com.istnetworks.hivesdk.data.utils.HiveSDKType
import com.istnetworks.hivesdk.data.utils.extensions.onClick
import com.istnetworks.hivesdk.data.utils.extensions.showToast
import com.istnetworks.hivesdk.databinding.FragmentMainBinding
import com.istnetworks.hivesdk.presentation.mainfragment.adapter.HorizontalPagerAdapter
import com.istnetworks.hivesdk.presentation.mainfragment.adapter.PagerAdapter
import com.istnetworks.hivesdk.presentation.mainfragment.adapter.VerticalListViewAdapter
import com.istnetworks.hivesdk.presentation.surveyExtension.surveyLogoStyle
import com.istnetworks.hivesdk.presentation.surveyExtension.surveyTitleStyle
import com.istnetworks.hivesdk.presentation.viewmodel.HiveSDKViewModel
import com.istnetworks.hivesdk.presentation.viewmodel.factory.HiveSDKViewModelFactory


class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    lateinit var onPageChangeCallback: ViewPager2.OnPageChangeCallback

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
        onClickActions()
        listenToViewPagerChanges()
        return binding.root
    }

    private fun listenToViewPagerChanges() {
        onPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding.hveIvPrevious.isEnabled = position > 0
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
        }
        binding.hveViewPager.registerOnPageChangeCallback(onPageChangeCallback)
    }


    private fun onClickActions() {
        binding.hveIvNext.onClick {
            onNextArrowClicked()
        }
        binding.hveIvPrevious.onClick {
            val newPosition =
                viewModel.getThePreviousPosition(binding.hveViewPager.currentItem)
            binding.hveViewPager.currentItem = newPosition
        }
    }

    private fun onNextArrowClicked() {
        if (viewModel.validateAnswer(binding.hveViewPager.currentItem)) {
            navigateToNextQuestion()
        } else {
            showToast(getString(R.string.question_is_required))
        }
    }

    private fun navigateToNextQuestion() {
        val newPosition =
            viewModel.getTheNextQuestionPosition(binding.hveViewPager.currentItem)
        binding.hveViewPager.currentItem = newPosition
    }

    private fun bindViews() {
        binding.hveIvIcon.surveyLogoStyle(viewModel.getSurveyTheme()?.surveyLogoStyle!!)
        binding.tvSurveyTitle.text = viewModel.survey?.title
        binding.tvSurveyTitle.surveyTitleStyle(viewModel.getSurveyTheme()?.surveyTitleStyle)
        binding.clParent.setBackgroundColor(Color.parseColor("#" + viewModel.getSurveyTheme()?.surveyBackgroundColor))
    }

    private fun initializeViewPager() {
        binding.hveViewPager.isUserInputEnabled = false
        val adapter = HorizontalPagerAdapter(this)
        adapter.setData(viewModel.survey?.questions ?: listOf())
        binding.hveViewPager.apply {
            offscreenPageLimit = 1
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
        }
        binding.hveViewPager.adapter = adapter
    }

    private fun initializeRecyclerView(){
        val pagerAdapter = PagerAdapter(childFragmentManager)
        pagerAdapter.setData(viewModel.survey?.questions?: listOf())
        binding.lvQuestions.adapter= pagerAdapter
    }
    override fun onDestroyView() {
        super.onDestroyView()
        binding.hveViewPager.unregisterOnPageChangeCallback(onPageChangeCallback)
    }
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