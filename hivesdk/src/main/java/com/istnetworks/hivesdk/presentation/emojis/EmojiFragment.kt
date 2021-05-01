package com.istnetworks.hivesdk.presentation.emojis

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.istnetworks.hivesdk.R
import com.istnetworks.hivesdk.data.local.CacheInMemory
import com.istnetworks.hivesdk.data.models.response.Question
import com.istnetworks.hivesdk.data.models.response.toQuestionResponse
import com.istnetworks.hivesdk.data.repository.HiveSDKRepositoryImpl
import com.istnetworks.hivesdk.data.utils.QuestionType
import com.istnetworks.hivesdk.databinding.FragmentEmojiBinding
import com.istnetworks.hivesdk.presentation.surveyExtension.questionTitleStyle
import com.istnetworks.hivesdk.presentation.surveyExtension.surveyTitleStyle
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

    }

    private fun observeSurvey() {
        val surveyResponse = CacheInMemory.getSurveyResponse()

        binding.tvQuestionTitle.questionTitleStyle(surveyResponse.survey?.surveyOptions?.surveyTheme?.questionTitleStyle)
        for (i in surveyResponse.survey?.questions?.indices!!) {
            if (surveyResponse.survey.questions[i].questionType == QuestionType.Emoji.value) {
                binding.tvQuestionTitle.text = surveyResponse.survey.questions[i].title
                isRequired = surveyResponse.survey.questions[i].isRequired!!
                selectedQuestion = surveyResponse.survey.questions[i]


            }


        }
    }



}