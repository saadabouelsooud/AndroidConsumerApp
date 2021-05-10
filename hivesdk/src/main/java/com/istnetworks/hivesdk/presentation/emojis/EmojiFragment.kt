package com.istnetworks.hivesdk.presentation.emojis

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.istnetworks.hivesdk.R
import com.istnetworks.hivesdk.data.models.response.Question
import com.istnetworks.hivesdk.data.models.response.toQuestionResponse
import com.istnetworks.hivesdk.data.repository.HiveSDKRepositoryImpl
import com.istnetworks.hivesdk.databinding.FragmentEmojiBinding
import com.istnetworks.hivesdk.presentation.BaseQuestionFragment
import com.istnetworks.hivesdk.presentation.mainfragment.MainFragment
import com.istnetworks.hivesdk.presentation.surveyExtension.questionTitleStyle
import com.istnetworks.hivesdk.presentation.viewmodel.HiveSDKViewModel
import com.istnetworks.hivesdk.presentation.viewmodel.factory.HiveSDKViewModelFactory

private const val ARG_QUESTION_POSITION = "ARG_QUESTION_POSITION"
class EmojiFragment : BaseQuestionFragment(){

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
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        bindQuestion()
        binding.root.requestLayout()
        (requireParentFragment() as MainFragment).updatePagerHeightForChild(binding.root)
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
}