package com.istnetworks.hivesdk.presentation.fullScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import com.istnetworks.hivesdk.data.models.response.Question
import com.istnetworks.hivesdk.data.repository.HiveSDKRepositoryImpl
import com.istnetworks.hivesdk.data.utils.QuestionType
import com.istnetworks.hivesdk.data.utils.extensions.toPx
import com.istnetworks.hivesdk.databinding.FragmentVerticalMainBinding
import com.istnetworks.hivesdk.presentation.datepickerquestion.DatePickerQuestionFragment
import com.istnetworks.hivesdk.presentation.emojis.EmojiFragment
import com.istnetworks.hivesdk.presentation.freeinputs.FreeInputsFragment
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
    private lateinit var binding:FragmentVerticalMainBinding
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
        createFragments()
        return binding.root
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

    private fun createFragments() {
        for (position in 0 until viewModel.survey?.questions!!.size) {
            val question = viewModel.findQuestion(position)
            val frameLayout = generateFrameLayout(position)
            addFragmentToFrame(frameLayout, question, position)
            binding.hveMain.addView(frameLayout)

        }
    }

    private fun addFragmentToFrame(f: FrameLayout, q: Question?, position: Int) {
        childFragmentManager.commit {
            setReorderingAllowed(true)
            add(f.id, getFragmentFromType(q!!.questionType, position))
        }
    }

    private fun generateFrameLayout(position: Int): FrameLayout {
        val frameLayout = FrameLayout(requireContext())
        frameLayout.id = position
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