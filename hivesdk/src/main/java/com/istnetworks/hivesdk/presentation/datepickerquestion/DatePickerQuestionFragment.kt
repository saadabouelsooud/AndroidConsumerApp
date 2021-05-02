package com.istnetworks.hivesdk.presentation.datepickerquestion

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Keep
import androidx.annotation.NonNull
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.istnetworks.hivesdk.data.models.response.Question
import com.istnetworks.hivesdk.data.models.response.toQuestionResponse
import com.istnetworks.hivesdk.data.repository.HiveSDKRepositoryImpl
import com.istnetworks.hivesdk.data.utils.extensions.disable
import com.istnetworks.hivesdk.data.utils.extensions.onClick
import com.istnetworks.hivesdk.databinding.FragmentDatePickerQuestionBinding
import com.istnetworks.hivesdk.presentation.surveyExtension.questionTitleStyle
import com.istnetworks.hivesdk.presentation.surveyExtension.submitButtonStyle
import com.istnetworks.hivesdk.presentation.viewmodel.HiveSDKViewModel
import com.istnetworks.hivesdk.presentation.viewmodel.factory.HiveSDKViewModelFactory
import java.util.*


const val ARG_POSITION = "pos"

class DatePickerQuestionFragment : Fragment() {
    private lateinit var binding: FragmentDatePickerQuestionBinding
    private val viewModel: HiveSDKViewModel by activityViewModels {
        HiveSDKViewModelFactory(
            HiveSDKRepositoryImpl()
        )
    }
    private var selectedQuestion: Question? = null
    private val position: Int? by lazy { arguments?.getInt(ARG_POSITION, -1) }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDatePickerQuestionBinding.inflate(inflater)
        selectedQuestion = viewModel.findQuestion(position)
        stylingViews()
        initSubmitBtn()
        bindQuestion()
        onClickActions()
        handleDatePickerField()
        return binding.root
    }

    private fun stylingViews() {
        val theme = viewModel.getSurveyTheme()
        binding.tvQuestionTitle.questionTitleStyle(theme?.questionTitleStyle)
    }

    private fun bindQuestion() {
        binding.tvQuestionTitle.text = selectedQuestion?.title
    }

    private fun initSubmitBtn() {
        binding.hveBtnSubmit.disable()
        binding.hveBtnSubmit.submitButtonStyle(viewModel.getSurveyTheme()?.submitButton)
    }

    private fun handleDatePickerField() {

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        binding.hveTietDate.onClick {
            //   binding.hveTietDate.disableForSecond()

            val dialog = DatePickerDialog(
                requireContext(), { _, year, month, dayOfMonth ->

                    val mm = if (month < 9)
                        "0${month + 1}"
                    else
                        (month + 1).toString()

                    val dd = if (dayOfMonth <= 9)
                        "0$dayOfMonth"
                    else
                        dayOfMonth.toString()

                    binding.hveTietDate.setText("$dd $mm,$year")
                    saveAnswer(binding.hveTietDate.text.toString())
                }, year, month, day - 1
            )
            dialog.show()
        }
    }

    private fun saveAnswer(answerText: String) {
        viewModel.updateQuestionResponsesList(
            selectedQuestion?.toQuestionResponse(
                answerText,
                0
            )
        )
    }


    @Keep
    companion object {
        @JvmStatic
        fun getInstance(@NonNull position: Int): DatePickerQuestionFragment {
            val f = DatePickerQuestionFragment()
            f.arguments = bundleOf(ARG_POSITION to position)
            return f
        }

    }

    private fun onClickActions() {

        binding.hveBtnSubmit.setOnClickListener {
            if (selectedQuestion?.isRequired == true) {
                //   viewModel.saveSurvey()
            }
        }


    }


}