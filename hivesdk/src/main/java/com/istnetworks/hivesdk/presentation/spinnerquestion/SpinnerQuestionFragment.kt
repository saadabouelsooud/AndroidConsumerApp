package com.istnetworks.hivesdk.presentation.spinnerquestion

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
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
import com.istnetworks.hivesdk.databinding.FragmentSpinnerQuestionBinding
import com.istnetworks.hivesdk.presentation.surveyExtension.questionTitleStyle
import com.istnetworks.hivesdk.presentation.surveyExtension.submitButtonStyle
import com.istnetworks.hivesdk.presentation.viewmodel.HiveSDKViewModel
import com.istnetworks.hivesdk.presentation.viewmodel.factory.HiveSDKViewModelFactory


const val ARG_POSITION = "pos"

class SpinnerQuestionFragment : Fragment() {
    private lateinit var binding: FragmentSpinnerQuestionBinding
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
        binding = FragmentSpinnerQuestionBinding.inflate(inflater)
        selectedQuestion = viewModel.findQuestion(position)
        stylingViews()
        initSubmitBtn()
        bindQuestion()
        onClickActions()
        return binding.root
    }

    private fun stylingViews() {
        val theme = viewModel.getSurveyTheme()
        binding.tvQuestionTitle.questionTitleStyle(theme?.questionTitleStyle)
    }

    private fun bindQuestion() {
        setSpinner()
        binding.tvQuestionTitle.text = selectedQuestion?.title
    }

    private fun initSubmitBtn() {
        binding.hveBtnSubmit.disable()
        binding.hveBtnSubmit.submitButtonStyle(viewModel.getSurveyTheme()?.submitButton)
    }

    private fun setSpinner() {
        val list = selectedQuestion?.choices?.map { it.title }?.toMutableList()
        list?.add(0, getString(R.string.choose_spinner))
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item, list ?: listOf()
        )
        binding.hveSpAnswers.adapter = adapter
    }

    @Keep
    companion object {
        @JvmStatic
        fun getInstance(@NonNull position: Int): SpinnerQuestionFragment {
            val f = SpinnerQuestionFragment()
            f.arguments = bundleOf(ARG_POSITION to position)
            return f
        }

    }

    private fun onClickActions() {

        binding.hveBtnSubmit.setOnClickListener {
            if (selectedQuestion?.isRequired == true) {

            }
        }

    }

    private fun onSurveyReadyToSave() {

    }


}