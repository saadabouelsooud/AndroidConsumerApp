package com.istnetworks.hivesdk.presentation.spinnerquestion

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.annotation.Keep
import androidx.annotation.NonNull
import androidx.core.os.bundleOf
import com.istnetworks.hivesdk.R
import com.istnetworks.hivesdk.data.models.response.Question
import com.istnetworks.hivesdk.data.models.response.toQuestionResponse
import com.istnetworks.hivesdk.data.utils.extensions.hide
import com.istnetworks.hivesdk.data.utils.extensions.show
import com.istnetworks.hivesdk.databinding.FragmentSpinnerQuestionBinding
import com.istnetworks.hivesdk.presentation.BaseQuestionFragment
import com.istnetworks.hivesdk.presentation.interfaces.IsRequiredInterface
import com.istnetworks.hivesdk.presentation.interfaces.SubmitButtonControl
import com.istnetworks.hivesdk.presentation.mainfragment.MainFragment
import com.istnetworks.hivesdk.presentation.surveyExtension.questionTitleStyle


const val ARG_POSITION = "pos"

class SpinnerQuestionFragment : BaseQuestionFragment(), IsRequiredInterface,SubmitButtonControl {
    private lateinit var binding: FragmentSpinnerQuestionBinding
    private var selectedQuestion: Question? = null
    private val position: Int? by lazy { arguments?.getInt(ARG_POSITION, -1) }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSpinnerQuestionBinding.inflate(inflater)
        selectedQuestion = viewModel.findQuestion(position)
        stylingViews()
        initSubmitBtn()
        onClickActions()
        setSpinner()
        listenToSpinnerSelection()
        observeViewModel()
        return binding.root
    }

    private fun observeViewModel() {
    }

    override fun onStart() {
        super.onStart()
        Log.d("TAG", "onStart: ")

    }

    override fun onResume() {
        super.onResume()
        bindQuestion()

        updatePagerHeight(binding.root)
        position?.let { viewModel.getDestinationsSubmitted(it) }
    }

    private fun stylingViews() {
        val theme = viewModel.getSurveyTheme()
        binding.tvQuestionTitle.questionTitleStyle(theme?.questionTitleStyle)
    }

    private fun bindQuestion() {

        binding.tvQuestionTitle.text = context?.getString(R.string.question_format,
            viewModel.getQuestionNumber(),selectedQuestion?.title)
    }

    private fun initSubmitBtn() {
        viewModel.setSubmitButtonBasedOnPosition(binding.hveBtnSubmit,position)
    }


    private fun setSpinner() {
        val list = selectedQuestion?.choices?.map { it.title }?.toMutableList()
        list?.add(0, getString(R.string.choose_spinner))
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item, list ?: listOf()
        )
        binding.hveSpAnswers.adapter = adapter
        this.view?.let { (requireParentFragment() as MainFragment)
            .updatePagerHeightForChild(it) }
    }

    @Keep
    companion object {
        @JvmStatic
        fun getInstance(@NonNull position: Int): SpinnerQuestionFragment {
            val f = SpinnerQuestionFragment()
            f.arguments = bundleOf(ARG_POSITION to position)
            return f
        }

    }

    private fun onClickActions() {

    }

    private fun listenToSpinnerSelection() {
        binding.hveSpAnswers.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position > 0)
                    viewModel.updateQuestionResponsesList(
                        selectedQuestion?.toQuestionResponse(
                            binding.hveSpAnswers.selectedItem.toString(),
                            position - 1,
                            choiceGUID = selectedQuestion?.choices?.get(position - 1)?.choiceGUID
                        )
                    )
                else {
                    viewModel.updateQuestionResponsesList(
                        selectedQuestion?.toQuestionResponse(
                            null,
                            null,
                        )
                    )
                }
                position?.let { viewModel.getDestinationsSubmitted(it) }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

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