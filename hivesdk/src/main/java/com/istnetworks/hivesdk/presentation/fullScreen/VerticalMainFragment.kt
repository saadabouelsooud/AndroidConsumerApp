package com.istnetworks.hivesdk.presentation.fullScreen

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.fragment.app.commitNow
import com.istnetworks.hivesdk.data.repository.HiveSDKRepositoryImpl
import com.istnetworks.hivesdk.data.utils.QuestionType
import com.istnetworks.hivesdk.data.utils.extensions.hide
import com.istnetworks.hivesdk.data.utils.extensions.show
import com.istnetworks.hivesdk.data.utils.extensions.toPx
import com.istnetworks.hivesdk.databinding.FragmentVerticalMainBinding
import com.istnetworks.hivesdk.presentation.datepickerquestion.DatePickerQuestionFragment
import com.istnetworks.hivesdk.presentation.emojis.EmojiFragment
import com.istnetworks.hivesdk.presentation.freeinputs.FreeInputsFragment
import com.istnetworks.hivesdk.presentation.interfaces.IsRequiredInterface
import com.istnetworks.hivesdk.presentation.interfaces.SubmitButtonInterface
import com.istnetworks.hivesdk.presentation.interfaces.ValidationErrorInterface
import com.istnetworks.hivesdk.presentation.multiImageChoice.MultipleImageChoiceFragment
import com.istnetworks.hivesdk.presentation.multipleChoices.MultipleChoicesFragment
import com.istnetworks.hivesdk.presentation.nps.NpsFragment
import com.istnetworks.hivesdk.presentation.rating.RatingFragment
import com.istnetworks.hivesdk.presentation.singleChoice.SingleChoiceFragment
import com.istnetworks.hivesdk.presentation.singleImageChoice.SingleImageChoiceFragment
import com.istnetworks.hivesdk.presentation.sliderquestion.SliderQuestionFragment
import com.istnetworks.hivesdk.presentation.spinnerquestion.SpinnerQuestionFragment
import com.istnetworks.hivesdk.presentation.surveyExtension.cardsBackground
import com.istnetworks.hivesdk.presentation.surveyExtension.surveyLogoStyle
import com.istnetworks.hivesdk.presentation.surveyExtension.surveyTitleStyle
import com.istnetworks.hivesdk.presentation.viewmodel.HiveSDKViewModel
import com.istnetworks.hivesdk.presentation.viewmodel.factory.HiveSDKViewModelFactory


class VerticalMainFragment : Fragment() {
    private lateinit var binding: FragmentVerticalMainBinding
    private var allFragments = mutableListOf<Fragment>()
    var displayedFragments: MutableList<Fragment> = mutableListOf()

    private val viewModel: HiveSDKViewModel by activityViewModels {
        HiveSDKViewModelFactory(
            HiveSDKRepositoryImpl()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentVerticalMainBinding.inflate(inflater)
        bindViews()
        generateAllFragmentsList()
        displayedFragments = getEligibleFragmentsFrom(0)
        addFragmentsToLayout(displayedFragments)
        observeViewModel()
        setupProgressSlider()
        return binding.root
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

    private fun generateAllFragmentsList() {
        val f = viewModel.survey?.questions?.mapIndexed { index, question ->
            getFragmentFromType(
                question.questionType,
                index
            )
        }
        allFragments.addAll(f ?: listOf())
    }

    private fun observeViewModel() {
        viewModel.updateProgressSliderLD.observe(viewLifecycleOwner, {
            if (it > 0)
                binding.hveSliderProgress.value = it
        })
        viewModel.nextPositionLD.observe(viewLifecycleOwner, { nextPosition ->
            val lastAnsweredQuestionPosition = viewModel.lastAnsweredQuestionPosition
            removeAfter(lastAnsweredQuestionPosition)
            if (nextPosition > -1) {
                val appendedFragments =
                    getEligibleFragmentsFrom(nextPosition)
                displayedFragments.addAll(appendedFragments)
                addFragmentsToLayout(displayedFragments)
                binding.hveMain.requestLayout()
            }
        })
        viewModel.showNotValidErrMsgLD.observe(viewLifecycleOwner, {
            val fragmentPosition = viewModel.findQuestionPosition(it.first)
            val f = allFragments[fragmentPosition]
            val questionType =
                viewModel.findQuestion(fragmentPosition)?.questionType
            if (it.second) {
                (f as ValidationErrorInterface).showNotValidError(questionType)
            } else {
                (f as ValidationErrorInterface).hideNotValidError(questionType)
            }


        })
        viewModel.showIsRequiredErrMsgLD.observe(viewLifecycleOwner, {
            val fragmentPosition = viewModel.findQuestionPosition(it.first)
            val f = allFragments[fragmentPosition]
            if (it.second) {
                (f as IsRequiredInterface).showIsRequiredError()
            } else {
                (f as IsRequiredInterface).hideIsRequiredError()
            }

        })

        viewModel.showSubmitButtonLD.observe(viewLifecycleOwner, {
            if (viewModel.lastAnsweredQuestionPosition == -1) return@observe
            val f =
                allFragments[viewModel.lastAnsweredQuestionPosition]

            if (f is SubmitButtonInterface)
                if (it == true)
                    (f as SubmitButtonInterface).showSubmitButton()
                else
                    (f as SubmitButtonInterface).hideSubmitButton()

        })

    }

    private fun removeAfter(lastAnsweredQuestionPosition: Int) {
        removeFromResponseList(lastAnsweredQuestionPosition+1)
        replaceWithNewInstance(lastAnsweredQuestionPosition+1)
        if (displayedFragments.lastIndex > lastAnsweredQuestionPosition){
            val counter = lastAnsweredQuestionPosition + 1
            while (counter <= displayedFragments.lastIndex) {
                childFragmentManager.commitNow {
                    remove(displayedFragments[counter])
                }
                Log.d("TAG", "backStackEntryCount: ${childFragmentManager.backStackEntryCount}")
                displayedFragments.removeAt(counter)
                binding.hveMain.removeViewAt(counter)

            }
        }

    }

    private fun removeFromResponseList(startFrom: Int) {
        for (index in startFrom until viewModel.survey?.questions?.size!!){
            val qGUID = viewModel.findQuestion(index)?.surveyQuestionGUID
            viewModel.removeResponseByQuestionGuid(qGUID)
        }
    }

    private fun replaceWithNewInstance(startFrom: Int){
        for (index in startFrom until viewModel.survey?.questions?.size!!){
            val question = viewModel.findQuestion(index)
            allFragments.removeAt(index)
            allFragments.add(index,getFragmentFromType(question?.questionType,index))
        }
    }

    private fun bindViews() {
        binding.hveIvIcon.surveyLogoStyle(viewModel.getSurveyTheme()?.surveyLogoStyle!!)
        binding.tvSurveyTitle.text = viewModel.survey?.title
        binding.tvSurveyTitle.surveyTitleStyle(viewModel.getSurveyTheme()?.surveyTitleStyle)
        binding.hveClHeader.cardsBackground(requireContext(), viewModel.surveyBackgroundColor())

        binding.hveIvClose.visibility =
            if (viewModel.getSurveyOptions()?.enableCloseButton == true) {
                View.VISIBLE
            } else {
                View.GONE
            }

    }

    private fun getEligibleFragmentsFrom(startFrom: Int): MutableList<Fragment> {
        val fragments = mutableListOf<Fragment>()
        for (position in startFrom until allFragments.size) {
            fragments.add(allFragments[position])
            if (viewModel.isCouldBeLastQuestion(position))
                break

        }
        return fragments
    }

    private fun addFragmentsToLayout(fragments: List<Fragment>) {
        for (position in fragments.indices) {
            if (binding.hveMain.children.none { it.id == position + 1 }) {
                val frameLayout = generateFrameLayout(position)
                binding.hveMain.addView(frameLayout)
                addFragmentToFrame(frameLayout, fragments[position])
                binding.hveMain.requestLayout()
            }
        }
    }

    private fun addFragmentToFrame(fl: FrameLayout, f: Fragment) {
        childFragmentManager.commitNow {
            disallowAddToBackStack();
            add(fl.id, f)
            Log.d("TAG", "addFragmentToFrame: ${fl.id}")


        }
        Log.d("TAG", "backStackEntryCount: ${childFragmentManager.backStackEntryCount}")
    }

    private fun generateFrameLayout(position: Int): FrameLayout {
        val frameLayout = FrameLayout(requireContext())
        frameLayout.id = position+1
        val layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams.topMargin = (8f).toPx(requireContext())
        frameLayout.layoutParams = layoutParams
        frameLayout.setPadding(
            8f.toPx(requireContext()),
            0,
            8f.toPx(requireContext()),
            0
        )
        frameLayout.cardsBackground(requireContext(), viewModel.surveyBackgroundColor())

        return frameLayout
    }


    private fun getFragmentFromType(it: Int?, position: Int): Fragment {
        return when (it) {
            QuestionType.MultipleChoiceQuestion.value -> MultipleChoicesFragment.newInstance(
                position
            )
            QuestionType.ListQuestion.value -> SpinnerQuestionFragment.getInstance(position)
            QuestionType.DateQuestion.value -> DatePickerQuestionFragment.getInstance(position)
            QuestionType.SlideQuestion.value -> SliderQuestionFragment.getInstance(position)
            QuestionType.StarQuestion.value -> RatingFragment.getInstance(position)
            QuestionType.NPS.value -> NpsFragment.getInstance(position)
            QuestionType.TextInput.value -> FreeInputsFragment.getInstance(position)
            QuestionType.NumberInput.value -> FreeInputsFragment.getInstance(position)
            QuestionType.EmailInput.value -> FreeInputsFragment.getInstance(position)
            QuestionType.PhoneNumberInput.value -> FreeInputsFragment.getInstance(position)
            QuestionType.PostalCodeInput.value -> FreeInputsFragment.getInstance(position)
            QuestionType.URLInput.value -> FreeInputsFragment.getInstance(position)
            QuestionType.TextInput.value -> FreeInputsFragment.getInstance(position)
            QuestionType.NumberInput.value -> FreeInputsFragment.getInstance(position)
            QuestionType.EmailInput.value -> FreeInputsFragment.getInstance(position)
            QuestionType.PhoneNumberInput.value -> FreeInputsFragment.getInstance(position)
            QuestionType.PostalCodeInput.value -> FreeInputsFragment.getInstance(position)
            QuestionType.URLInput.value -> FreeInputsFragment.getInstance(position)
            QuestionType.TextInput.value -> FreeInputsFragment.getInstance(position)
            QuestionType.NumberInput.value -> FreeInputsFragment.getInstance(position)
            QuestionType.EmailInput.value -> FreeInputsFragment.getInstance(position)
            QuestionType.PhoneNumberInput.value -> FreeInputsFragment.getInstance(position)
            QuestionType.PostalCodeInput.value -> FreeInputsFragment.getInstance(position)
            QuestionType.URLInput.value -> FreeInputsFragment.getInstance(position)
            QuestionType.SingleChoice.value -> SingleChoiceFragment.newInstance(position)
            QuestionType.Emoji.value -> EmojiFragment.getInstance(position)
            QuestionType.ImageMCQ.value -> MultipleImageChoiceFragment.getInstance(position)
            QuestionType.ImageSingleChoice.value -> SingleImageChoiceFragment.getInstance(position)
            QuestionType.CSAT.value -> SingleChoiceFragment.newInstance(position)
            null -> SpinnerQuestionFragment.getInstance(position)
            else -> SpinnerQuestionFragment.getInstance(position)
        }
    }
}