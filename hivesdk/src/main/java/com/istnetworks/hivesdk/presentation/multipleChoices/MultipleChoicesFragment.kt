package com.istnetworks.hivesdk.presentation.multipleChoices

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.Toast
import com.istnetworks.hivesdk.R
import com.istnetworks.hivesdk.data.models.Choices
import com.istnetworks.hivesdk.data.models.SelectedChoices
import com.istnetworks.hivesdk.data.models.response.Question
import com.istnetworks.hivesdk.data.models.response.styles.QuestionChoicesStyle
import com.istnetworks.hivesdk.data.models.response.toQuestionResponse
import com.istnetworks.hivesdk.data.utils.extensions.hide
import com.istnetworks.hivesdk.data.utils.extensions.show
import com.istnetworks.hivesdk.databinding.FragmentMultipleChoicesBinding
import com.istnetworks.hivesdk.presentation.BaseQuestionFragment
import com.istnetworks.hivesdk.presentation.interfaces.IsRequiredInterface
import com.istnetworks.hivesdk.presentation.interfaces.SubmitButtonInterface
import com.istnetworks.hivesdk.presentation.surveyExtension.multiChoiceStyle
import com.istnetworks.hivesdk.presentation.surveyExtension.questionTitleStyle

private const val ARG_QUESTION_POSITION = "ARG_QUESTION_POSITION"
private const val TAG = "MultipleChoicesFragment"
class MultipleChoicesFragment : BaseQuestionFragment() ,
    CompoundButton.OnCheckedChangeListener,IsRequiredInterface,SubmitButtonInterface{
    private var questionPosition: Int? = null
    private var selectedQuestion: Question? = null
    private var isRequired: Boolean = false
    private lateinit var binding : FragmentMultipleChoicesBinding
    private var selectedChoices :ArrayList<SelectedChoices> = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            questionPosition = it.getInt(ARG_QUESTION_POSITION, 0)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMultipleChoicesBinding.inflate(inflater)

        observeViewModel()
        observeSurvey()
        onClickActions()
        initSubmitBtn()
     //  questionPosition?.let { viewModel.updateSubmitBtnAndNxtArrow(it) }

        return binding.root
    }


    override fun onResume() {
        super.onResume()
        bindQuestionTitle()
        updatePagerHeight(binding.root)
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
        selectedQuestion = questionPosition?.let { viewModel.findQuestion(it) }
        binding.tvQuestionTitle.questionTitleStyle(viewModel.getSurveyTheme()?.questionTitleStyle)

        isRequired = selectedQuestion?.isRequired!!
        createChoices(selectedQuestion?.choices,viewModel.getSurveyTheme()?.questionChoicesStyle!!)

    }

    private fun bindQuestionTitle() {
        binding.tvQuestionTitle.text = context?.getString(
            R.string.question_format,
            viewModel.getQuestionNumber(), selectedQuestion?.title
        )
    }

    private fun createChoices(choiceList: List<Choices>?, style: QuestionChoicesStyle) {
        val inflater = LayoutInflater.from(context)
        for (choice in choiceList!!) {
            val cbChoice = inflater.inflate(R.layout.multi_choice_item
                , binding.hveLiMultipleChoiceWrapper, false) as CheckBox

            cbChoice.id = choice.choiceID!!
            cbChoice.text = choice.title

            cbChoice.multiChoiceStyle(style)
            cbChoice.setOnCheckedChangeListener(this)
            binding.hveLiMultipleChoiceWrapper.addView(cbChoice)

        }
    }
    private fun onClickActions() {


    }

    override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
        val checkedId = p0?.id
        val selectedChoice = selectedQuestion?.choices?.find { it.choiceID == checkedId }
        if (p1) {
            selectedChoices.add(SelectedChoices(selectedChoice?.choiceID, selectedChoice?.choiceGUID))
        }
        else
        {
            selectedChoices.removeIf { it -> it.choiceID == checkedId }
        }
        viewModel.updateQuestionResponsesList(
            selectedQuestion?.toQuestionResponse(
                "", null,
                selectedChoices
            )
        )
        questionPosition?.let { viewModel.updateSubmitBtnVisibilityBeforeAnswerChosen(it) }
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
            MultipleChoicesFragment().apply {
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