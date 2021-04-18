package com.istnetworks.hivesdk

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class TokenBody(
        @SerializedName("Username")
        @Expose
        val userName: String? = "",
        @SerializedName("grant_type")
        @Expose
        val grantType: String? = "",
        @SerializedName("Password")
        @Expose
        val password: String? = "",
        @SerializedName("refresh_token")
        @Expose
        val refreshToken: String? = ""
):Serializable

data class TokenResponse(
        @SerializedName("access_token")
        @Expose
        val accessToken: String? = "",
        @SerializedName("token_type")
        @Expose
        val tokenType: String? = "",
        @SerializedName("expires_in")
        @Expose
        val expiresIn: Int? = 0,
        @SerializedName("refresh_token")
        @Expose
        val refreshToken: String? = ""
):Serializable

data class RelevantWebSurveyBody(
        @SerializedName("CustomerCIC")
        @Expose
        var customerCIC: String? = "",
        @SerializedName("Channel")
        @Expose
        var channel: String? = "",
        @SerializedName("Language")
        @Expose
        var language: String? = "",
        @SerializedName("DispositionCodes")
        @Expose
        val dispositionCodes: String? = "",
        @SerializedName("CustomerName")
        @Expose
        val customerName: String? = "",
        @SerializedName("CustomerEmail")
        @Expose
        val customerEmail: String? = "",
        @SerializedName("CustomerPhone")
        @Expose
        val customerPhone: String? = ""
):Serializable

data class RelevantWebSurveyResponse(
        @SerializedName("IsSuccess")
        @Expose
        val isSuccess: Boolean? = false,
        @SerializedName("Message")
        @Expose
        val message: String? = "",
        @SerializedName("StatusCode")
        @Expose
        val StatusCode: Int? = 0,
        @SerializedName("Survey")
        @Expose
        val survey: Survey? = null
):Serializable

data class Survey(
        @SerializedName("InvitationGuid")
        @Expose
        val invitationGuid: String? = "",
        @SerializedName("SurveyGuid")
        @Expose
        val surveyGuid: String? = "",
        @SerializedName("Name")
        @Expose
        val name: String? = "",
        @SerializedName("SurveyLanguage")
        @Expose
        val surveyLanguage: Int? = 0,
        @SerializedName("QuestionType")
        @Expose
        val questionType: Int? = 0,
        @SerializedName("Questions")
        @Expose
        val questions: List<Question>? = null,

        @SerializedName("SkipLogic")
        @Expose
        val skipLogic: List<WebSkipLogic>? = null,

        @SerializedName("SurveyOptions")
        @Expose
        val surveyOptions: SurveyOptions? = null
):Serializable

data class SurveyOptions(
        @SerializedName("DisplayMode")
        @Expose
        val displayMode: Int? = 0,
        @SerializedName("HasProgressBar")
        @Expose
        val hasProgressBar: Boolean? = false,
        @SerializedName("ProgressBarPosition")
        @Expose
        val progressBarPosition: Int? = 0,
        @SerializedName("EnableCloseButton")
        @Expose
        val enableCloseButton: Boolean? = false,

):Serializable

data class Question(
        @SerializedName("SurveyQuestionGUID")
        @Expose
        val surveyQuestionGUID: String? = "",
        @SerializedName("SurveyQuestionID")
        @Expose
        val surveyQuestionID: Int? = 0,
        @SerializedName("Title")
        @Expose
        val title: String? = "",
        @SerializedName("IsRequired")
        @Expose
        val isRequired: Boolean? = false,
        @SerializedName("QuestionType")
        @Expose
        val questionType: Int? = 0,
        @SerializedName("Choices")
        @Expose
        val choices: List<Choices>? = null,
        @SerializedName("StarOption")
        @Expose
        val StarOption: Any? = null
):Serializable

data class Choices(
        @SerializedName("ChoiceGUID")
        @Expose
        val choiceGUID: String? = "",
        @SerializedName("ChoiceID")
        @Expose
        val choiceID: Int? = 0,
        @SerializedName("Title")
        @Expose
        val title: String? = "",
        @SerializedName("SurveyQuestionGUID")
        @Expose
        val surveyQuestionGUID: String? = "",
        @SerializedName("ImageURL")
        @Expose
        val imageURL: String? = ""
):Serializable

data class WebSkipLogic(
        @SerializedName("ID")
        @Expose
        val id: Int? = 0,
        @SerializedName("QuestionGUID")
        @Expose
        val questionGUID: String? = "",
        @SerializedName("QChoiceGUID")
        @Expose
        val qChoiceGUID: String? = "",
        @SerializedName("MinValue")
        @Expose
        val minValue: Int? = 0,
        @SerializedName("MaxValue")
        @Expose
        val maxValue: Int? = 0,
        @SerializedName("SkipToQuestionGUID")
        @Expose
        val skipToQuestionGUID: String? = ""
):Serializable

data class SaveWebSurveyBody(
        @SerializedName("InvitationGuid")
        @Expose
        val invitationGuid: String? = "",
        @SerializedName("SurveyGuid")
        @Expose
        val surveyGuid: String? = "",
        @SerializedName("SurveyID")
        @Expose
        val surveyID: Int? = 0,
        @SerializedName("QuestionResponses")
        @Expose
        val questionResponses: List<QuestionResponses>? = null
):Serializable

data class QuestionResponses(
        @SerializedName("QuestionID")
        @Expose
        val questionID: Int? = 0,
        @SerializedName("QuestionGUID")
        @Expose
        val questionGUID: String? = "",
        @SerializedName("TextResponse")
        @Expose
        val textResponse: String? = "",
        @SerializedName("NumberResponse")
        @Expose
        val numberResponse: Int? = 0,
        @SerializedName("SelectedChoices")
        @Expose
        val selectedChoices: List<SelectedChoices>? = null
):Serializable

data class SelectedChoices(
        @SerializedName("ChoiceID")
        @Expose
        val choiceID: Int? = 0,
        @SerializedName("ChoiceGuid")
        @Expose
        val choiceGuid: String? = ""
):Serializable
