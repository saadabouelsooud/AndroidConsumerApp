package com.istnetworks.hivesdk.data.models.response.styles


import com.google.gson.annotations.SerializedName

data class ThankyouMessage(
    @SerializedName("Mode")
    val mode: Int?,
    @SerializedName("SubTitle")
    val subTitle: String?,
    @SerializedName("SubTitleStyle")
    val subTitleStyle: SubTitleStyle?,
    @SerializedName("TemplateID")
    val templateID: String?,
    @SerializedName("Title")
    val title: String?,
    @SerializedName("TitleStyle")
    val titleStyle: TitleStyle?
)