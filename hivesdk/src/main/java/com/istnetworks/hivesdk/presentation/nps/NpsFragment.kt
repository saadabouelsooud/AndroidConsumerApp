package com.istnetworks.hivesdk.presentation.nps

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.istnetworks.hivesdk.R
import com.istnetworks.hivesdk.data.models.response.Question
import com.istnetworks.hivesdk.data.models.response.toQuestionResponse
import com.istnetworks.hivesdk.data.utils.extensions.hide
import com.istnetworks.hivesdk.data.utils.extensions.show
import com.istnetworks.hivesdk.databinding.FragmentNpsBinding
import com.istnetworks.hivesdk.presentation.BaseQuestionFragment
import com.istnetworks.hivesdk.presentation.interfaces.IsRequiredInterface
import com.istnetworks.hivesdk.presentation.interfaces.SubmitButtonInterface
import com.istnetworks.hivesdk.presentation.surveyExtension.questionTitleStyle
import com.istnetworks.hivesdk.presentation.surveyExtension.submitButtonStyle

private const val ARG_QUESTION_POSITION = "ARG_QUESTION_POSITION"
private const val SELECTED_WIDTH = 30
private const val UN_SELECTED_WIDTH = 18
private const val SELECTED_HEIGHT = SELECTED_WIDTH
private const val UN_SELECTED_HEIGHT = UN_SELECTED_WIDTH

class NpsFragment : BaseQuestionFragment(),IsRequiredInterface,SubmitButtonInterface {
    private var questionPosition: Int? = null
    private lateinit var binding: FragmentNpsBinding
    private var selectedQuestion: Question? = null
    private var isRequired: Boolean = false


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

        setNpsList()
        initQuestion()
        observeViewModel()
        binding.hveBtnSubmit.submitButtonStyle(viewModel.getSurveyTheme()?.submitButton)
        onClickActions()
        initSubmitBtn()
        return binding.root
    }


    private fun initSubmitBtn() {
        viewModel.setSubmitButtonBasedOnPosition(binding.hveBtnSubmit,questionPosition)
    }

    override fun onResume() {
        super.onResume()
        bindQuestionTitle()
        updatePagerHeight(binding.root)
        questionPosition?.let { viewModel.updateSubmitBtnVisibilityBeforeAnswerChosen(it) }
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

    private fun onClickActions() {

    }

    private fun onSurveyReadyToSave(npsValue: Int?) {
        viewModel.updateQuestionResponsesList(
            selectedQuestion?.toQuestionResponse(
                "",
                npsValue
            )
        )

        questionPosition?.let { viewModel.updateSubmitBtnVisibilityBeforeAnswerChosen(it) }
    }


    private fun initQuestion() {
        selectedQuestion = viewModel.findQuestion(questionPosition)
        binding.tvQuestionTitle.questionTitleStyle(viewModel.getSurveyTheme()?.questionTitleStyle)
    }

    private fun bindQuestionTitle() {
        binding.tvQuestionTitle.text = context?.getString(
            R.string.question_format,
            viewModel.getQuestionNumber(), selectedQuestion?.title
        )
    }


    private fun setNpsList() {
        val nps: ArrayList<NpsModel> = ArrayList()
        nps.add(
            NpsModel(
                npsBackgroundColor = "#e43e3d",
                npsSelectedHeight = SELECTED_HEIGHT,
                npsSelectedWidth = SELECTED_WIDTH,
                npsUnselectedHeight = UN_SELECTED_HEIGHT,
                npsUnselectedWidth = UN_SELECTED_WIDTH,
                npsText = "0"
            )
        )
        nps.add(
            NpsModel(
                npsBackgroundColor = "#e43e3d",
                npsSelectedHeight = SELECTED_HEIGHT,
                npsSelectedWidth = SELECTED_WIDTH,
                npsUnselectedHeight = UN_SELECTED_HEIGHT,
                npsUnselectedWidth = UN_SELECTED_WIDTH,
                npsText = "1"
            )
        )
        nps.add(
            NpsModel(
                npsBackgroundColor = "#ea484d",
                npsSelectedHeight = SELECTED_HEIGHT,
                npsSelectedWidth = SELECTED_WIDTH,
                npsUnselectedHeight = UN_SELECTED_HEIGHT,
                npsUnselectedWidth = UN_SELECTED_WIDTH,
                npsText = "2"
            )
        )
        nps.add(
            NpsModel(
                npsBackgroundColor = "#ec654e",
                npsSelectedHeight = SELECTED_HEIGHT,
                npsSelectedWidth = SELECTED_WIDTH,
                npsUnselectedHeight = UN_SELECTED_HEIGHT,
                npsUnselectedWidth = UN_SELECTED_WIDTH,
                npsText = "3"
            )
        )
        nps.add(
            NpsModel(
                npsBackgroundColor = "#f3a84c",
                npsSelectedHeight = SELECTED_HEIGHT,
                npsSelectedWidth = SELECTED_WIDTH,
                npsUnselectedHeight = UN_SELECTED_HEIGHT,
                npsUnselectedWidth = UN_SELECTED_WIDTH,
                npsText = "4"
            )
        )
        nps.add(
            NpsModel(
                npsBackgroundColor = "#f8c43e",
                npsSelectedHeight = SELECTED_HEIGHT,
                npsSelectedWidth = SELECTED_WIDTH,
                npsUnselectedHeight = UN_SELECTED_HEIGHT,
                npsUnselectedWidth = UN_SELECTED_WIDTH,
                npsText = "5"
            )
        )
        nps.add(
            NpsModel(
                npsBackgroundColor = "#e1c63b",
                npsSelectedHeight = SELECTED_HEIGHT,
                npsSelectedWidth = SELECTED_WIDTH,
                npsUnselectedHeight = UN_SELECTED_HEIGHT,
                npsUnselectedWidth = UN_SELECTED_WIDTH,
                npsText = "6"
            )
        )
        nps.add(
            NpsModel(
                npsBackgroundColor = "#e1c63b",
                npsSelectedHeight = SELECTED_HEIGHT,
                npsSelectedWidth = SELECTED_WIDTH,
                npsUnselectedHeight = UN_SELECTED_HEIGHT,
                npsUnselectedWidth = UN_SELECTED_WIDTH,
                npsText = "7"
            )
        )
        nps.add(
            NpsModel(
                npsBackgroundColor = "#9fce35",
                npsSelectedHeight = SELECTED_HEIGHT,
                npsSelectedWidth = SELECTED_WIDTH,
                npsUnselectedHeight = UN_SELECTED_HEIGHT,
                npsUnselectedWidth = UN_SELECTED_WIDTH,
                npsText = "8"
            )
        )
        nps.add(
            NpsModel(
                npsBackgroundColor = "#7fcd31",
                npsSelectedHeight = SELECTED_HEIGHT,
                npsSelectedWidth = SELECTED_WIDTH,
                npsUnselectedHeight = UN_SELECTED_HEIGHT,
                npsUnselectedWidth = UN_SELECTED_WIDTH,
                npsText = "9"
            )
        )
        nps.add(
            NpsModel(
                npsBackgroundColor = "#5aaf2b",
                npsSelectedHeight = SELECTED_HEIGHT,
                npsSelectedWidth = SELECTED_WIDTH,
                npsUnselectedHeight = UN_SELECTED_HEIGHT,
                npsUnselectedWidth = UN_SELECTED_WIDTH,
                npsText = "10"
            )
        )

        val flexAdapter = FlexboxLayoutManager(context, FlexDirection.ROW, FlexWrap.NOWRAP)
        flexAdapter.justifyContent = JustifyContent.SPACE_BETWEEN
        binding.npsRecyclerView.setItemViewCacheSize(10);
        binding.npsRecyclerView.layoutManager = flexAdapter
        val adapter = NpsAdapter(nps) {
            onSurveyReadyToSave(it)
        }
        adapter.setHasStableIds(true)
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