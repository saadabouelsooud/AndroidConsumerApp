package com.istnetworks.hivesdk.presentation.spinnerquestion

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
import com.istnetworks.hivesdk.databinding.FragmentSpinnerQuestionBinding
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
        binding.hveBtnSubmit.disable()
        viewModel.stylingSubmitBtn(binding.hveBtnSubmit)
        selectedQuestion = viewModel.findQuestion(position)
        setSpinner()
        onClickActions()
        return binding.root
    }

    private fun setSpinner() {

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