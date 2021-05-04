package com.istnetworks.hivesdk.presentation.rating

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import com.istnetworks.hivesdk.R
import com.istnetworks.hivesdk.data.models.StarOption
import com.istnetworks.hivesdk.data.models.response.Question
import com.istnetworks.hivesdk.data.repository.HiveSDKRepositoryImpl
import com.istnetworks.hivesdk.data.utils.StarOptionEnum
import com.istnetworks.hivesdk.data.utils.extensions.disable
import com.istnetworks.hivesdk.databinding.FragmentRatingBinding
import com.istnetworks.hivesdk.presentation.spinnerquestion.ARG_POSITION
import com.istnetworks.hivesdk.presentation.surveyExtension.questionTitleStyle
import com.istnetworks.hivesdk.presentation.viewmodel.HiveSDKViewModel
import com.istnetworks.hivesdk.presentation.viewmodel.factory.HiveSDKViewModelFactory

class RatingFragment : Fragment() {

    private lateinit var binding: FragmentRatingBinding
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
        binding = FragmentRatingBinding.inflate(inflater)
        selectedQuestion = viewModel.findQuestion(position)
        stylingViews()
        selectedQuestion?.StarOption?.shape?.let { bindQuestions(it,selectedQuestion?.StarOption) }
        return binding.root
    }
    private fun bindQuestions(
        type: Int,
        starOption: StarOption?
    ) {
        binding.ratingBar.setStarColor(
                ColorStateList.valueOf(
                    Color.parseColor("#"+starOption?.fillColor)))
        when (type) {
            StarOptionEnum.HEART.value -> {
                binding.ratingBar.setStarDrawable(R.drawable.ic_heart)
            }
            StarOptionEnum.SMILEY.value -> {
                binding.ratingBar.setStarDrawable(R.drawable.ic_rating_star_border)
            }
            StarOptionEnum.THUMB.value -> {
                binding.ratingBar.setStarDrawable(R.drawable.baseline_thumb_up_24)
            }
            StarOptionEnum.STAR.value -> {
                binding.ratingBar.setStarDrawable(R.drawable.ic_rating_star_solid)
            }
        }

    }

    private fun stylingViews() {
        val theme = viewModel.getSurveyTheme()
        binding.tvQuestionTitle.questionTitleStyle(theme?.questionTitleStyle)
        binding.tvQuestionTitle.text = context?.getString(R.string.question_format,
            position?.plus(1),selectedQuestion?.title)

    }

    private fun initSubmitBtn() {
        binding.hveBtnSubmit.disable()
        //binding.hveBtnSubmit.submitButtonStyle(viewModel.getSurveyTheme()?.submitButton)
    }

    companion object {
        @JvmStatic
        fun getInstance(@NonNull position: Int) =
            RatingFragment().apply {
                arguments =  bundleOf(ARG_POSITION to position)
            }
    }
}