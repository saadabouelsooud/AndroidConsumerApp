package com.istnetworks.hivesdk.presentation.sliderquestion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Keep
import androidx.annotation.NonNull
import androidx.core.os.bundleOf
import com.istnetworks.hivesdk.R
import com.istnetworks.hivesdk.data.models.response.Question
import com.istnetworks.hivesdk.data.models.response.toQuestionResponse
import com.istnetworks.hivesdk.data.utils.extensions.hide
import com.istnetworks.hivesdk.data.utils.extensions.show
import com.istnetworks.hivesdk.databinding.HveFragmentSliderQuestionBinding
import com.istnetworks.hivesdk.presentation.BaseQuestionFragment
import com.istnetworks.hivesdk.presentation.interfaces.IsRequiredInterface
import com.istnetworks.hivesdk.presentation.interfaces.SubmitButtonInterface
import com.istnetworks.hivesdk.presentation.surveyExtension.questionTitleStyle


const val ARG_POSITION = "pos"

class SliderQuestionFragment : BaseQuestionFragment(),IsRequiredInterface ,SubmitButtonInterface{
    private lateinit var binding: HveFragmentSliderQuestionBinding
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
        onClickActions()
        setSliderListener()
        observeViewModel()
        return binding.root
    }

    private fun observeViewModel() {
    }

    private fun setSliderListener() {
        binding.hveSliderAnswers.addOnChangeListener { _, value, _ ->
            viewModel.updateQuestionResponsesList(
                selectedQuestion?.toQuestionResponse(
                    numberResponse = value.toInt(),
                    textResponse = "${value.toInt()}"
                )
            )
            position?.let { viewModel.updateSubmitBtnVisibilityBeforeAnswerChosen(it) }
        }
    }

    override fun onStart() {
        super.onStart()

    }

    override fun onResume() {
        super.onResume()
        bindQuestionTitle()
        position?.let { viewModel.updateSubmitBtnVisibilityBeforeAnswerChosen(it) }
        updatePagerHeight(binding.root)

    }

    private fun stylingViews() {
        val theme = viewModel.getSurveyTheme()
        binding.tvQuestionTitle.questionTitleStyle(theme?.questionTitleStyle)
    }

    private fun bindQuestionTitle() {
        binding.tvQuestionTitle.text = context?.getString(
            R.string.question_format,
            viewModel.getQuestionNumber(),selectedQuestion?.title)
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

    override fun showIsRequiredError() {
        binding.tvErrorMessage.show()
        updatePagerHeight(binding.root)

    }

    override fun hideIsRequiredError() {
       binding.tvErrorMessage.hide()
        updatePagerHeight(binding.root)

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