package com.istnetworks.hivesdk.presentation.freeinputs

import android.graphics.Color
import android.os.Bundle
import android.text.InputType
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Keep
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import com.istnetworks.hivesdk.R
import com.istnetworks.hivesdk.data.models.response.Question
import com.istnetworks.hivesdk.data.repository.HiveSDKRepositoryImpl
import com.istnetworks.hivesdk.data.utils.QuestionType
import com.istnetworks.hivesdk.data.utils.extensions.disable
import com.istnetworks.hivesdk.databinding.FragmentFreeInputsBinding
import com.istnetworks.hivesdk.presentation.spinnerquestion.ARG_POSITION

import com.istnetworks.hivesdk.presentation.surveyExtension.isValidEmail
import com.istnetworks.hivesdk.presentation.surveyExtension.isValidUrl

import com.istnetworks.hivesdk.presentation.surveyExtension.questionTitleStyle
import com.istnetworks.hivesdk.presentation.surveyExtension.submitButtonStyle
import com.istnetworks.hivesdk.presentation.viewmodel.HiveSDKViewModel
import com.istnetworks.hivesdk.presentation.viewmodel.factory.HiveSDKViewModelFactory


class FreeInputsFragment : Fragment() {

    private lateinit var binding: FragmentFreeInputsBinding
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
        binding = FragmentFreeInputsBinding.inflate(inflater)
        selectedQuestion = viewModel.findQuestion(position)
        stylingViews()
        initSubmitBtn()
        selectedQuestion?.questionType?.let { bindQuestions(it) }
        selectedQuestion?.questionType?.let { onClickActions(it) }
        return binding.root
    }

    private fun bindQuestions(questionType: Int) {
        binding.hveEdtFreeInput.visibility = View.VISIBLE
        binding.llPhone.visibility = View.GONE
        when (questionType) {
            QuestionType.TextInput.value -> {
                binding.hveTvFreeInputsLabel.text = "First Name"
                binding.hveEdtFreeInput.inputType = InputType.TYPE_CLASS_TEXT
                binding.hveEdtFreeInput.hint = "Enter your name"
            }
            QuestionType.NumberInput.value -> {
                binding.hveTvFreeInputsLabel.text = "Age"
                binding.hveEdtFreeInput.inputType = InputType.TYPE_CLASS_NUMBER
                binding.hveEdtFreeInput.hint = "Enter your age"
            }
            QuestionType.EmailInput.value -> {
                binding.hveTvFreeInputsLabel.text = getString(R.string.email)
                binding.hveEdtFreeInput.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
                binding.hveEdtFreeInput.hint = getString(R.string.email)
            }
            QuestionType.PhoneNumberInput.value -> {
                handlePhoneInput()
            }
            QuestionType.PostalCodeInput.value -> {
                binding.hveTvFreeInputsLabel.text = getString(R.string.postal_code)
                binding.hveEdtFreeInput.inputType = InputType.TYPE_CLASS_NUMBER
                binding.hveEdtFreeInput.hint = getString(R.string.postal_code)
            }
            QuestionType.URLInput.value -> {
                binding.hveTvFreeInputsLabel.text = getString(R.string.url)
                binding.hveEdtFreeInput.inputType = InputType.TYPE_TEXT_VARIATION_URI
                binding.hveEdtFreeInput.hint = getString(R.string.url)
            }
        }
    }

    private fun handlePhoneInput() {
        binding.hveTvFreeInputsLabel.text = getString(R.string.phone_number)
        binding.hveEdtFreeInput.visibility = View.GONE
        binding.llPhone.visibility = View.VISIBLE
        binding.hveEdtPhone.hint = getString(R.string.phone_number)
        binding.tvCountryCode.setTextColor(Color.parseColor("#0075be"))
        binding.hveEdtPhone.setTextColor(Color.parseColor("#0075be"))
        activity?.let { ContextCompat.getColor(it, R.color.navyBlue) }?.let {
            binding.ivPhoneIcon.setColorFilter(
                it, android.graphics.PorterDuff.Mode.SRC_IN
            )
        }
    }

    private fun moveToNextQuestion(questionType: Int): Boolean {
        when (questionType) {
            QuestionType.EmailInput.value -> {
                if (!binding.hveEdtFreeInput.text.toString().isValidEmail()) {
                    setFreeInputError()
                    return false
                }
                return true
            }
            QuestionType.URLInput.value -> {
                if (!binding.hveEdtFreeInput.text.toString().isValidUrl()) {
                    setFreeInputError()
                    return false
                }
                return true
            }
            QuestionType.PhoneNumberInput.value -> {
                if (TextUtils.isEmpty(binding.hveEdtPhone.text)) {
                    setPhoneError()
                    return false
                }
                return true
            }
            else -> {
                if (TextUtils.isEmpty(binding.hveEdtFreeInput.text)) {
                    setFreeInputError()
                    return false
                }
                return true
            }
        }
    }

    private fun setFreeInputError() {
        binding.tvErrorMessage.visibility = View.VISIBLE
        binding.hveEdtFreeInput.setTextColor(Color.parseColor("#e4606c"))
        binding.hveEdtFreeInput.background =
            activity?.let { it1 ->
                ContextCompat.getDrawable(
                    it1,
                    R.drawable.free_input_error
                )
            }
    }

    private fun setPhoneError() {
        binding.tvErrorMessage.visibility = View.VISIBLE
        binding.hveEdtPhone.setTextColor(Color.parseColor("#e4606c"))
        binding.llPhone.background =
            activity?.let { it1 ->
                ContextCompat.getDrawable(
                    it1,
                    R.drawable.free_input_error
                )
            }
        binding.tvCountryCode.setTextColor(Color.parseColor("#e4606c"))
        activity?.let { ContextCompat.getColor(it, R.color.errorColor) }?.let {
            binding.ivPhoneIcon.setColorFilter(
                it, android.graphics.PorterDuff.Mode.SRC_IN
            )
        }
    }

    private fun getFreeInputText(questionType: Int): String {
        return when (questionType) {
            QuestionType.PhoneNumberInput.value ->
                binding.hveEdtPhone.text.toString()
            else -> binding.hveEdtFreeInput.text.toString()

        }
    }

    private fun stylingViews() {
        val theme = viewModel.getSurveyTheme()
        binding.tvQuestionTitle.questionTitleStyle(theme?.questionTitleStyle)
        binding.tvQuestionTitle.text = selectedQuestion?.title

    }

    private fun initSubmitBtn() {
        binding.hveBtnSubmit.disable()
        binding.hveBtnSubmit.submitButtonStyle(viewModel.getSurveyTheme()?.submitButton)
    }

    private fun onClickActions(questionType: Int) {
        binding.hveBtnSubmit.setOnClickListener {
            if (selectedQuestion?.isRequired == true) {

            }
        }
    }

    /////// handle next and prev button ///////
    // if(moveToNextQuestion(questionType) ==true)
    /// getFreeInputText(questionType) , move to next question
    /// else show error , and do nothing


    @Keep
    companion object {
        @JvmStatic
        fun getInstance(@NonNull position: Int) =
            FreeInputsFragment().apply {
                arguments = bundleOf(ARG_POSITION to position)
            }
    }
}