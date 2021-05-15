package com.istnetworks.hivesdk.presentation.viewmodel

import android.graphics.Color
import android.util.Log
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
    var showSubmitButton = MutableLiveData<Boolean> ()
    var enableNextButton = MutableLiveData<Boolean>()

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

    /**
     * @param currentQuestionPosition takes the current question position to get it's details
     * this method gets the skip to question based on the question type and it's response
     * gets  the question response choiceID
     * then gets question detail using position
     * based on question type either get skip to from SkipLogicHandler
     * @return SkipTo question position in Questions list
     */
    private fun getQuestionPositionByChoice(currentQuestionPosition: Int): Int {
        val choiceGUID :String? = getQuestionResponseByPosition(currentQuestionPosition)?.choiceGUID
        val question = findQuestion(currentQuestionPosition)!!
        val questionTo =
            when (question.questionType) {
                in QuestionType.TextInput.value..QuestionType.URLInput.value,
                QuestionType.DateQuestion.value,
                QuestionType.MultipleChoiceQuestion.value,
                QuestionType.ImageMCQ.value -> {
                    skipHandler.freeInputAndDateSkip(question)
                        ?: survey?.skipLogic?.findLast { it.qChoiceGUID.equals(choiceGUID) }?.skipToQuestionGUID
                }
                QuestionType.SlideQuestion.value,
                QuestionType.StarQuestion.value,
                QuestionType.Emoji.value,
                QuestionType.NPS.value,
                QuestionType.CSAT.value-> {
                    if (hasQuestionResponse(question.surveyQuestionID!!)) {
                        val response =
                            questionResponsesList.find { it.questionID == question.surveyQuestionID }
                        skipHandler.singleChoiceQuestionsSkip(
                            question,
                            response?.numberResponse
                        )
                    } else {
                        survey?.skipLogic?.findLast { it.qChoiceGUID.equals(choiceGUID) }?.skipToQuestionGUID

                    }
                }
                else -> {
                    survey?.skipLogic?.findLast { it.qChoiceGUID.equals(choiceGUID) }?.skipToQuestionGUID
                }
            }
        return survey?.questions?.indexOfFirst { it.surveyQuestionGUID!! == questionTo }?: -1
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
    fun getDestinationsSubmitted(questionPosition:Int){
        val question = survey?.questions?.get(questionPosition)
        val hasAnswer = hasQuestionResponse(question!!.surveyQuestionID!!)
        val hasSkipLogic = skipHandler.hasSkipLogic(question.surveyQuestionGUID!!)
        val skipTo = getQuestionPositionByChoice(questionPosition)

            if(hasSkipLogic && hasAnswer && !question.isRequired!!
            && (question.questionType == QuestionType.SingleChoice.value
                    ||question.questionType == QuestionType.StarQuestion.value
                    ||question.questionType == QuestionType.SlideQuestion.value
                    ||question.questionType == QuestionType.NPS.value
                    ||question.questionType == QuestionType.Emoji.value)){
            showSubmitButton.value = false
            enableNextButton.value = true
        }
        else if (hasSkipLogic && !question.isRequired!! && !hasAnswer)
        {
            showSubmitButton.value = true
            enableNextButton.value = false
        }
        if(skipTo ==-1)
        {
            showSubmitButton.value = true
            enableNextButton.value = false
        }
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
//        getDestinationsSubmitted(question!!.questionGUID!!)
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
                true -> {
                    val skipToQuestion = getQuestionPositionByChoice(currentQuestionPosition)

                    if(skipToQuestion ==-1)
                    {
                        currentQuestionPosition +1
                    }
                    else
                    {
                        skipToQuestion
                    }
                }
                false -> currentQuestionPosition + 1
            }
        } catch (e: NullPointerException) {
            currentQuestionPosition + 1
            Log.e("crash", "getTheNextQuestionPosition: ", e)
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
        if (survey?.questions?.lastIndex == questionPosition ) {
            btn.show()
        } else {
            btn.hide()
        }
        btn.onClick {
            saveSurvey()
        }
    }

}