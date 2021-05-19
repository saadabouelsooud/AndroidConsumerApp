package com.istnetworks.hivesdk.presentation.freeinputs

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Keep
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import com.istnetworks.hivesdk.R
import com.istnetworks.hivesdk.data.models.response.Question
import com.istnetworks.hivesdk.data.models.response.toQuestionResponse
import com.istnetworks.hivesdk.data.utils.QuestionType
import com.istnetworks.hivesdk.data.utils.extensions.hide
import com.istnetworks.hivesdk.data.utils.extensions.show
import com.istnetworks.hivesdk.databinding.FragmentFreeInputsBinding
import com.istnetworks.hivesdk.presentation.BaseQuestionFragment
import com.istnetworks.hivesdk.presentation.interfaces.IsRequiredInterface
import com.istnetworks.hivesdk.presentation.interfaces.SubmitButtonInterface
import com.istnetworks.hivesdk.presentation.interfaces.ValidationErrorInterface
import com.istnetworks.hivesdk.presentation.spinnerquestion.ARG_POSITION
import com.istnetworks.hivesdk.presentation.surveyExtension.isValidEmail
import com.istnetworks.hivesdk.presentation.surveyExtension.isValidUrl
import com.istnetworks.hivesdk.presentation.surveyExtension.questionTitleStyle


class FreeInputsFragment : BaseQuestionFragment(), IsRequiredInterface, ValidationErrorInterface,SubmitButtonInterface {

    private val CODE_AREA_PATTERN = "^[^01][0-9]{2}\$"
    private val PHONE_NUMBER_PATTERN = "[0-9][0-9]{8,14}"

    private lateinit var binding: FragmentFreeInputsBinding
    private var selectedQuestion: Question? = null
    private val position: Int? by lazy { arguments?.getInt(ARG_POSITION, -1) }
    private lateinit var textWatcher: TextWatcher
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
        listenToInputTextChanges()
        observeViewModel()
        return binding.root
    }

    private fun observeViewModel() {

    }

    private fun listenToInputTextChanges() {
        textWatcher = object :TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
                viewModel.updateQuestionResponsesList(
                    selectedQuestion?.toQuestionResponse(
                        textResponse = p0.toString(),
                        numberResponse = null
                    )
                )
                viewModel.validateAnswer(position!!)
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        }
//        binding.hveEdtFreeInput.doAfterTextChanged {
//
//            viewModel.updateQuestionResponsesList(
//                selectedQuestion?.toQuestionResponse(
//                    textResponse = it.toString(),
//                    numberResponse = null
//                )
//            )
//            viewModel.validateAnswer(position!!)
//        }

        binding.hveEdtPhone.doAfterTextChanged {
            viewModel.updateQuestionResponsesList(
                selectedQuestion?.toQuestionResponse(
                    textResponse = binding.edtCountryCode.text.toString() + it.toString(),
                    numberResponse = null
                )
            )
            viewModel.validateAnswer(position!!)
        }
    }

    override fun onResume() {
        super.onResume()
        bindQuestionTitle()
        updatePagerHeight(binding.root)
        binding.hveEdtFreeInput.addTextChangedListener(textWatcher)
    }

    override fun onPause() {
        super.onPause()
        binding.hveEdtFreeInput.removeTextChangedListener(null)
    }

    private fun bindQuestions(questionType: Int) {
        binding.hveEdtFreeInput.visibility = View.VISIBLE
        binding.llPhone.visibility = View.GONE
        when (questionType) {
            QuestionType.TextInput.value -> {
                binding.hveEdtFreeInput.inputType = InputType.TYPE_CLASS_TEXT
                binding.hveEdtFreeInput.hint = getString(R.string.enter_text_answer)
            }
            QuestionType.NumberInput.value -> {
                binding.hveEdtFreeInput.inputType = InputType.TYPE_CLASS_NUMBER
                binding.hveEdtFreeInput.hint = getString(R.string.enter_number_answer)
            }
            QuestionType.EmailInput.value -> {
                binding.hveEdtFreeInput.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
                binding.hveEdtFreeInput.hint = getString(R.string.enter_email_answer)
            }
            QuestionType.PhoneNumberInput.value -> {
                handlePhoneInput()
            }
            QuestionType.PostalCodeInput.value -> {
                binding.hveEdtFreeInput.inputType = InputType.TYPE_CLASS_NUMBER
                binding.hveEdtFreeInput.hint = getString(R.string.enter_postal_code_answer)
            }
            QuestionType.URLInput.value -> {
                binding.hveEdtFreeInput.inputType = InputType.TYPE_TEXT_VARIATION_URI
                binding.hveEdtFreeInput.hint = getString(R.string.enter_url_answer)
            }
        }
    }

    private fun handlePhoneInput() {
        binding.hveEdtFreeInput.visibility = View.GONE
        binding.llPhone.visibility = View.VISIBLE
        binding.hveEdtPhone.hint = getString(R.string.enter_telephone_answer)
        binding.edtCountryCode.setTextColor(Color.parseColor("#0075be"))
        binding.hveEdtPhone.setTextColor(Color.parseColor("#0075be"))
    }

    private fun validate(questionType: Int): Boolean {
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
                if (!binding.hveEdtPhone.text.toString().matches(PHONE_NUMBER_PATTERN.toRegex()) && !binding.edtCountryCode.text.toString().matches(CODE_AREA_PATTERN.toRegex()) ) {
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

    private fun hideFreeInputError() {
        binding.tvErrorMessage.hide()
        binding.hveEdtFreeInput.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.navyBlue
            )
        )
        binding.hveEdtFreeInput.background =
            activity?.let { it1 ->
                ContextCompat.getDrawable(
                    it1,
                    R.drawable.bg_free_input_rounded_border
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
        binding.edtCountryCode.setTextColor(Color.parseColor("#e4606c"))
    }

    private fun hidePhoneError() {
        binding.tvErrorMessage.hide()
        binding.hveEdtPhone.setTextColor(ContextCompat.getColor(requireContext(), R.color.navyBlue))
        binding.llPhone.background =
            activity?.let { it1 ->
                ContextCompat.getDrawable(
                    it1,
                    R.drawable.bg_free_input_rounded_border
                )
            }
        binding.edtCountryCode.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.navyBlue
            )
        )
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


    }

    private fun bindQuestionTitle() {
        binding.tvQuestionTitle.text = context?.getString(
            R.string.question_format,
            viewModel.getQuestionNumber(), selectedQuestion?.title
        )
    }


    private fun initSubmitBtn() {
        viewModel.setSubmitButtonBasedOnPosition(binding.hveBtnSubmit,position)
    }

    private fun onClickActions(questionType: Int) {

    }

    override fun showIsRequiredError() {
        binding.tvErrorMessage.show()
        updatePagerHeight(binding.root)

    }

    override fun hideIsRequiredError() {
        binding.tvErrorMessage.hide()
        updatePagerHeight(binding.root)

    }

    @Keep
    companion object {
        @JvmStatic
        fun getInstance(@NonNull position: Int) =
            FreeInputsFragment().apply {
                arguments = bundleOf(ARG_POSITION to position)
            }
    }

    override fun showNotValidError(questionType: Int?) {
        if (questionType != null) {
            validate(questionType)
            updatePagerHeight(binding.root)
        }

    }

    override fun hideNotValidError(questionType: Int?) {
        when (questionType) {
            QuestionType.EmailInput.value ,
            QuestionType.URLInput.value -> {
                hideFreeInputError()
            }
            QuestionType.PhoneNumberInput.value -> {
               hidePhoneError()
            }
            else -> {
                hideFreeInputError()
            }
        }
    }

    override fun showSubmitButton() {
        binding.hveBtnSubmit.show()
        updatePagerHeight(binding.root)
    }

    override fun hideSubmitButton() {
        binding.hveBtnSubmit.hide()
        updatePagerHeight(binding.root)
    }
}