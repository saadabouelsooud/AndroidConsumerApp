package com.istnetworks.hivesdk.presentation.rating

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.core.os.bundleOf
import com.istnetworks.hivesdk.R
import com.istnetworks.hivesdk.data.models.StarOption
import com.istnetworks.hivesdk.data.models.response.Question
import com.istnetworks.hivesdk.data.models.response.toQuestionResponse
import com.istnetworks.hivesdk.data.utils.StarOptionEnum
import com.istnetworks.hivesdk.data.utils.extensions.hide
import com.istnetworks.hivesdk.data.utils.extensions.show
import com.istnetworks.hivesdk.databinding.FragmentRatingBinding
import com.istnetworks.hivesdk.presentation.BaseQuestionFragment
import com.istnetworks.hivesdk.presentation.interfaces.IsRequiredInterface
import com.istnetworks.hivesdk.presentation.interfaces.SubmitButtonInterface
import com.istnetworks.hivesdk.presentation.spinnerquestion.ARG_POSITION
import com.istnetworks.hivesdk.presentation.surveyExtension.questionTitleStyle

class RatingFragment : BaseQuestionFragment(),IsRequiredInterface, SubmitButtonInterface {

    private lateinit var binding: FragmentRatingBinding
    private var selectedQuestion: Question? = null
    private val position: Int? by lazy { arguments?.getInt(ARG_POSITION, -1) }
    private lateinit var selectedRating :HiveRatingBar
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRatingBinding.inflate(inflater)
        selectedQuestion = viewModel.findQuestion(position)
        stylingViews()
        initSubmitBtn()
        selectedQuestion?.StarOption?.shape?.let { bindQuestions(it, selectedQuestion?.StarOption) }
        observeViewModel()
        return binding.root
    }

    private fun observeViewModel() {
    }

    override fun onResume() {
        super.onResume()
        bindQuestionTitle()
        updatePagerHeight(binding.root)
        position?.let { viewModel.getDestinationsSubmitted(it) }

    }
    private fun bindQuestions(
        type: Int,
        starOption: StarOption?
    ) {

        when (type) {
            StarOptionEnum.HEART.value -> {
                selectedRating = binding.hveHeartRatingBar
            }
            StarOptionEnum.SMILEY.value -> {
                selectedRating = binding.hveSmileRatingBar
            }
            StarOptionEnum.THUMB.value -> {
                selectedRating = binding.hveThumbRatingBar
            }
            StarOptionEnum.STAR.value -> {
                selectedRating = binding.hveStarRatingBar
            }
        }

        selectedRating.show()
        selectedRating.setStarColor(
            ColorStateList.valueOf(
                Color.parseColor("#"+starOption?.fillColor)))

        listenToRating()
    }

    private fun stylingViews() {
        val theme = viewModel.getSurveyTheme()
        binding.tvQuestionTitle.questionTitleStyle(theme?.questionTitleStyle)


    }

    private fun bindQuestionTitle() {
        binding.tvQuestionTitle.text = context?.getString(
            R.string.question_format,
            viewModel.getQuestionNumber(), selectedQuestion?.title
        )
    }

    private fun listenToRating(){
        selectedRating.setOnRatingChangeListener { ratingBar, rating, fromUser ->
            viewModel.updateQuestionResponsesList(selectedQuestion?.toQuestionResponse(
                "",
                rating.toInt()
            ))
            position?.let { viewModel.getDestinationsSubmitted(it) }
        }
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