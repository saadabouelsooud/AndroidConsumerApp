package com.istnetworks.hivesdk.data.models.response

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.istnetworks.hivesdk.data.models.Choices
import com.istnetworks.hivesdk.data.models.QuestionResponses
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@Parcelize
data class Question(
    @SerializedName("QuestionGUID")
    @Expose
    val surveyQuestionGUID: String? = "",
    @SerializedName("QuestionID")
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
    val StarOption: StarOption? = null
) : Parcelable

fun Question.toQuestionResponse(textResponse: String?, numberResponse: Int?): QuestionResponses {
    return QuestionResponses(
        surveyQuestionID, surveyQuestionGUID, textResponse, numberResponse, listOf()
    )
}