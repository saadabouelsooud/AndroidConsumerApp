package com.istnetworks.hivesdk.presentation.singleImageChoice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.istnetworks.hivesdk.R
import com.istnetworks.hivesdk.data.local.CacheInMemory
import com.istnetworks.hivesdk.data.models.Choices
import com.istnetworks.hivesdk.data.models.SelectedChoices
import com.istnetworks.hivesdk.data.models.response.Question
import com.istnetworks.hivesdk.data.models.response.styles.QuestionChoicesStyle
import com.istnetworks.hivesdk.data.models.response.toQuestionResponse
import com.istnetworks.hivesdk.data.repository.HiveSDKRepositoryImpl
import com.istnetworks.hivesdk.data.utils.extensions.disable
import com.istnetworks.hivesdk.databinding.FragmentSingleImageChoiceBinding
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
private const val TAG = "SingleImageChoiceFragment"

class SingleImageChoiceFragment : Fragment() {
    private var questionPosition: Int? = null
    private var selectedQuestion: Question? = null
    private var isRequired: Boolean = false
    private lateinit var binding: FragmentSingleImageChoiceBinding
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
        // Inflate the layout for this fragment
        binding = FragmentSingleImageChoiceBinding.inflate(inflater)
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

        selectedQuestion = questionPosition?.let { viewModel.getQuestions(it) }
        binding.hveTvQuestionTitle.questionTitleStyle(surveyResponse.survey?.surveyOptions?.surveyTheme?.questionTitleStyle)
        binding.hveTvQuestionTitle.text = selectedQuestion?.title
        isRequired = selectedQuestion?.isRequired!!

      /*  createChoices(selectedQuestion?.choices,surveyResponse?.survey?.surveyOptions?.
        surveyTheme?.questionChoicesStyle!!)*/

    }

    private fun createChoices(choiceList: List<Choices>?, style: QuestionChoicesStyle){
        val inflater = LayoutInflater.from(context)
        for (choice in choiceList!!) {
            val rbChoice = inflater.inflate(
                R.layout.single_choice_image_item, binding.hveRgSingleChoiceWrapper, false
            ) as RadioButton

            rbChoice.id = choice.choiceID!!

            lifecycleScope.launch {
                withContext(Dispatchers.IO)
                {
                    val bitmap =
                        Picasso.get().load(choice.imageURL)
                            .placeholder(R.drawable.emoji_bad)
                            .resize(200, 200)
                            .get().toDrawable(resources)
                    withContext(Dispatchers.Main) {

                        rbChoice.setCompoundDrawablesWithIntrinsicBounds(bitmap, null, null, null)

                        (requireParentFragment() as MainFragment).updatePagerHeightForChild(binding.root)
                    }
                }

            }
            rbChoice.singleChoiceStyle(style)
            rbChoice.setPadding(32, 16, 16, 16)
            binding.hveRgSingleChoiceWrapper.addView(rbChoice)
            this.view?.let { (requireParentFragment() as MainFragment).updatePagerHeightForChild(it) }

        }

    }

    private fun onClickActions() {

        binding.hveBtnSubmit.setOnClickListener {
            if (isRequired) {

            } else {
                onSurveyReadyToSave()
            }

        }

        binding.hveRgSingleChoiceWrapper.setOnCheckedChangeListener { radioGroup, i ->
            val checkedId = radioGroup.checkedRadioButtonId
            val selectedChoice = selectedQuestion?.choices?.find { it.choiceID == checkedId }
            viewModel.updateSelectedQuestions(
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
//        viewModel.updateSelectedQuestions(
//            selectedQuestion?.toQuestionResponse(
//                "",
//                0,
//                listOf(
//                    SelectedChoices(sele?.choiceID, selectedChoice?.choiceGUID)
//                )
//            )
//        )
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
}