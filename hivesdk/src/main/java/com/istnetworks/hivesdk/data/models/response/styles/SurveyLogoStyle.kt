package com.istnetworks.hivesdk.data.models.response.styles


import com.google.gson.annotations.SerializedName

data class SurveyLogoStyle(
    @SerializedName("ActualWidth")
    val actualWidth: Int?,
    @SerializedName("Opacity")
    val opacity: Int?,
    @SerializedName("Position")
    val position: String?,
    @SerializedName("SizePercentage")
    val sizePercentage: Int?,
    @SerializedName("URL")
    val uRL: String?
)