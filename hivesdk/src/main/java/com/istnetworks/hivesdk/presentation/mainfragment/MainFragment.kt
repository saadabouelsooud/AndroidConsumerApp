package com.istnetworks.hivesdk.presentation.mainfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.istnetworks.hivesdk.data.repository.HiveSDKRepositoryImpl
import com.istnetworks.hivesdk.data.utils.extensions.hide
import com.istnetworks.hivesdk.data.utils.extensions.onClick
import com.istnetworks.hivesdk.data.utils.extensions.show
import com.istnetworks.hivesdk.databinding.FragmentMainBinding
import com.istnetworks.hivesdk.presentation.interfaces.SubmitButtonControl
import com.istnetworks.hivesdk.presentation.interfaces.IsRequiredInterface
import com.istnetworks.hivesdk.presentation.interfaces.ValidationErrorInterface
import com.istnetworks.hivesdk.presentation.mainfragment.adapter.HorizontalPagerAdapter
import com.istnetworks.hivesdk.presentation.mainfragment.adapter.PagerAdapter
import com.istnetworks.hivesdk.presentation.surveyExtension.cardsBackground
import com.istnetworks.hivesdk.presentation.surveyExtension.surveyLogoStyle
import com.istnetworks.hivesdk.presentation.surveyExtension.surveyTitleStyle
import com.istnetworks.hivesdk.presentation.viewmodel.HiveSDKViewModel
import com.istnetworks.hivesdk.presentation.viewmodel.factory.HiveSDKViewModelFactory


class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    lateinit var onPageChangeCallback: ViewPager2.OnPageChangeCallback
    lateinit var horizontalPagerAdapter: HorizontalPagerAdapter

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
        setupProgressSlider()
        listenToViewPagerChanges()
        observeViewModel()
        return binding.root
    }


    private fun observeViewModel() {
        viewModel.updateProgressSliderLD.observe(viewLifecycleOwner, {
            binding.hveSliderProgress.value = it
        })
        viewModel.showNotValidErrMsgLD.observe(viewLifecycleOwner, {
            val f =
                horizontalPagerAdapter.getFragmentByPosition(binding.hveViewPager.currentItem)
            if (it == true) {
                val questionType =
                    viewModel.findQuestion(binding.hveViewPager.currentItem)?.questionType
                (f as ValidationErrorInterface).showNotValidError(questionType)
            }

        })
        viewModel.showIsRequiredErrMsgLD.observe(viewLifecycleOwner, {
            val f =
                horizontalPagerAdapter.getFragmentByPosition(binding.hveViewPager.currentItem)
            if (it == true) {
                (f as IsRequiredInterface).showIsRequiredError()
            } else {
                (f as IsRequiredInterface).hideIsRequiredError()
            }
        })

//        viewModel.enableNextButton.observe(viewLifecycleOwner,{
//            binding.hveIvNext.isEnabled = it
//        })

        viewModel.showSubmitButton.observe(viewLifecycleOwner,{
            val f =
                horizontalPagerAdapter.getFragmentByPosition(binding.hveViewPager.currentItem)
            if(it == true){
                (f as SubmitButtonControl).showSubmitButton()
            }
            else
            {
                (f as SubmitButtonControl).hideSubmitButton()
            }
        })
    }

    private fun setupProgressSlider() {
        if (viewModel.survey?.surveyOptions?.hasProgressBar == true) {
            binding.hveSliderProgress.isEnabled = false
            binding.hveSliderProgress.thumbRadius=0
            binding.hveSliderProgress.show()
            binding.hveSliderProgress.valueFrom = 0f
            binding.hveSliderProgress.stepSize = 1f
            binding.hveSliderProgress.valueTo = viewModel.survey?.questions?.size?.toFloat() ?: 1f
        } else {
            binding.hveSliderProgress.hide()
        }
    }

    private fun listenToViewPagerChanges() {
        onPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding.hveIvPrevious.isEnabled = position > 0

                val fragment = childFragmentManager.findFragmentByTag("f$position")
                if (fragment != null) {
                    fragment.view?.let {
                        // Now we've got access to the fragment Root View
                        // we will use it to calculate the height and
                        // apply it to the ViewPager2
                        it.requestLayout()
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
                viewModel.getThePreviousPosition()
            binding.hveViewPager.setCurrentItem(newPosition,false)
        }

        binding.hveIvClose.onClick {
            requireActivity().finish()
        }
    }

    private fun onNextArrowClicked() {
        if (viewModel.validateAnswer(binding.hveViewPager.currentItem)) {
            navigateToNextQuestion()
        }
    }

    private fun navigateToNextQuestion() {
        val newPosition =
            viewModel.getTheNextQuestionPosition(binding.hveViewPager.currentItem)

        binding.hveViewPager.setCurrentItem(newPosition,false)
    }

    private fun bindViews() {
        binding.hveIvIcon.surveyLogoStyle(viewModel.getSurveyTheme()?.surveyLogoStyle!!)
        binding.tvSurveyTitle.text = viewModel.survey?.title
        binding.tvSurveyTitle.surveyTitleStyle(viewModel.getSurveyTheme()?.surveyTitleStyle)
        binding.hveClHeader.cardsBackground(requireContext(), viewModel.surveyBackgroundColor())
        binding.hveClQuestionHolder.cardsBackground(
            requireContext(),
            viewModel.surveyBackgroundColor()
        )
        binding.hveIvClose.visibility =
            if (viewModel.getSurveyOptions()?.enableCloseButton == true) {
                View.VISIBLE
            } else {
                View.GONE
            }

    }

    private fun initializeViewPager() {
        binding.hveViewPager.isUserInputEnabled = false
        horizontalPagerAdapter = HorizontalPagerAdapter(this)
        horizontalPagerAdapter.setData(viewModel.survey?.questions ?: listOf())
        binding.hveViewPager.apply {
            offscreenPageLimit = 1
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
        }
        binding.hveViewPager.adapter = horizontalPagerAdapter
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