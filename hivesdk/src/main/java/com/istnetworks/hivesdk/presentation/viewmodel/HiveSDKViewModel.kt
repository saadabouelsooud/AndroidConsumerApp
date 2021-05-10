package com.istnetworks.hivesdk.presentation.viewmodel

import android.graphics.Color
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.material.button.MaterialButton
import com.istnetworks.hivesdk.data.models.QuestionResponses
import com.istnetworks.hivesdk.data.models.RelevantWebSurveyBody
import com.istnetworks.hivesdk.data.models.RelevantWebSurveyResponse
import com.istnetworks.hivesdk.data.models.Status
import com.istnetworks.hivesdk.data.models.response.Question
import com.istnetworks.hivesdk.data.models.response.Survey
import com.istnetworks.hivesdk.data.models.response.toSaveSurveyBody
import com.istnetworks.hivesdk.data.repository.HiveSDKRepository
import com.istnetworks.hivesdk.data.utils.QuestionType
import com.istnetworks.hivesdk.data.utils.extensions.hide
import com.istnetworks.hivesdk.data.utils.extensions.onClick
import com.istnetworks.hivesdk.data.utils.extensions.show
import com.istnetworks.hivesdk.presentation.surveyExtension.isValidEmail
import com.istnetworks.hivesdk.presentation.surveyExtension.isValidUrl
import com.istnetworks.hivesdk.presentation.surveyExtension.submitButtonStyle
import kotlinx.coroutines.launch
import java.util.*

private const val PHONE_NUMBER_PATTERN = "[0-9][0-9]{10,16}"
class HiveSDKViewModel(private val hiveSDKRepository: HiveSDKRepository) : ViewModel() {

    val getSurveyResponseLD = MutableLiveData<RelevantWebSurveyResponse?>()
    var survey: Survey? = null
    val saveSurveyResponseLD = MutableLiveData<RelevantWebSurveyResponse?>()
    val isLoading = MutableLiveData<Boolean>()
    val showErrorMsg = MutableLiveData<String?>()
    val showIsRequiredErrMsgLD = MutableLiveData<Boolean?>()
    val showNotValidErrMsgLD = MutableLiveData<Boolean?>()
    private val questionResponsesList: MutableList<QuestionResponses> = mutableListOf()
    val updateProgressSliderLD = MutableLiveData<Float>()
    var previousQuestions = Stack<Int>()

    fun findQuestion(position: Int?): Question? {
        if (position == null || position == -1) return null
        return survey?.questions?.get(position)
    }

    private fun hasSkipLogic(qId: String): Boolean {
        return survey?.skipLogic?.any { it.questionGUID.equals(qId) } ?: false
    }

    fun getQuestionNumber(): Int {
        return previousQuestions.size+1
    }

    private fun getQuestionPositionByChoiceGUID(choiceGUID: String): Int {
        val questionTo =
            survey?.skipLogic?.findLast { it.qChoiceGUID.equals(choiceGUID) }!!.skipToQuestionGUID
        return survey?.questions?.indexOfFirst { it.surveyQuestionGUID!! == questionTo }!!
    }

    fun getSurvey(username: String, password: String) = viewModelScope.launch {
        val surveyBody = RelevantWebSurveyBody()
        surveyBody.language = "en"
        surveyBody.channel = "InApp"
        surveyBody.customerName = "saad"
        surveyBody.customerEmail = "ss@ss.com"
        surveyBody.customerPhone = "01234567890"
        surveyBody.dispositionCodes = listOf("complian Event")

        val surveyResult =
            hiveSDKRepository.getRelevantWebSurveyResource(username, password, surveyBody)
        when (surveyResult.status) {
            Status.SUCCESS -> {
                getSurveyResponseLD.value = surveyResult.data
            }
            Status.ERROR -> showErrorMsg.value = surveyResult.message
            Status.LOADING -> TODO()
        }
        survey = surveyResult.data?.survey

    }

    fun updateQuestionResponsesList(question: QuestionResponses?) {
        question?.let { q ->
            val duplicatedQuestion = questionResponsesList.find { it.questionID == q.questionID }
            if (duplicatedQuestion != null)
                questionResponsesList.remove(duplicatedQuestion)

            if (hasNoAnswer(question))
                return@let
            questionResponsesList.add(q)
            showIsRequiredErrMsgLD.value = false
        }
        updateProgressSliderLD.value = questionResponsesList.size.toFloat()
    }

    private fun hasNoAnswer(question: QuestionResponses) =
        (question.numberResponse == null
                && question.textResponse.isNullOrEmpty()
                && question.selectedChoices.isNullOrEmpty())

    fun saveSurvey() {
        viewModelScope.launch {
            val resp = hiveSDKRepository.saveWebSurvey(generateSaveSurveyRequest())
            when (resp.status) {
                Status.SUCCESS -> saveSurveyResponseLD.value = resp.data
                Status.ERROR -> showErrorMsg.value = resp.message
                Status.LOADING -> TODO()
            }
        }
    }

    fun getSurveyTheme()=survey?.surveyOptions?.surveyTheme

    fun getSurveyOptions() = survey?.surveyOptions

    fun getSurveyBackgroundColor()=Color.parseColor("#${getSurveyTheme()?.surveyBackgroundColor}")

    private fun generateSaveSurveyRequest() =
        survey?.toSaveSurveyBody(questionResponsesList)

    fun getQuestions(position: Int): Question {
        return getSurveyResponseLD.value!!.survey!!.questions!![position]
    }

    fun hasProgressBar(): Boolean? {
        return getSurveyResponseLD.value?.survey?.surveyOptions?.hasProgressBar
    }


    fun validateAnswer(questionPosition: Int): Boolean {
        val currentQuestion = survey?.questions?.get(questionPosition)
        return if (noResponseForCurrentQuestion(currentQuestion) && currentQuestion?.isRequired == false)
            true
        else
            isCurrentQuestionResponseValid(currentQuestion)
    }

    private fun isCurrentQuestionResponseValid(currentQuestion: Question?): Boolean {
        val questionResponse =
            questionResponsesList.find { it.questionID == currentQuestion?.surveyQuestionID }

        if (questionResponse == null) {
            showIsRequiredErrMsgLD.value = true
            return false
        }

        return when (currentQuestion?.questionType) {
            QuestionType.EmailInput.value -> {
                val validEmail = questionResponse.textResponse.isValidEmail()
                showNotValidErrMsgLD.value = !validEmail
                validEmail
            }
            QuestionType.URLInput.value -> {
                val validUrl = questionResponse.textResponse?.isValidUrl()
                showNotValidErrMsgLD.value = validUrl == false
                validUrl == true
            }
            QuestionType.PhoneNumberInput.value -> {
                val validPhone =
                    questionResponse.textResponse?.matches(PHONE_NUMBER_PATTERN.toRegex())
                showNotValidErrMsgLD.value = validPhone == false
                validPhone == true
            }
            else -> true
        }


    }

    private fun getQuestionResponseByPosition(current: Int): QuestionResponses? {
        val currentQuestion = survey?.questions?.get(current)

        return questionResponsesList.find { it.questionID == currentQuestion?.surveyQuestionID }
    }

    private fun noResponseForCurrentQuestion(currentQuestion: Question?): Boolean {
        val questionResponse =
            questionResponsesList.find { it.questionID == currentQuestion?.surveyQuestionID }

        return questionResponse == null
    }

    fun getTheNextQuestionPosition(currentQuestionPosition: Int): Int {
        previousQuestions.push(currentQuestionPosition)
        try {

            return when (hasSkipLogic(findQuestion(currentQuestionPosition)!!.surveyQuestionGUID!!)) {
                true -> getQuestionPositionByChoiceGUID(
                    getQuestionResponseByPosition(currentQuestionPosition)!!.choiceGUID!!
                )
                false -> currentQuestionPosition + 1
            }
        } catch (e: NullPointerException) {
            return currentQuestionPosition + 1
        }
    }

    fun getThePreviousPosition(): Int {

        return if (previousQuestions.size > 0) {
            previousQuestions.pop()
        } else {
            return 0
        }
    }

    fun setSubmitButtonBasedOnPosition(btn: MaterialButton, questionPosition: Int?) {
        btn.submitButtonStyle(getSurveyTheme()?.submitButton)
        if (survey?.questions?.lastIndex == questionPosition) {
            btn.show()
        } else {
            btn.hide()
        }
        btn.onClick {
            saveSurvey()
        }
    }

}