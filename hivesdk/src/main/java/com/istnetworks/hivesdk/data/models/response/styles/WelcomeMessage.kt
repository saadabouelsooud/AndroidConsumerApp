package com.istnetworks.hivesdk.data.models.response.styles


import com.google.gson.annotations.SerializedName

data class WelcomeMessage(
    @SerializedName("Mode")
    val mode: Int?,
    @SerializedName("SubTitle")
    val subTitle: String?,
    @SerializedName("SubTitleStyle")
    val subTitleStyle: SubTitleStyleX?,
    @SerializedName("TemplateID")
    val templateID: Any?,
    @SerializedName("Title")
    val title: String?,
    @SerializedName("TitleStyle")
    val titleStyle: TitleStyleX?
)