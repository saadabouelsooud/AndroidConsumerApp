package com.istnetworks.hivesdk.presentation.singleImageChoice

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.chip.Chip
import com.istnetworks.hivesdk.R
import com.istnetworks.hivesdk.data.local.CacheInMemory
import com.istnetworks.hivesdk.data.models.Choices
import com.istnetworks.hivesdk.data.models.SelectedChoices
import com.istnetworks.hivesdk.data.models.response.Question
import com.istnetworks.hivesdk.data.models.response.styles.QuestionChoicesStyle
import com.istnetworks.hivesdk.data.models.response.toQuestionResponse
import com.istnetworks.hivesdk.data.repository.HiveSDKRepositoryImpl
import com.istnetworks.hivesdk.data.utils.extensions.disable
import com.istnetworks.hivesdk.data.utils.extensions.hide
import com.istnetworks.hivesdk.data.utils.extensions.show
import com.istnetworks.hivesdk.databinding.FragmentSingleImageChoiceBinding
import com.istnetworks.hivesdk.presentation.BaseQuestionFragment
import com.istnetworks.hivesdk.presentation.interfaces.IsRequiredInterface
import com.istnetworks.hivesdk.presentation.mainfragment.MainFragment
import com.istnetworks.hivesdk.presentation.surveyExtension.questionTitleStyle
import com.istnetworks.hivesdk.presentation.surveyExtension.singleChoiceStyle
import com.istnetworks.hivesdk.presentation.viewmodel.HiveSDKViewModel
import com.istnetworks.hivesdk.presentation.viewmodel.factory.HiveSDKViewModelFactory
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


private const val ARG_QUESTION_POSITION = "ARG_QUESTION_POSITION"
private const val TAG = "SingleImageChoice"

class SingleImageChoiceFragment : BaseQuestionFragment() ,IsRequiredInterface{
    private var questionPosition: Int? = null
    private var selectedQuestion: Question? = null
    private var isRequired: Boolean = false
    private lateinit var binding: FragmentSingleImageChoiceBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            questionPosition = it.getInt(ARG_QUESTION_POSITION, 0)
        }
    }

    override fun onResume() {
        super.onResume()
        bindQuestionTitle()
        updatePagerHeight(binding.root)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSingleImageChoiceBinding.inflate(inflater)

        observeViewModel()
        onClickActions()
        observeSurvey()
        initSubmitBtn()
        return binding.root
    }
    private fun initSubmitBtn() {
        viewModel.setSubmitButtonBasedOnPosition(binding.hveBtnSubmit,questionPosition)
    }

    private fun observeViewModel() {
        viewModel.showErrorMsg.observe(viewLifecycleOwner, {
            Toast.makeText(requireContext(), "$it", Toast.LENGTH_SHORT).show()
        })

        viewModel.saveSurveyResponseLD.observe(viewLifecycleOwner, {
            Toast.makeText(requireContext(), it?.message, Toast.LENGTH_SHORT).show()
            requireActivity().finish()
        })
    }

    private fun observeSurvey() {
        val surveyResponse = CacheInMemory.getSurveyResponse()

        selectedQuestion = questionPosition?.let { viewModel.getQuestions(it) }
        binding.hveTvQuestionTitle.questionTitleStyle(surveyResponse.survey?.surveyOptions?.surveyTheme?.questionTitleStyle)

        isRequired = selectedQuestion?.isRequired!!

        createChoices(selectedQuestion?.choices,surveyResponse?.survey?.surveyOptions?.
        surveyTheme?.questionChoicesStyle!!)

    }

    private fun bindQuestionTitle() {
        binding.hveTvQuestionTitle.text = context?.getString(
            R.string.question_format,
            viewModel.getQuestionNumber(), selectedQuestion?.title
        )
    }

    private fun createChoices(choiceList: List<Choices>?, style: QuestionChoicesStyle){
        val inflater = LayoutInflater.from(context)
        for (choice in choiceList!!) {
            val rbChoice = inflater.inflate(
                R.layout.single_choice_image_item, binding.hveRgSingleChoiceWrapper, false
            ) as RadioButton

            rbChoice.id = choice.choiceID!!
            rbChoice.text = choice.title
            lifecycleScope.launch {
                withContext(Dispatchers.IO)
                {
                    try {

                        val bitmap =
                            Picasso.get().load(choice.imageURL)
                                .placeholder(R.drawable.emoji_bad)
                                .resize(200, 200)
                                .get().toDrawable(resources)
                        withContext(Dispatchers.Main) {

                            rbChoice.setCompoundDrawablesWithIntrinsicBounds(null, bitmap, null, null)

                            (requireParentFragment() as MainFragment).updatePagerHeightForChild(binding.root)
                        }
                    }catch (e:Exception)
                    {
                        Log.e(TAG, "createChoices: ", e)
                    }
                }

            }
            rbChoice.singleChoiceStyle(style)
            rbChoice.setPadding(32, 16, 16, 16)
            rbChoice.layoutParams.apply {

            }
            binding.hveRgSingleChoiceWrapper.addView(rbChoice)
            this.view?.let { (requireParentFragment() as MainFragment).updatePagerHeightForChild(it) }

        }

    }


    private fun onClickActions() {


        binding.hveRgSingleChoiceWrapper.setOnCheckedChangeListener { radioGroup, i ->
            val checkedId = radioGroup.checkedCheckableImageButtonId
            val selectedChoice = selectedQuestion?.choices?.find { it.choiceID == checkedId }
            viewModel.updateQuestionResponsesList(
                selectedQuestion?.toQuestionResponse(
                    "", 0,
                    listOf(
                        SelectedChoices(selectedChoice?.choiceID, selectedChoice?.choiceGUID)
                    )
                )
            )
        }


    }

    private fun onSurveyReadyToSave() {
        viewModel.saveSurvey()
    }

    companion object {
        /**
         *
         * @param questionPosition Parameter 1.
         * @return A new instance of fragment SingleChoiceFragment.
         */
        @JvmStatic
        fun getInstance(questionPosition: Int) =
            SingleImageChoiceFragment().apply {
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
}