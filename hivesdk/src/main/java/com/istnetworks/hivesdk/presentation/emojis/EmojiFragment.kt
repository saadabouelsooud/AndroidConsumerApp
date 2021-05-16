package com.istnetworks.hivesdk.presentation.emojis

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.istnetworks.hivesdk.R
import com.istnetworks.hivesdk.data.models.response.Question
import com.istnetworks.hivesdk.data.models.response.toQuestionResponse
import com.istnetworks.hivesdk.data.utils.extensions.hide
import com.istnetworks.hivesdk.data.utils.extensions.show
import com.istnetworks.hivesdk.databinding.FragmentEmojiBinding
import com.istnetworks.hivesdk.presentation.BaseQuestionFragment
import com.istnetworks.hivesdk.presentation.interfaces.IsRequiredInterface
import com.istnetworks.hivesdk.presentation.interfaces.SubmitButtonInterface
import com.istnetworks.hivesdk.presentation.surveyExtension.questionTitleStyle

private const val ARG_QUESTION_POSITION = "ARG_QUESTION_POSITION"
class EmojiFragment : BaseQuestionFragment(),IsRequiredInterface,SubmitButtonInterface{

    private val questionPosition by lazy { arguments?.getInt(ARG_QUESTION_POSITION) }
    private lateinit var binding: FragmentEmojiBinding
    private var selectedQuestion: Question? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEmojiBinding.inflate(inflater)
        initEmojisView()
        setOnClickListeners()
        initSubmitBtn()
        observeViewModel()
        return binding.root
    }

    private fun observeViewModel() {

    }

    override fun onResume() {
        super.onResume()
        bindQuestion()
        updatePagerHeight(binding.root)
        questionPosition?.let { viewModel.getDestinationsSubmitted(it) }
    }
    private fun initSubmitBtn() {
        viewModel.setSubmitButtonBasedOnPosition(binding.hveBtnSubmit,questionPosition)
    }

    private fun setOnClickListeners() {
        binding.smileyRating.setRatingSelectListener(object : RatingSelectListener {
            override fun ratingSelected(rating: Int) {
                viewModel.updateQuestionResponsesList(
                    selectedQuestion?.toQuestionResponse(
                        textResponse = null,
                        numberResponse = rating
                    )
                )
                questionPosition?.let { viewModel.getDestinationsSubmitted(it) }
            }

        })

    }

    private fun initEmojisView() {
        selectedQuestion = viewModel.findQuestion(questionPosition)
        binding.tvQuestionTitle.questionTitleStyle(viewModel.getSurveyTheme()?.questionTitleStyle)
        binding.smileyRating.ratingScale = selectedQuestion?.scale ?:5
    }

    private fun bindQuestion() {
        binding.tvQuestionTitle.text = context?.getString(
            R.string.question_format,
            viewModel.getQuestionNumber(), selectedQuestion?.title
        )
    }

    companion object{
        @JvmStatic
        fun getInstance(questionPosition :Int)=
            EmojiFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_QUESTION_POSITION, questionPosition)
                }
            }
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