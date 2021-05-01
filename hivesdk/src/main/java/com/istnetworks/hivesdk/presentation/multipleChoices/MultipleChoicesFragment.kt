package com.istnetworks.hivesdk.presentation.multipleChoices

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
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
import com.istnetworks.hivesdk.databinding.FragmentMultipleChoicesBinding
import com.istnetworks.hivesdk.presentation.mainfragment.MainFragment
import com.istnetworks.hivesdk.presentation.surveyExtension.multiChoiceStyle
import com.istnetworks.hivesdk.presentation.surveyExtension.questionTitleStyle
import com.istnetworks.hivesdk.presentation.viewmodel.HiveSDKViewModel
import com.istnetworks.hivesdk.presentation.viewmodel.factory.HiveSDKViewModelFactory

private const val ARG_QUESTION_POSITION = "ARG_QUESTION_POSITION"
private const val TAG = "MultipleChoicesFragment"
class MultipleChoicesFragment : Fragment() , CompoundButton.OnCheckedChangeListener{
    private var questionPosition: Int? = null
    private var selectedQuestion: Question? = null
    private var isRequired: Boolean = false
    private lateinit var binding : FragmentMultipleChoicesBinding
    private var selectedChoices :ArrayList<SelectedChoices> = arrayListOf()
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
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMultipleChoicesBinding.inflate(inflater)
        binding.hveBtnSubmit.disable()
        observeViewModel()
        observeSurvey()
        onClickActions()
        return binding.root
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
//        if (surveyResponse.survey?.surveyOptions?.hasProgressBar == true)
//            binding.animateProgressBar.visibility = View.VISIBLE
        selectedQuestion = questionPosition?.let { viewModel.getQuestions(it) }

        binding.tvQuestionTitle.questionTitleStyle(surveyResponse.survey?.surveyOptions?.surveyTheme?.questionTitleStyle)
        binding.tvQuestionTitle.text = selectedQuestion?.title
        isRequired = selectedQuestion?.isRequired!!

        createChoices(selectedQuestion?.choices,surveyResponse.survey?.surveyOptions
                ?.surveyTheme?.questionChoicesStyle!!)

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
            this.view?.let { (requireParentFragment() as MainFragment).updatePagerHeightForChild(it) }

        }
    }
    private fun onClickActions() {

        binding.hveBtnSubmit.setOnClickListener {
            if (isRequired) {

            }else{
                onSurveyReadyToSave()
            }

        }

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
        viewModel.updateSelectedQuestions(
            selectedQuestion?.toQuestionResponse(
                "", 0,
                selectedChoices
            )
        )
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
}