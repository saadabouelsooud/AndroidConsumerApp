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

    /**
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

    private fun getQuestionPositionByChoiceGUID( currentQuestionPosition: Int): Int {
        val choiceGUID = getQuestionResponseByPosition(currentQuestionPosition)!!.choiceGUID
        val question = findQuestion(currentQuestionPosition)!!
        val questionTo =
            when (question.questionType) {
                in QuestionType.TextInput.value..QuestionType.URLInput.value,
                QuestionType.DateQuestion.value,
                QuestionType.MultipleChoiceQuestion.value,
                QuestionType.ImageMCQ.value -> {
                    skipHandler.freeInputAndDateSkip(question)
                        ?: survey?.skipLogic?.findLast { it.qChoiceGUID.equals(choiceGUID) }!!.skipToQuestionGUID
                }
                QuestionType.SlideQuestion.value,
                QuestionType.StarQuestion.value,
                QuestionType.Emoji.value,
                QuestionType.NPS.value-> {
                    if (hasQuestionResponse(question.surveyQuestionID!!)) {
                        val response =
                            questionResponsesList.find { it.questionID == question.surveyQuestionID }
                        val choiceId = skipHandler.sliderAndRatingQuestionSkip(question,response!!.numberResponse!!)
                        //Show submit button
                        if (choiceId.equals("00000000-0000-0000-0000-000000000000")){

                        }
                        else{

                        }
                        choiceId
                    }
                    else
                    {
                        survey?.skipLogic?.findLast { it.qChoiceGUID.equals(choiceGUID) }!!.skipToQuestionGUID
                    }
                }
                else -> {
                    survey?.skipLogic?.findLast { it.qChoiceGUID.equals(choiceGUID) }!!.skipToQuestionGUID
                }
            }

        return survey?.questions?.indexOfFirst { it.surveyQuestionGUID!! == questionTo }!!
    }

    private fun getQuestionResponseByPosition(current: Int): QuestionResponses? {
        val currentQuestion = survey?.questions?.get(current)

        return questionResponsesList.find { it.questionID == currentQuestion?.surveyQuestionID }
    }

    private fun hasQuestionResponse(questionId: Int): Boolean {
        return questionResponsesList.any { it.questionID == questionId }
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
        }

        updateProgressBar(questionResponse?.questionID)
    }

    private fun updateProgressBar(questionID: Int?) {
        questionID?.let {
            val totalQuestions = survey?.questions
            val currentQuestionPosition =
                totalQuestions?.indexOf(totalQuestions.find { it.surveyQuestionID == questionID })
            currentQuestionPosition?.let {
                val nextPosition = getQuestionPositionByChoiceGUID(currentQuestionPosition)
                val skippedQuestionsNumber = nextPosition - currentQuestionPosition
                updateProgressSliderLD.value =
                    questionResponsesList.size.toFloat() + skippedQuestionsNumber
            }
        }

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

    fun getSurveyTheme() = survey?.surveyOptions?.surveyTheme

    fun getSurveyOptions() = survey?.surveyOptions

    fun getSurveyBackgroundColor() = Color.parseColor("#${getSurveyTheme()?.surveyBackgroundColor}")

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

    private fun noResponseForCurrentQuestion(currentQuestion: Question?): Boolean {
        val questionResponse =
            questionResponsesList.find { it.questionID == currentQuestion?.surveyQuestionID }

        return questionResponse == null
    }

    fun getTheNextQuestionPosition(currentQuestionPosition: Int): Int {
        previousQuestions.push(currentQuestionPosition)
        val currentQuestion = findQuestion(currentQuestionPosition)
        return try {

            when (skipHandler.hasSkipLogic(currentQuestion!!.surveyQuestionGUID!!)) {
                true -> getQuestionPositionByChoiceGUID(currentQuestionPosition)
                false -> currentQuestionPosition + 1
            }
        } catch (e: NullPointerException) {
            currentQuestionPosition + 1
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