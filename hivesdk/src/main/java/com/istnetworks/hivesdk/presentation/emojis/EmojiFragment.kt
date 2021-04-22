package com.istnetworks.hivesdk.presentation.emojis

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.istnetworks.hivesdk.R
import com.istnetworks.hivesdk.data.local.CacheInMemory
import com.istnetworks.hivesdk.data.models.response.Question
import com.istnetworks.hivesdk.data.models.response.toQuestionResponse
import com.istnetworks.hivesdk.data.repository.HiveSDKRepositoryImpl
import com.istnetworks.hivesdk.data.utils.QuestionType
import com.istnetworks.hivesdk.databinding.FragmentEmojiBinding
import com.istnetworks.hivesdk.presentation.viewmodel.HiveSDKViewModel
import com.istnetworks.hivesdk.presentation.viewmodel.factory.HiveSDKViewModelFactory


class EmojiFragment : Fragment() {
    private val viewModel: HiveSDKViewModel by activityViewModels {
        HiveSDKViewModelFactory(
            HiveSDKRepositoryImpl()
        )
    }
    private lateinit var binding: FragmentEmojiBinding
    private var isRequired: Boolean = false
    private var rateValue: Int = -1
    private var selectedQuestion: Question? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentEmojiBinding.inflate(inflater)
        observeSurvey()
        setOnClickListeners()
        return binding.root
    }

    private fun setOnClickListeners() {
        binding.smileyRating.setRatingSelectListener(object : RatingSelectListener {
            override fun ratingSelected(rating: Int) {
                rateValue = rating
            }

        })
        binding.ivNextQuestion.setOnClickListener {
            validateNextButton()
        }

        binding.ivClose.setOnClickListener{
            requireActivity().finish()
        }
    }

    private fun observeSurvey() {
        val surveyResponse = CacheInMemory.getSurveyResponse()
        if (surveyResponse.survey?.surveyOptions?.hasProgressBar == true)
            binding.animateProgressBar.visibility = View.VISIBLE

        binding.tvSurveyTitle.text = surveyResponse.survey?.title

        for (i in surveyResponse.survey?.questions?.indices!!) {
            if (surveyResponse.survey.questions[i].questionType == QuestionType.Emoji.value) {
                binding.tvQuestionTitle.text = surveyResponse.survey.questions[i].title
                isRequired = surveyResponse.survey.questions[i].isRequired!!
                selectedQuestion = surveyResponse.survey.questions[i]
                if (i == 0)
                    binding.ivPrevQuestion.visibility = View.GONE
                break

            }


        }
    }

    private fun validateNextButton() {
        if (isRequired) {
            if (rateValue >= 0) {
                moveToNpsQuestion()
            } else Toast.makeText(activity, getString(R.string.required), Toast.LENGTH_LONG).show()
        } else
            moveToNpsQuestion()
    }

    private fun moveToNpsQuestion() {
        viewModel.updateSelectedQuestions(selectedQuestion?.toQuestionResponse("", rateValue))
        val navController = view?.let { Navigation.findNavController(it) }
        if (navController?.currentDestination?.id == R.id.emojiFragment)
            navController.navigate(R.id.action_emoji_to_nps)
    }


}