package com.istnetworks.hivesdk.presentation.singleChoice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.istnetworks.hivesdk.R
import com.istnetworks.hivesdk.data.local.CacheInMemory
import com.istnetworks.hivesdk.data.models.Choices
import com.istnetworks.hivesdk.data.models.SelectedChoices
import com.istnetworks.hivesdk.data.models.response.Question
import com.istnetworks.hivesdk.data.models.response.styles.QuestionChoicesStyle
import com.istnetworks.hivesdk.data.models.response.toQuestionResponse
import com.istnetworks.hivesdk.data.repository.HiveSDKRepositoryImpl
import com.istnetworks.hivesdk.data.utils.extensions.disable
import com.istnetworks.hivesdk.databinding.FragmentSingleChoiceBinding
import com.istnetworks.hivesdk.presentation.mainfragment.MainFragment
import com.istnetworks.hivesdk.presentation.surveyExtension.questionTitleStyle
import com.istnetworks.hivesdk.presentation.surveyExtension.singleChoiceStyle
import com.istnetworks.hivesdk.presentation.viewmodel.HiveSDKViewModel
import com.istnetworks.hivesdk.presentation.viewmodel.factory.HiveSDKViewModelFactory

private const val ARG_QUESTION_POSITION = "ARG_QUESTION_POSITION"
private const val TAG = "SingleChoiceFragment"
class SingleChoiceFragment : Fragment() {
    private var questionPosition: Int? = null
    private var selectedQuestion: Question? = null
    private var isRequired: Boolean = false
    private var selectedChoice :Int =0
    private lateinit var binding:FragmentSingleChoiceBinding
    private val viewModel: HiveSDKViewModel by activityViewModels {
        HiveSDKViewModelFactory(
            HiveSDKRepositoryImpl()
        )
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            questionPosition = it.getInt(ARG_QUESTION_POSITION, 0)
        }
    }

    override fun onResume() {
        super.onResume()
        binding.root.requestLayout()
        (requireParentFragment() as MainFragment).updatePagerHeightForChild(binding.root)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSingleChoiceBinding.inflate(inflater)

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
//        viewModel.isLoading.observe(viewLifecycleOwner, {
//            if(it){
//                binding.animateProgressBar.visibility=View.VISIBLE
//            }else {
//                binding.animateProgressBar.visibility=View.GONE
//            }
//
//        })
        viewModel.saveSurveyResponseLD.observe(viewLifecycleOwner, {
            Toast.makeText(requireContext(), it?.message, Toast.LENGTH_SHORT).show()
            requireActivity().finish()
        })
    }

    private fun observeSurvey() {
        val surveyResponse = CacheInMemory.getSurveyResponse()

        selectedQuestion = questionPosition?.let { viewModel.getQuestions(it) }
        binding.tvQuestionTitle.questionTitleStyle(surveyResponse.survey?.surveyOptions?.surveyTheme?.questionTitleStyle)
        binding.tvQuestionTitle.text = context?.getString(R.string.question_format,
            viewModel.previousQuestions.size.plus(1),selectedQuestion?.title)
        isRequired = selectedQuestion?.isRequired!!

        createChoices(selectedQuestion?.choices,
            surveyResponse.survey?.surveyOptions?.surveyTheme?.questionChoicesStyle!!)

    }

    private fun createChoices(choiceList: List<Choices>?,style:QuestionChoicesStyle){
        val inflater = LayoutInflater.from(context)
        for (choice in choiceList!!) {
            val rbChoice = inflater.inflate(R.layout.single_choice_item
                , binding.rgSingleChoiceWrapper, false) as RadioButton

            rbChoice.id = choice.choiceID!!
            rbChoice.text = choice.title

            rbChoice.singleChoiceStyle(style)
            binding.rgSingleChoiceWrapper.addView(rbChoice)

        }
    }

    private fun onClickActions() {


        binding.rgSingleChoiceWrapper.setOnCheckedChangeListener { radioGroup, i ->
            val checkedId = radioGroup.checkedRadioButtonId
            val selectedChoice = selectedQuestion?.choices?.find { it.choiceID == checkedId }
            viewModel.updateQuestionResponsesList(
                selectedQuestion?.toQuestionResponse("",0,
                listOf(SelectedChoices(selectedChoice?.choiceID,selectedChoice?.choiceGUID)
                )
                )
            )
        }


    }

    private fun onSurveyReadyToSave() {
//        viewModel.updateSelectedQuestions(
//            selectedQuestion?.toQuestionResponse(
//                "",
//                npsValue
//            )
//        )
//        viewModel.saveSurvey()
    }



    companion object {
        /**
         *
         * @param questionPosition Parameter 1.
         * @return A new instance of fragment SingleChoiceFragment.
         */
        @JvmStatic
        fun newInstance(questionPosition: Int) =
            SingleChoiceFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_QUESTION_POSITION, questionPosition)
                }
            }
    }
}