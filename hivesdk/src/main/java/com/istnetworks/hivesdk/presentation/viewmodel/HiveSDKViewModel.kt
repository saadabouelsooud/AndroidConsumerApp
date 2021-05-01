package com.istnetworks.hivesdk.presentation.viewmodel

import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RectShape
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.istnetworks.hivesdk.data.models.QuestionResponses
import com.istnetworks.hivesdk.data.models.RelevantWebSurveyBody
import com.istnetworks.hivesdk.data.models.RelevantWebSurveyResponse
import com.istnetworks.hivesdk.data.models.Status
import com.istnetworks.hivesdk.data.models.response.Question
import com.istnetworks.hivesdk.data.models.response.Survey
import com.istnetworks.hivesdk.data.models.response.styles.SubmitButton
import com.istnetworks.hivesdk.data.models.response.toSaveSurveyBody
import com.istnetworks.hivesdk.data.repository.HiveSDKRepository
import com.istnetworks.hivesdk.data.utils.QuestionType
import kotlinx.coroutines.launch


class HiveSDKViewModel(private val hiveSDKRepository: HiveSDKRepository) : ViewModel() {

    val getSurveyResponseLD = MutableLiveData<RelevantWebSurveyResponse?>()
    var survey: Survey? = null
    val saveSurveyResponseLD = MutableLiveData<RelevantWebSurveyResponse?>()
    val isLoading = MutableLiveData<Boolean>()
    val showErrorMsg = MutableLiveData<String?>()
    private val questionResponsesList: MutableList<QuestionResponses> = mutableListOf()


    fun findQuestion(position: Int?): Question? {
        if (position == null || position == -1) return null
        return survey?.questions?.get(position)
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
        when(surveyResult.status){
            Status.SUCCESS -> getSurveyResponseLD.value = surveyResult.data
            Status.ERROR -> showErrorMsg.value = surveyResult.message
            Status.LOADING -> TODO()
        }
        survey = surveyResult.data?.survey

    }

    fun updateSelectedQuestions(question: QuestionResponses?) {
        question?.let { q ->
            val duplicatedQuestion = questionResponsesList.find { it.questionID == q.questionID }
            if (duplicatedQuestion != null)
                questionResponsesList.remove(duplicatedQuestion)
            questionResponsesList.add(q)
        }

    }

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

    private fun createSubmitBtnDrawable(submitBtnStyle: SubmitButton?): ShapeDrawable {
        val drawable = ShapeDrawable()
        drawable.shape = RectShape()
        drawable.paint.color = Color.parseColor("#${submitBtnStyle?.backgroundColor}")
        drawable.paint.strokeWidth = 10f
        drawable.paint.style = Paint.Style.STROKE
        return drawable
    }

    private fun getFontStyle(submitBtnStyle: SubmitButton?) =
        when {
            submitBtnStyle?.fontBold == true -> Typeface.BOLD
            submitBtnStyle?.fontItalic == true -> Typeface.ITALIC
            else -> Typeface.NORMAL
        }

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
        val currentQuestionId = survey?.questions?.get(questionPosition)?.surveyQuestionID
        return questionResponsesList.find { it.questionID == currentQuestionId } != null
                || currentQuestion?.isRequired == false
    }

    fun getTheNextQuestionPosition(currentQuestion: Int): Int {
        //TODO Skip Logic
        return currentQuestion + 1
    }

    fun getThePreviousPosition(currentItem: Int): Int {
        return currentItem-1
    }

}