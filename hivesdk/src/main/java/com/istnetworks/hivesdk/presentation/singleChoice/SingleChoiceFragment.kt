package com.istnetworks.hivesdk.presentation.singleChoice

import android.os.Bundle
import android.util.LayoutDirection
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.core.view.marginTop
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.google.android.material.resources.TextAppearance
import com.istnetworks.hivesdk.R
import com.istnetworks.hivesdk.data.local.CacheInMemory
import com.istnetworks.hivesdk.data.models.response.Question
import com.istnetworks.hivesdk.data.repository.HiveSDKRepositoryImpl
import com.istnetworks.hivesdk.data.utils.extensions.disable
import com.istnetworks.hivesdk.databinding.FragmentSingleChoiceBinding
import com.istnetworks.hivesdk.presentation.surveyExtension.questionTitleStyle
import com.istnetworks.hivesdk.presentation.surveyExtension.singleChoiceStyle
import com.istnetworks.hivesdk.presentation.viewmodel.HiveSDKViewModel
import com.istnetworks.hivesdk.presentation.viewmodel.factory.HiveSDKViewModelFactory

private const val ARG_QUESTION_POSITION = "ARG_QUESTION_POSITION"

class SingleChoiceFragment : Fragment() {
    private var questionPosition: Int? = null
    private var selectedQuestion: Question? = null
    private var isRequired: Boolean = false
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSingleChoiceBinding.inflate(inflater)
        binding.hveBtnSubmit.disable()
        observeViewModel()
        onClickActions()
        observeSurvey()
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
        if (questionPosition == 0)
            binding.ivPrevQuestion.visibility = View.GONE

        val inflater = LayoutInflater.from(context)
        for (choice in selectedQuestion?.choices!!) {
            val rbChoice = inflater.inflate(R.layout.single_choice_item
                , binding.rgSingleChoiceWrapper, false) as RadioButton
            rbChoice.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                , ViewGroup.LayoutParams.WRAP_CONTENT)
            rbChoice.layoutDirection = View.LAYOUT_DIRECTION_RTL
            rbChoice.id = choice.choiceID!!
            rbChoice.text = choice.title
            rbChoice.singleChoiceStyle(surveyResponse.survey?.surveyOptions?.surveyTheme?.questionChoicesStyle!!)
            binding.rgSingleChoiceWrapper.addView(rbChoice)
        }

    }

    private fun onClickActions() {
        binding.ivPrevQuestion.setOnClickListener {
            val navController = view?.let { Navigation.findNavController(it) }
            if (navController?.currentDestination?.id == R.id.npsFragment)
                navController.popBackStack()
        }
        binding.ivNextQuestion.setOnClickListener {
            validateNextButton()
        }
        binding.hveBtnSubmit.setOnClickListener {
            if (isRequired) {

            }else{
                onSurveyReadyToSave()
            }

        }

        binding.rgSingleChoiceWrapper.
        setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { radioGroup, i ->

        })


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

    private fun validateNextButton() {
//        if (isRequired) {
//            if (npsValue >= 0) {
//
//            } else Toast.makeText(activity, getString(R.string.required), Toast.LENGTH_LONG).show()
//        }

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