package com.istnetworks.hivesdk.presentation.fullScreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import com.istnetworks.hivesdk.R
import com.istnetworks.hivesdk.data.repository.HiveSDKRepositoryImpl
import com.istnetworks.hivesdk.data.utils.QuestionType
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
        createFragments()
        return binding.root
    }

    private fun createFragments() {
        for (position in 0 until viewModel.survey?.questions!!.size){
            val question = viewModel.findQuestion(position)
            val frameLayout = FrameLayout(requireContext())
            frameLayout.id = position
            childFragmentManager.commit {
                setReorderingAllowed(true)
                add(frameLayout.id,getFragmentFromType(question!!.questionType,position))
            }
            binding.hveMain.addView(frameLayout)

        }
    }

    private fun getFragmentFromType(it: Int?, position: Int): Fragment {
        return when (it) {
            QuestionType.MultipleChoiceQuestion.value -> MultipleChoicesFragment.newInstance(position)
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