package com.istnetworks.hivesdk.presentation.mainfragment.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.istnetworks.hivesdk.data.models.response.Question
import com.istnetworks.hivesdk.data.utils.QuestionType
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

/**
 * Created by khairy on ن, 06/ماي/2019 at 03:15 م.
 *
 */
class HorizontalPagerAdapter(f: Fragment) : FragmentStateAdapter(f) {

    private val mFragmentList : MutableList<Fragment> = mutableListOf()
    fun setData(questions: List<Question?>) {
        mFragmentList.clear()
        mFragmentList.addAll(questions.mapIndexed { questionPosition, it ->
            getFragmentFromType(
                it?.questionType,
                questionPosition
            )
        })
    }
   fun getFragmentByPosition(fragmentPosition: Int): Fragment {
        return mFragmentList.get(fragmentPosition)
    }

    private fun getFragmentFromType(it: Int?, questionPosition: Int): Fragment {
        return when (it) {
            QuestionType.MultipleChoiceQuestion.value -> MultipleChoicesFragment.newInstance(questionPosition)
            QuestionType.ListQuestion.value -> SpinnerQuestionFragment.getInstance(questionPosition)
            QuestionType.DateQuestion.value -> DatePickerQuestionFragment.getInstance(questionPosition)
            QuestionType.SlideQuestion.value -> SliderQuestionFragment.getInstance(questionPosition)
            QuestionType.StarQuestion.value -> RatingFragment.getInstance(questionPosition)
            QuestionType.NPS.value -> NpsFragment.getInstance(questionPosition)
            QuestionType.TextInput.value -> FreeInputsFragment.getInstance(questionPosition)
            QuestionType.NumberInput.value -> FreeInputsFragment.getInstance(questionPosition)
            QuestionType.EmailInput.value -> FreeInputsFragment.getInstance(questionPosition)
            QuestionType.PhoneNumberInput.value -> FreeInputsFragment.getInstance(questionPosition)
            QuestionType.PostalCodeInput.value -> FreeInputsFragment.getInstance(questionPosition)
            QuestionType.URLInput.value -> FreeInputsFragment.getInstance(questionPosition)
            QuestionType.TextInput.value -> FreeInputsFragment.getInstance(questionPosition)
            QuestionType.NumberInput.value -> FreeInputsFragment.getInstance(questionPosition)
            QuestionType.EmailInput.value -> FreeInputsFragment.getInstance(questionPosition)
            QuestionType.PhoneNumberInput.value -> FreeInputsFragment.getInstance(questionPosition)
            QuestionType.PostalCodeInput.value -> FreeInputsFragment.getInstance(questionPosition)
            QuestionType.URLInput.value -> FreeInputsFragment.getInstance(questionPosition)
            QuestionType.TextInput.value -> FreeInputsFragment.getInstance(questionPosition)
            QuestionType.NumberInput.value -> FreeInputsFragment.getInstance(questionPosition)
            QuestionType.EmailInput.value -> FreeInputsFragment.getInstance(questionPosition)
            QuestionType.PhoneNumberInput.value -> FreeInputsFragment.getInstance(questionPosition)
            QuestionType.PostalCodeInput.value -> FreeInputsFragment.getInstance(questionPosition)
            QuestionType.URLInput.value -> FreeInputsFragment.getInstance(questionPosition)
            QuestionType.SingleChoice.value -> SingleChoiceFragment.newInstance(questionPosition)
            QuestionType.Emoji.value -> EmojiFragment.getInstance(questionPosition)
            QuestionType.ImageMCQ.value -> MultipleImageChoiceFragment.getInstance(questionPosition)
            QuestionType.ImageSingleChoice.value -> SingleImageChoiceFragment.getInstance(questionPosition)
            QuestionType.CSAT.value -> SingleChoiceFragment.newInstance(questionPosition)
            null ->SpinnerQuestionFragment.getInstance(questionPosition)
            else -> SpinnerQuestionFragment.getInstance(questionPosition)
        }
    }


    override fun getItemCount(): Int {
        return mFragmentList.size
    }


    override fun createFragment(position: Int): Fragment {
        return mFragmentList[position]
    }
}