package com.istnetworks.hivesdk.presentation.mainfragment.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.istnetworks.hivesdk.R
import com.istnetworks.hivesdk.data.models.response.Question
import com.istnetworks.hivesdk.data.utils.QuestionType
import com.istnetworks.hivesdk.presentation.datepickerquestion.DatePickerQuestionFragment
import com.istnetworks.hivesdk.presentation.emojis.EmojiFragment
import com.istnetworks.hivesdk.presentation.freeinputs.FreeInputsFragment
import com.istnetworks.hivesdk.presentation.multiImageChoice.MultipleImageChoiceFragment
import com.istnetworks.hivesdk.presentation.multipleChoices.MultipleChoicesFragment
import com.istnetworks.hivesdk.presentation.nps.NpsFragment
import com.istnetworks.hivesdk.presentation.singleChoice.SingleChoiceFragment
import com.istnetworks.hivesdk.presentation.singleImageChoice.SingleImageChoiceFragment
import com.istnetworks.hivesdk.presentation.spinnerquestion.SpinnerQuestionFragment

class VerticalListViewAdapter(private val fm: FragmentManager, private val types: List<Question>?) :
    RecyclerView.Adapter<VerticalListViewAdapter.VH>() {


    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
    }

    override fun getItemViewType(position: Int): Int {
        return 1
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.hve_vertical_adapter_item, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val it = types?.get(position)
        holder.setIsRecyclable(false)
        val f = getQuestionFragment(it, position)
        val fl = holder.itemView.findViewById<FrameLayout>(R.id.fl_fragment)
        fm.beginTransaction().add(fl.id, f).commit()
    }

    private fun getQuestionFragment(
        it: Question?,
        position: Int
    ): Fragment {
        return when (it?.questionType) {
            QuestionType.MultipleChoiceQuestion.value -> MultipleChoicesFragment.newInstance(
                position
            )
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
            QuestionType.TextInput.value -> FreeInputsFragment.getInstance(position)
            QuestionType.NumberInput.value -> FreeInputsFragment.getInstance(position)
            QuestionType.EmailInput.value -> FreeInputsFragment.getInstance(position)
            QuestionType.PhoneNumberInput.value -> FreeInputsFragment.getInstance(position)
            QuestionType.PostalCodeInput.value -> FreeInputsFragment.getInstance(position)
            QuestionType.URLInput.value -> FreeInputsFragment.getInstance(position)
            QuestionType.SingleChoice.value -> SingleChoiceFragment.newInstance(position)
            QuestionType.Emoji.value -> EmojiFragment()
            QuestionType.ImageMCQ.value -> MultipleImageChoiceFragment.getInstance(position)
            QuestionType.ImageSingleChoice.value -> SingleImageChoiceFragment.getInstance(position)
            QuestionType.CSAT.value -> SingleChoiceFragment.newInstance(position)
            null -> SpinnerQuestionFragment.getInstance(position)
            else -> SpinnerQuestionFragment.getInstance(position)
        }
    }

    override fun getItemCount(): Int {
       return types?.size?:0
    }


}