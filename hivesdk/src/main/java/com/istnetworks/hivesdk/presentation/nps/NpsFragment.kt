package com.istnetworks.hivesdk.presentation.nps

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
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
import com.istnetworks.hivesdk.presentation.surveyExtension.questionStyle
import com.istnetworks.hivesdk.presentation.surveyExtension.surveyTitleStyle
import com.istnetworks.hivesdk.presentation.viewmodel.HiveSDKViewModel
import com.istnetworks.hivesdk.presentation.viewmodel.factory.HiveSDKViewModelFactory


class NpsFragment : Fragment() {
    private lateinit var binding: FragmentNpsBinding
    private val viewModel: HiveSDKViewModel by activityViewModels {
        HiveSDKViewModelFactory(
            HiveSDKRepositoryImpl()
        )
    }
    private var selectedQuestion: Question? = null
    private var isRequired: Boolean = false
    private var npsValue: Int = -1
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNpsBinding.inflate(inflater)
        binding.hveBtnSubmit.disable()
        setNpsList()
        observeSurvey()
        observeViewModel()
        viewModel.stylingSubmitBtn(binding.hveBtnSubmit)
        onClickActions()
        return binding.root
    }


    private fun observeViewModel() {
        viewModel.showErrorMsg.observe(viewLifecycleOwner, {
            Toast.makeText(requireContext(), "$it", Toast.LENGTH_SHORT).show()
        })
        viewModel.isLoading.observe(viewLifecycleOwner, {
           if(it){
               binding.animateProgressBar.visibility=View.VISIBLE
           }else {
               binding.animateProgressBar.visibility=View.GONE
           }

        })
        viewModel.saveSurveyResponseLD.observe(viewLifecycleOwner, {
            Toast.makeText(requireContext(), it?.message, Toast.LENGTH_SHORT).show()
            requireActivity().finish()
        })
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

        binding.ivClose.setOnClickListener{
            requireActivity().finish()
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
        val surveyResponse = CacheInMemory.getSurveyResponse()
        if (surveyResponse.survey?.surveyOptions?.hasProgressBar == true)
            binding.animateProgressBar.visibility = View.VISIBLE
        binding.tvSurveyTitle.text = surveyResponse.survey?.title
        binding.tvSurveyTitle.surveyTitleStyle(surveyResponse.survey?.surveyOptions?.surveyTheme?.surveyTitleStyle)
        binding.tvQuestionTitle.questionStyle(surveyResponse.survey?.surveyOptions?.surveyTheme?.questionTitleStyle)
        for (i in surveyResponse.survey?.questions?.indices!!) {
            if (surveyResponse.survey.questions[i].questionType == QuestionType.NPS.value) {
                binding.tvQuestionTitle.text = surveyResponse.survey.questions[i].title
                isRequired = surveyResponse.survey.questions[i].isRequired!!
                selectedQuestion = surveyResponse.survey.questions[i]
                if (i == 0)
                    binding.ivPrevQuestion.visibility = View.GONE
                break

            }


        }
    }

    private fun validateNextButton() {
        if (isRequired) {
            if (npsValue >= 0) {

            } else Toast.makeText(activity, getString(R.string.required), Toast.LENGTH_LONG).show()
        }

    }



    private fun setNpsList() {
        val nps: ArrayList<NpsModel> = ArrayList()
        nps.add(NpsModel(npsBackgroundColor = "#e43e3d", npsSelectedHeight = 24, npsSelectedWidth = 24, npsUnselectedHeight = 18, npsUnselectedWidth = 18, npsText = "0"))
        nps.add(NpsModel(npsBackgroundColor = "#e43e3d", npsSelectedHeight = 24, npsSelectedWidth = 24, npsUnselectedHeight = 18, npsUnselectedWidth = 18, npsText = "1"))
        nps.add(NpsModel(npsBackgroundColor = "#ea484d", npsSelectedHeight = 24, npsSelectedWidth = 24, npsUnselectedHeight = 18, npsUnselectedWidth = 18, npsText = "2"))
        nps.add(NpsModel(npsBackgroundColor = "#ec654e", npsSelectedHeight = 24, npsSelectedWidth = 24, npsUnselectedHeight = 18, npsUnselectedWidth = 18, npsText = "3"))
        nps.add(NpsModel(npsBackgroundColor = "#f3a84c", npsSelectedHeight = 24, npsSelectedWidth = 24, npsUnselectedHeight = 18, npsUnselectedWidth = 18, npsText = "4"))
        nps.add(NpsModel(npsBackgroundColor = "#f8c43e", npsSelectedHeight = 24, npsSelectedWidth = 24, npsUnselectedHeight = 18, npsUnselectedWidth = 18, npsText = "5"))
        nps.add(NpsModel(npsBackgroundColor = "#e1c63b", npsSelectedHeight = 24, npsSelectedWidth = 24, npsUnselectedHeight = 18, npsUnselectedWidth = 18, npsText = "6"))
        nps.add(NpsModel(npsBackgroundColor = "#e1c63b", npsSelectedHeight = 24, npsSelectedWidth = 24, npsUnselectedHeight = 18, npsUnselectedWidth = 18, npsText = "7"))
        nps.add(NpsModel(npsBackgroundColor = "#9fce35", npsSelectedHeight = 24, npsSelectedWidth = 24, npsUnselectedHeight = 18, npsUnselectedWidth = 18, npsText = "8"))
        nps.add(NpsModel(npsBackgroundColor = "#7fcd31", npsSelectedHeight = 24, npsSelectedWidth = 24, npsUnselectedHeight = 18, npsUnselectedWidth = 18, npsText = "9"))
        nps.add(NpsModel(npsBackgroundColor = "#5aaf2b", npsSelectedHeight = 24, npsSelectedWidth = 24, npsUnselectedHeight = 18, npsUnselectedWidth = 18, npsText = "10"))

        binding.npsRecyclerView.layoutManager = GridLayoutManager(context, 11)
        val adapter = NpsAdapter(nps) {
            npsValue = it ?: -1
            binding.hveBtnSubmit.enable()
        }
        binding.npsRecyclerView.adapter = adapter
    }
}