package com.istnetworks.hivesdk.data.models.response.styles


import com.google.gson.annotations.SerializedName

data class SurveyTheme(
    @SerializedName("EndScreenMsg")
    val endScreenMsg: Any?,
    @SerializedName("EndScreenMsgStyle")
    val endScreenMsgStyle: EndScreenMsgStyle?,
    @SerializedName("FocusColor")
    val focusColor: String?,
    @SerializedName("InputAnswerStyle")
    val inputAnswerStyle: InputAnswerStyle?,
    @SerializedName("QuestionChoicesStyle")
    val questionChoicesStyle: QuestionChoicesStyle?,
    @SerializedName("QuestionTitleStyle")
    val questionTitleStyle: QuestionTitleStyle?,
    @SerializedName("StartScreenMsg")
    val startScreenMsg: Any?,
    @SerializedName("StartScreenMsgStyle")
    val startScreenMsgStyle: StartScreenMsgStyle?,
    @SerializedName("SubmitButton")
    val submitButton: SubmitButton?,
    @SerializedName("SurveyBackgroundColor")
    val surveyBackgroundColor: String?,
    @SerializedName("SurveyLogoStyle")
    val surveyLogoStyle: SurveyLogoStyle?,
    @SerializedName("SurveyTitleStyle")
    val surveyTitleStyle: SurveyTitleStyle?,
    @SerializedName("ThankyouMessage")
    val thankyouMessage: ThankyouMessage?,
    @SerializedName("TitleOrientation")
    val titleOrientation: String?,
    @SerializedName("WelcomeMessage")
    val welcomeMessage: WelcomeMessage?
)