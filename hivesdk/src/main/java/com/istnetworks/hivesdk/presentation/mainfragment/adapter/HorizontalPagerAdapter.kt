package com.istnetworks.hivesdk.presentation.mainfragment.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.istnetworks.hivesdk.data.models.response.Question
import com.istnetworks.hivesdk.data.utils.QuestionType
import com.istnetworks.hivesdk.presentation.datepickerquestion.DatePickerQuestionFragment
import com.istnetworks.hivesdk.presentation.emojis.EmojiFragment
import com.istnetworks.hivesdk.presentation.freeinputs.FreeInputsFragment
import com.istnetworks.hivesdk.presentation.multipleChoices.MultipleChoicesFragment
import com.istnetworks.hivesdk.presentation.nps.NpsFragment
import com.istnetworks.hivesdk.presentation.singleChoice.SingleChoiceFragment
import com.istnetworks.hivesdk.presentation.spinnerquestion.SpinnerQuestionFragment

/**
 * Created by khairy on ن, 06/ماي/2019 at 03:15 م.
 *
 */
class HorizontalPagerAdapter(f: Fragment) : FragmentStateAdapter(f) {

    private val mFragmentList : MutableList<Fragment> = mutableListOf()
    fun setData(questions: List<Question?>) {
        mFragmentList.clear()
        mFragmentList.addAll(questions.mapIndexed { position, it ->
            getFragmentFromType(
                it?.questionType,
                position
            )
        })
    }

    private fun getFragmentFromType(it: Int?, position: Int): Fragment {
        return when (it) {
            QuestionType.MultipleChoiceQuestion.value -> MultipleChoicesFragment.newInstance(position)
            QuestionType.ListQuestion.value -> SpinnerQuestionFragment.getInstance(position)
            QuestionType.DateQuestion.value -> DatePickerQuestionFragment.getInstance(position)
            QuestionType.SlideQuestion.value -> SpinnerQuestionFragment.getInstance(position)
            QuestionType.StarQuestion.value -> SpinnerQuestionFragment.getInstance(position)
            QuestionType.NPS.value -> NpsFragment()
            QuestionType.TextInput.value -> FreeInputsFragment.getInstance(position)
            QuestionType.NumberInput.value -> FreeInputsFragment.getInstance(position)
            QuestionType.EmailInput.value -> FreeInputsFragment.getInstance(position)
            QuestionType.PhoneNumberInput.value -> FreeInputsFragment.getInstance(position)
            QuestionType.PostalCodeInput.value -> FreeInputsFragment.getInstance(position)
            QuestionType.URLInput.value -> FreeInputsFragment.getInstance(position)
            QuestionType.SingleChoice.value -> SingleChoiceFragment.newInstance(position)
            QuestionType.Emoji.value -> EmojiFragment()
            QuestionType.ImageMCQ.value -> SpinnerQuestionFragment.getInstance(position)
            QuestionType.ImageSingleChoice.value -> SpinnerQuestionFragment.getInstance(position)
            QuestionType.CSAT.value -> SpinnerQuestionFragment.getInstance(position)
            null ->SpinnerQuestionFragment.getInstance(position)
            else -> SpinnerQuestionFragment.getInstance(position)
        }
    }


    override fun getItemCount(): Int {
        return mFragmentList.size
    }


    override fun createFragment(position: Int): Fragment {
        return mFragmentList.get(position)
    }
}