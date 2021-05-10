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
import com.istnetworks.hivesdk.R
import com.istnetworks.hivesdk.data.models.response.Question
import com.istnetworks.hivesdk.data.models.response.toQuestionResponse
import com.istnetworks.hivesdk.data.repository.HiveSDKRepositoryImpl
import com.istnetworks.hivesdk.data.utils.extensions.disable
import com.istnetworks.hivesdk.data.utils.extensions.hide
import com.istnetworks.hivesdk.data.utils.extensions.onClick
import com.istnetworks.hivesdk.data.utils.extensions.show
import com.istnetworks.hivesdk.databinding.FragmentDatePickerQuestionBinding
import com.istnetworks.hivesdk.presentation.BaseQuestionFragment
import com.istnetworks.hivesdk.presentation.interfaces.IsRequiredInterface
import com.istnetworks.hivesdk.presentation.mainfragment.MainFragment
import com.istnetworks.hivesdk.presentation.surveyExtension.questionTitleStyle
import com.istnetworks.hivesdk.presentation.surveyExtension.submitButtonStyle
import com.istnetworks.hivesdk.presentation.viewmodel.HiveSDKViewModel
import com.istnetworks.hivesdk.presentation.viewmodel.factory.HiveSDKViewModelFactory
import java.text.SimpleDateFormat
import java.util.*


const val ARG_POSITION = "pos"

class DatePickerQuestionFragment : BaseQuestionFragment(),IsRequiredInterface {
    private lateinit var binding: FragmentDatePickerQuestionBinding
    private var selectedQuestion: Question? = null
    private val position: Int? by lazy { arguments?.getInt(ARG_POSITION, -1) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDatePickerQuestionBinding.inflate(inflater)
        selectedQuestion = viewModel.findQuestion(position)
        stylingQuestionTitle(binding.tvQuestionTitle)
        initSubmitBtn()
        onClickActions()
        handleDatePickerField()
        observeViewModel()
        return binding.root
    }

    private fun observeViewModel() {

    }

    override fun onResume() {
        super.onResume()
        binding.root.requestLayout()
        bindQuestion()
    }



    private fun bindQuestion() {
        binding.tvQuestionTitle.text = context?.getString(
            R.string.question_format,
            viewModel.getQuestionNumber(), selectedQuestion?.title
        )
    }

    private fun initSubmitBtn() {
       viewModel.setSubmitButtonBasedOnPosition(binding.hveBtnSubmit,position)
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

                    val sdf = SimpleDateFormat("dd MMMM,yyyy",Locale.getDefault())
                    c.set(year, month, dayOfMonth)
                    val dateString: String = sdf.format(c.getTime())
                    binding.hveTietDate.setText(dateString)
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

    }

    override fun showIsRequiredError() {
        binding.tvErrorMessage.show()
        updatePagerHeight(binding.root)

    }

    override fun hideIsRequiredError() {
        binding.tvErrorMessage.hide()
        updatePagerHeight(binding.root)

    }
}