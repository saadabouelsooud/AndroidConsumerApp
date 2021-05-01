package com.istnetworks.hivesdk.presentation.freeinputs

import android.os.Bundle
import android.text.InputType
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import com.istnetworks.hivesdk.R
import com.istnetworks.hivesdk.data.models.response.Question
import com.istnetworks.hivesdk.data.repository.HiveSDKRepositoryImpl
import com.istnetworks.hivesdk.data.utils.extensions.disable
import com.istnetworks.hivesdk.databinding.FragmentFreeInputsBinding
import com.istnetworks.hivesdk.presentation.spinnerquestion.ARG_POSITION
import com.istnetworks.hivesdk.presentation.surveyExtension.questionStyle
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
        bindQuestions(9)
        onClickActions()
        return binding.root
    }

    private fun bindQuestions(questionType: Int) {
        when (questionType) {
            9 -> {
                binding.hveTvFreeInputsLabel.text = ""
                binding.hveEdtFreeInput.inputType = InputType.TYPE_CLASS_TEXT
                binding.hveEdtFreeInput.hint = ""
            }
            10 -> {
                binding.hveTvFreeInputsLabel.text = ""
                binding.hveEdtFreeInput.inputType = InputType.TYPE_CLASS_NUMBER
                binding.hveEdtFreeInput.hint = ""
            }
            11 -> {
                binding.hveTvFreeInputsLabel.text = getString(R.string.email)
                binding.hveEdtFreeInput.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
                binding.hveEdtFreeInput.hint = getString(R.string.email)
            }
            12 -> {
                binding.hveTvFreeInputsLabel.text = getString(R.string.phone_number)
                binding.hveEdtFreeInput.inputType = InputType.TYPE_CLASS_PHONE
                binding.hveEdtFreeInput.hint = getString(R.string.phone_number)
            }
            13 -> {
                binding.hveTvFreeInputsLabel.text = getString(R.string.postal_code)
                binding.hveEdtFreeInput.inputType = InputType.TYPE_CLASS_NUMBER
                binding.hveEdtFreeInput.hint = getString(R.string.postal_code)
            }
            14 -> {
                binding.hveTvFreeInputsLabel.text = getString(R.string.url)
                binding.hveEdtFreeInput.inputType = InputType.TYPE_TEXT_VARIATION_URI
                binding.hveEdtFreeInput.hint = getString(R.string.url)
            }
        }

    }

    private fun stylingViews() {
        val theme = viewModel.getSurveyTheme()
        binding.tvQuestionTitle.questionStyle(theme?.questionTitleStyle)
    }

    private fun initSubmitBtn() {
        binding.hveBtnSubmit.disable()
        binding.hveBtnSubmit.submitButtonStyle(viewModel.getSurveyTheme()?.submitButton)
    }
    private fun onClickActions() {
        binding.hveBtnSubmit.setOnClickListener {
            if (selectedQuestion?.isRequired == true) {
                if (TextUtils.isEmpty(binding.hveEdtFreeInput.text)) {
                    binding.hveEdtFreeInput.error=getString(R.string.required)
                    binding.hveEdtFreeInput.background=
                        activity?.let { it1 -> ContextCompat.getDrawable(it1,R.drawable.free_input_error) }
                }
            }
        }

    }
    companion object {
        @JvmStatic
        fun newInstance(@NonNull position: Int) =
            FreeInputsFragment().apply {
                arguments  = bundleOf(ARG_POSITION to position)
            }
    }
}