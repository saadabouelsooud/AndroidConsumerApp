package com.istnetworks.hivesdk.presentation.nps

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.istnetworks.hivesdk.R
import com.istnetworks.hivesdk.data.local.CacheInMemory
import com.istnetworks.hivesdk.data.models.response.Question
import com.istnetworks.hivesdk.data.models.response.toQuestionResponse
import com.istnetworks.hivesdk.data.repository.HiveSDKRepositoryImpl
import com.istnetworks.hivesdk.data.utils.QuestionType
import com.istnetworks.hivesdk.data.utils.extensions.disable
import com.istnetworks.hivesdk.data.utils.extensions.enable
import com.istnetworks.hivesdk.databinding.FragmentNpsBinding
import com.istnetworks.hivesdk.presentation.mainfragment.MainFragment
import com.istnetworks.hivesdk.presentation.multipleChoices.MultipleChoicesFragment
import com.istnetworks.hivesdk.presentation.surveyExtension.questionTitleStyle
import com.istnetworks.hivesdk.presentation.surveyExtension.submitButtonStyle
import com.istnetworks.hivesdk.presentation.surveyExtension.surveyTitleStyle
import com.istnetworks.hivesdk.presentation.viewmodel.HiveSDKViewModel
import com.istnetworks.hivesdk.presentation.viewmodel.factory.HiveSDKViewModelFactory

private const val ARG_QUESTION_POSITION = "ARG_QUESTION_POSITION"

class NpsFragment : Fragment() {
    private var questionPosition: Int? = null
    private lateinit var binding: FragmentNpsBinding
    private val viewModel: HiveSDKViewModel by activityViewModels {
        HiveSDKViewModelFactory(
            HiveSDKRepositoryImpl()
        )
    }
    private var selectedQuestion: Question? = null
    private var isRequired: Boolean = false
    private var npsValue: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            questionPosition = it.getInt(ARG_QUESTION_POSITION, 0)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNpsBinding.inflate(inflater)
        binding.hveBtnSubmit.disable()
        setNpsList()
        observeSurvey()
        observeViewModel()
        binding.hveBtnSubmit.submitButtonStyle(viewModel.getSurveyTheme()?.submitButton)
        onClickActions()
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        binding.root.requestLayout()
    }
    private fun observeViewModel() {
        viewModel.showErrorMsg.observe(viewLifecycleOwner, {
            Toast.makeText(requireContext(), "$it", Toast.LENGTH_SHORT).show()
        })
//        viewModel.isLoading.observe(viewLifecycleOwner, {
//           if(it){
//               binding.animateProgressBar.visibility=View.VISIBLE
//           }else {
//               binding.animateProgressBar.visibility=View.GONE
//           }
//
//        })
        viewModel.saveSurveyResponseLD.observe(viewLifecycleOwner, {
            Toast.makeText(requireContext(), it?.message, Toast.LENGTH_SHORT).show()
            requireActivity().finish()
        })
    }

    private fun onClickActions() {

        binding.hveBtnSubmit.setOnClickListener {
            if (isRequired) {
                if (npsValue >= 0) {
                    onSurveyReadyToSave()
                } else Toast.makeText(
                    requireContext(),
                    getString(R.string.required),
                    Toast.LENGTH_LONG
                )
                    .show()
            }else{
                onSurveyReadyToSave()
            }

        }
    }

    private fun onSurveyReadyToSave() {
        viewModel.updateSelectedQuestions(
            selectedQuestion?.toQuestionResponse(
                "",
                npsValue
            )
        )
        viewModel.saveSurvey()
    }


    private fun observeSurvey() {
        selectedQuestion = viewModel.findQuestion(questionPosition)
        binding.tvQuestionTitle.questionTitleStyle(viewModel.getSurveyTheme()?.questionTitleStyle)
        binding.tvQuestionTitle.text = selectedQuestion?.title
        isRequired = selectedQuestion?.isRequired ?:false
    }


    private fun setNpsList() {
        val nps: ArrayList<NpsModel> = ArrayList()
        nps.add(NpsModel(npsBackgroundColor = "#e43e3d", npsSelectedHeight = 22, npsSelectedWidth = 22, npsUnselectedHeight = 18, npsUnselectedWidth = 18, npsText = "0"))
        nps.add(NpsModel(npsBackgroundColor = "#e43e3d", npsSelectedHeight = 22, npsSelectedWidth = 22, npsUnselectedHeight = 18, npsUnselectedWidth = 18, npsText = "1"))
        nps.add(NpsModel(npsBackgroundColor = "#ea484d", npsSelectedHeight = 22, npsSelectedWidth = 22, npsUnselectedHeight = 18, npsUnselectedWidth = 18, npsText = "2"))
        nps.add(NpsModel(npsBackgroundColor = "#ec654e", npsSelectedHeight = 22, npsSelectedWidth = 22, npsUnselectedHeight = 18, npsUnselectedWidth = 18, npsText = "3"))
        nps.add(NpsModel(npsBackgroundColor = "#f3a84c", npsSelectedHeight = 22, npsSelectedWidth = 22, npsUnselectedHeight = 18, npsUnselectedWidth = 18, npsText = "4"))
        nps.add(NpsModel(npsBackgroundColor = "#f8c43e", npsSelectedHeight = 22, npsSelectedWidth = 22, npsUnselectedHeight = 18, npsUnselectedWidth = 18, npsText = "5"))
        nps.add(NpsModel(npsBackgroundColor = "#e1c63b", npsSelectedHeight = 22, npsSelectedWidth = 22, npsUnselectedHeight = 18, npsUnselectedWidth = 18, npsText = "6"))
        nps.add(NpsModel(npsBackgroundColor = "#e1c63b", npsSelectedHeight = 22, npsSelectedWidth = 22, npsUnselectedHeight = 18, npsUnselectedWidth = 18, npsText = "7"))
        nps.add(NpsModel(npsBackgroundColor = "#9fce35", npsSelectedHeight = 22, npsSelectedWidth = 22, npsUnselectedHeight = 18, npsUnselectedWidth = 18, npsText = "8"))
        nps.add(NpsModel(npsBackgroundColor = "#7fcd31", npsSelectedHeight = 22, npsSelectedWidth = 22, npsUnselectedHeight = 18, npsUnselectedWidth = 18, npsText = "9"))
        nps.add(NpsModel(npsBackgroundColor = "#5aaf2b", npsSelectedHeight = 22, npsSelectedWidth = 22, npsUnselectedHeight = 18, npsUnselectedWidth = 18, npsText = "10"))

        binding.npsRecyclerView.layoutManager = GridLayoutManager(context, 11)
        val adapter = NpsAdapter(nps) {
            npsValue = it ?: -1
            binding.hveBtnSubmit.enable()
        }
        binding.npsRecyclerView.adapter = adapter
    }

    companion object{
        /**
         *
         * @param questionPosition Parameter 1.
         * @return A new instance of fragment SingleChoiceFragment.
         */

        @JvmStatic
        fun getInstance(questionPosition :Int)=
            NpsFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_QUESTION_POSITION, questionPosition)
                }
            }
    }
}