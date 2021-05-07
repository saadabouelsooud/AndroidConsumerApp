package com.istnetworks.hivesdk.presentation.sliderquestion

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
import com.istnetworks.hivesdk.databinding.HveFragmentSliderQuestionBinding
import com.istnetworks.hivesdk.presentation.mainfragment.MainFragment
import com.istnetworks.hivesdk.presentation.surveyExtension.questionTitleStyle
import com.istnetworks.hivesdk.presentation.surveyExtension.submitButtonStyle
import com.istnetworks.hivesdk.presentation.viewmodel.HiveSDKViewModel
import com.istnetworks.hivesdk.presentation.viewmodel.factory.HiveSDKViewModelFactory


const val ARG_POSITION = "pos"

class SliderQuestionFragment : Fragment() {
    private lateinit var binding: HveFragmentSliderQuestionBinding
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
        binding = HveFragmentSliderQuestionBinding.inflate(inflater)
        selectedQuestion = viewModel.findQuestion(position)
        stylingViews()
        initSubmitBtn()
        bindQuestion()
        onClickActions()
        setSliderListener()
        return binding.root
    }

    private fun setSliderListener() {
        binding.hveSliderAnswers.addOnChangeListener { _, value, _ ->
            viewModel.updateQuestionResponsesList(
                selectedQuestion?.toQuestionResponse(
                    numberResponse = value.toInt(),
                    textResponse = "${value.toInt()}"
                )
            )
        }
    }

    override fun onStart() {
        super.onStart()

    }

    override fun onResume() {
        super.onResume()
        (requireParentFragment() as MainFragment).updatePagerHeightForChild(binding.root)
    }

    private fun stylingViews() {
        val theme = viewModel.getSurveyTheme()
        binding.tvQuestionTitle.questionTitleStyle(theme?.questionTitleStyle)
    }

    private fun bindQuestion() {
        binding.tvQuestionTitle.text = context?.getString(
            R.string.question_format,
            position?.plus(1),selectedQuestion?.title)
    }

    private fun initSubmitBtn() {
        viewModel.setSubmitButtonBasedOnPosition(binding.hveBtnSubmit,position)
    }



    @Keep
    companion object {
        @JvmStatic
        fun getInstance(@NonNull position: Int): SliderQuestionFragment {
            val f = SliderQuestionFragment()
            f.arguments = bundleOf(ARG_POSITION to position)
            return f
        }

    }

    private fun onClickActions() {


    }



}