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
import com.istnetworks.hivesdk.data.utils.HiveSDKType
import com.istnetworks.hivesdk.data.utils.QuestionType
import com.istnetworks.hivesdk.data.utils.extensions.*
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
    var showSubmitButtonLD = MutableLiveData<Boolean> ()
    var enableNextButtonLD = MutableLiveData<Boolean>()

    /**=
     *  TODO: dependency inversion
     */
    private lateinit var skipHandler: SkipLogicHandler

    fun findQuestion(position: Int?): Question? {
        if (position == null || position == -1) return null
        return survey?.questions?.get(position)
    }

    fun getQuestionNumber(): Int {
        return previousQuestions.size + 1
    }

    private fun getSkipToQuestionPosition(currentQuestionPosition: Int): Int {
        val response: QuestionResponses? = getQuestionResponseByPosition(currentQuestionPosition)
        val question = findQuestion(currentQuestionPosition)
        val questionTo =
            when (question?.questionType) {
                in QuestionType.TextInput.value..QuestionType.URLInput.value,
                QuestionType.MultipleChoiceQuestion.value,
                QuestionType.ImageMCQ.value,
                QuestionType.DateQuestion.value -> {
                    if (hasNoAnswer(response))
                        survey?.questions?.get(currentQuestionPosition + 1)?.surveyQuestionGUID
                    else
                        skipHandler.getSkipToByQuestionGuid(question)
                }

                QuestionType.ListQuestion.value,
                QuestionType.SingleChoice.value,
                QuestionType.CSAT.value -> {
                    skipHandler.getSkipToByQuestionGuidAndChoiceGuid(
                        question.surveyQuestionGUID,
                        response?.choiceGUID
                    )
                }
                QuestionType.SlideQuestion.value,
                QuestionType.StarQuestion.value,
                QuestionType.Emoji.value,
                QuestionType.NPS.value -> {
                    skipHandler.getSkipToByNumberAndQuestionGuid(question, response?.numberResponse)
                }
                else -> {
                    null
                }
            }

        return survey?.questions?.indexOfFirst { it.surveyQuestionGUID!! == questionTo } ?: -1
    }

    fun surveyBackgroundColor() = Color.parseColor("#" + getSurveyTheme()?.surveyBackgroundColor)

    fun isFullScreen(): Boolean {
        return survey?.surveyOptions?.displayMode == HiveSDKType.FULLSCREEN.value
    }

    /**
     * @param questionPosition : takes the current question position to get it's details
     * set screen configurations by
     * 1- if question has answer
     * 2- if has skip logic
     * 3- if the skip logic is skip To end
     * 4- Question type is single choice question
     *
     * this method is being called from
     * 1- onResume method
     * 2- Update Answer method
     *
     * @return using public mutableLiveData variable observe the result on MainFragment
     * and notify the current screen
     */
    fun updateSubmitBtnVisibilityBeforeAnswerChosen(questionPosition: Int) {

        val question = survey?.questions?.get(questionPosition)
        val hasAnswer = hasQuestionResponse(question!!.surveyQuestionID!!)
        val hasSkipLogic = skipHandler.hasSkipLogic(question.surveyQuestionGUID!!)
        if (hasSkipLogic && !hasAnswer && isSingleChoiceModeQuestion(question))
            showSubmitButtonLD.value = true
    }

    fun updateNextArrowEnablingOnFragmentChange(questionPosition: Int) {

        val question = survey?.questions?.get(questionPosition)
        val hasAnswer = hasQuestionResponse(question!!.surveyQuestionID!!)
        val hasSkipLogic = skipHandler.hasSkipLogic(question.surveyQuestionGUID!!)
        val skipTo = getSkipToQuestionPosition(questionPosition)
        if (isSingleChoiceModeQuestion(question))
            if (hasSkipLogic)
                if (hasAnswer)
                    enableNextButtonLD.value = skipTo != -1
                else
                    enableNextButtonLD.value = false
            else
                enableNextButtonLD.value = true
        else
            enableNextButtonLD.value = true


    }

    private fun isSingleChoiceModeQuestion(question: Question) =
        (question.questionType == QuestionType.SingleChoice.value
                || question.questionType == QuestionType.StarQuestion.value
                || question.questionType == QuestionType.SlideQuestion.value
                || question.questionType == QuestionType.NPS.value
                || question.questionType == QuestionType.ListQuestion.value
                || question.questionType == QuestionType.Emoji.value)

    private fun getQuestionResponseByPosition(current: Int): QuestionResponses? {
        val currentQuestion = survey?.questions?.get(current)

        return questionResponsesList.find { it.questionID == currentQuestion?.surveyQuestionID }
    }

    private fun hasQuestionResponse(questionId: Int?): Boolean {
        return questionResponsesList.any { it.questionID == questionId } ?: false
    }

    fun getSurvey(username: String, password: String) = viewModelScope.launch {
        val surveyBody = RelevantWebSurveyBody()
        surveyBody.language = "en"
        surveyBody.channel = "InApp"
        surveyBody.customerName = "saad"
        surveyBody.customerEmail = "ss@ss.com"
        surveyBody.customerPhone = "01234567890"
        surveyBody.dispositionCodes = listOf("login Event")

        val surveyResult =
            hiveSDKRepository.getRelevantWebSurveyResource(username, password, surveyBody)
        when (surveyResult.status) {
            Status.SUCCESS -> {
                getSurveyResponseLD.value = surveyResult.data
                isLoading.value=false
            }
            Status.ERROR -> {
                showErrorMsg.value = surveyResult.message
                isLoading.value = false
            }
            Status.LOADING -> isLoading.value = true
        }

        survey = surveyResult.data?.survey
        skipHandler = SkipLogicHandler(survey?.skipLogic, survey?.questions)
    }

    fun updateQuestionResponsesList(questionResponse: QuestionResponses?) {
        questionResponse?.let { q ->
            val duplicatedQuestion = questionResponsesList.find { it.questionID == q.questionID }
            if (duplicatedQuestion != null)
                questionResponsesList.remove(duplicatedQuestion)

            if (hasNoAnswer(questionResponse))
                return@let
            questionResponsesList.add(q)
            showIsRequiredErrMsgLD.value = false

            updateSubmitAndNxtAfterAnswerChosen(q.questionGUID)
        }
        updateProgressBar(questionResponse?.questionID)
    }

    private fun updateSubmitAndNxtAfterAnswerChosen(qGuid: String?) {
        if (qGuid == null) return
        val questionPosition = survey?.questions?.indexOfFirst {
            it.surveyQuestionGUID == qGuid
        }
        if (questionPosition == null || questionPosition == -1) return
        if (skipHandler.hasSkipLogic(qGuid)) {
            val skipTo = getSkipToQuestionPosition(questionPosition)
            if (skipTo == -1) //skip to end
            {
                showSubmitButtonLD.value = true
                enableNextButtonLD.value = false
            } else if (skipTo != -1) {
                showSubmitButtonLD.value = false
                enableNextButtonLD.value = true
            }

        } else {
            enableNextButtonLD.value = true
        }

    }


    private fun updateProgressBar(questionID: Int?) {
        questionID?.let {
            val totalQuestions = survey?.questions
            val currentQuestionPosition =
                totalQuestions?.indexOf(totalQuestions.find { it.surveyQuestionID == questionID })
            currentQuestionPosition?.let {
                val nextPosition = getTheNextQuestionPosition(currentQuestionPosition)
                var skippedQuestionsNumber = nextPosition - currentQuestionPosition
                if (skippedQuestionsNumber < 0)
                    skippedQuestionsNumber *= -1
                updateProgressSliderLD.value =
                    questionResponsesList.size.toFloat() + skippedQuestionsNumber
            }
        }

    }

    private fun hasNoAnswer(question: QuestionResponses?) =
        (question?.numberResponse == null
                && question?.textResponse.isNullOrEmpty()
                && question?.selectedChoices.isNullOrEmpty())

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

    fun getSurveyTheme() = survey?.surveyOptions?.surveyTheme

    fun getSurveyOptions() = survey?.surveyOptions

    private fun generateSaveSurveyRequest() =
        survey?.toSaveSurveyBody(questionResponsesList)


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

    private fun noResponseForCurrentQuestion(currentQuestion: Question?): Boolean {
        val questionResponse =
            questionResponsesList.find { it.questionID == currentQuestion?.surveyQuestionID }

        return questionResponse == null
    }

    fun getTheNextQuestionPosition(currentQuestionPosition: Int): Int {
        val currentQuestion = findQuestion(currentQuestionPosition)
        return if (skipHandler.hasSkipLogic(currentQuestion?.surveyQuestionGUID)) {
            val skipTo = getSkipToQuestionPosition(currentQuestionPosition)
            if (skipTo == -1) currentQuestionPosition + 1 else skipTo
        } else
            currentQuestionPosition + 1
    }

    fun getAndPopPreviousPosition(): Int {

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
        val question = findQuestion(questionPosition)
        if (question?.isRequired == true && hasQuestionResponse(question.surveyQuestionID)) {
            btn.disable()
        } else {
            btn.enable()
        }

        btn.onClick {
            saveSurvey()
        }
    }

    fun pushToPreviousQuestionsStack(currentQuestionPosition: Int) {
        previousQuestions.push(currentQuestionPosition)
    }

}