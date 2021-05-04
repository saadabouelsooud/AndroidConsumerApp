package com.istnetworks.hivesdk.presentation.rating

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
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
        initSubmitBtn()

        selectedQuestion?.StarOption?.shape?.let { bindQuestions(it, selectedQuestion?.StarOption) }
        return binding.root
    }

    private fun bindQuestions(
        type: Int,
        starOption: StarOption?
    ) {

        when (type) {
            StarOptionEnum.HEART.value -> {
                binding.ratingBar.rating
            }
            StarOptionEnum.SMILEY.value -> {

            }
            StarOptionEnum.THUMB.value -> {

            }
            StarOptionEnum.STAR.value -> {
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
        viewModel.setSubmitButtonBasedOnPosition(binding.hveBtnSubmit,position)
    }

    companion object {
        @JvmStatic
        fun getInstance(@NonNull position: Int) =
            RatingFragment().apply {
                arguments =  bundleOf(ARG_POSITION to position)
            }
    }
}