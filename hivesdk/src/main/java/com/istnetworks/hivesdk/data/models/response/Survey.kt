package com.istnetworks.hivesdk.data.models.response

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.istnetworks.hivesdk.data.models.QuestionResponses
import com.istnetworks.hivesdk.data.models.SaveWebSurveyBody
import com.istnetworks.hivesdk.data.models.SurveyOptions
import com.istnetworks.hivesdk.data.models.WebSkipLogic
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@Parcelize
data class Survey(
    @SerializedName("InvitationGuid")
    @Expose
    val invitationGuid: String? = "",
    @SerializedName("Title")
    @Expose
    val title: String? = "",
    @SerializedName("SurveyGuid")
    @Expose
    val surveyGuid: String? = "",
    @SerializedName("SurveyID")
    @Expose
    val surveyID: Int? = 0,
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
) : Parcelable

fun Survey.toSaveSurveyBody(questionResponses: List<QuestionResponses>): SaveWebSurveyBody {
    return SaveWebSurveyBody(invitationGuid, surveyGuid, surveyID, questionResponses)
}
