package com.istnetworks.hivesdk.data.models.response.styles


import com.google.gson.annotations.SerializedName

data class SubmitButton(
    @SerializedName("BackgroundColor")
    val backgroundColor: String?,
    @SerializedName("BorderColor")
    val borderColor: String?,
    @SerializedName("FontBold")
    val fontBold: Boolean?,
    @SerializedName("FontColor")
    val fontColor: String?,
    @SerializedName("FontFamily")
    val fontFamily: String?,
    @SerializedName("FontItalic")
    val fontItalic: Boolean?,
    @SerializedName("FontSize")
    val fontSize: String?,
    @SerializedName("FontUnderline")
    val fontUnderline: Boolean?,
    @SerializedName("HoverBackground")
    val hoverBackground: String?,
    @SerializedName("HoverFontColor")
    val hoverFontColor: String?
)