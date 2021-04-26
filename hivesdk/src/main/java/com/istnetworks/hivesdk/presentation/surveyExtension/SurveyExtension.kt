package com.istnetworks.hivesdk.presentation.surveyExtension

import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.widget.CheckBox
import android.widget.RadioButton
import android.widget.TextView
import com.google.android.material.button.MaterialButton
import com.istnetworks.hivesdk.data.models.response.styles.*

/**
 * Survey title style
 */
fun TextView.surveyTitleStyle(titleStyle: SurveyTitleStyle?){
    setTextColor(Color.parseColor("#" + titleStyle?.fontColor))
    typeface = getFontTypeface(titleStyle?.fontBold!!, titleStyle.fontItalic!!,
        titleStyle.fontFamily!!)
    val sp: Float =
        titleStyle.
        fontSize?.
        replace("px","")?.toFloat() ?: 12f  / resources.displayMetrics.scaledDensity+ 0.5f

    textSize = sp

    if (titleStyle.fontUnderline!!)
    {
        paintFlags = Paint.UNDERLINE_TEXT_FLAG
    }
}

/**
 * Welcome screen title style
 */
fun TextView.welcomeScreenTitleStyle(titleStyle: TitleStyle?){
    setTextColor(Color.parseColor("#" + titleStyle?.fontColor))
    typeface = getFontTypeface(titleStyle?.fontBold!!, titleStyle.fontItalic!!,
        titleStyle.fontFamily!!)
    val sp: Float =
        titleStyle.
        fontSize?.
        replace("px","")?.toFloat() ?: 12f  / resources.displayMetrics.scaledDensity+ 0.5f

    textSize = sp

    if (titleStyle.fontUnderline!!)
    {
        paintFlags = Paint.UNDERLINE_TEXT_FLAG
    }
}

/**
 * Question title style
 */
fun TextView.questionTitleStyle(titleStyle: QuestionTitleStyle?){
    setTextColor(Color.parseColor("#" + titleStyle?.fontColor))
    typeface = getFontTypeface(titleStyle?.fontBold!!, titleStyle.fontItalic!!,
        titleStyle.fontFamily!!)
    val sp: Float =
        titleStyle.
        fontSize?.
        replace("px","")?.toFloat() ?: 12f  / resources.displayMetrics.scaledDensity+ 0.5f

    textSize = sp

    if (titleStyle.fontUnderline!!)
    {
        paintFlags = Paint.UNDERLINE_TEXT_FLAG
    }
}

/**
 * subTitleStyle
 */
fun TextView.subTitleStyle(subtitleStyle: SubTitleStyle?){
    setTextColor(Color.parseColor("#" + subtitleStyle?.fontColor))
    typeface = getFontTypeface(subtitleStyle?.fontBold!!,
        subtitleStyle.fontItalic!!, subtitleStyle.fontFamily!!)
    val sp: Float =
        subtitleStyle.
        fontSize?.
        replace("px","")?.toFloat() ?: 12f  / resources.displayMetrics.scaledDensity+ 0.5f

    textSize = sp

    if (subtitleStyle.fontUnderline!!)
    {
        paintFlags = Paint.UNDERLINE_TEXT_FLAG
    }
}

/**
 * multiChoice style
 */
fun CheckBox.multiChoiceStyle(questionChoicesStyle: QuestionChoicesStyle)
{
    setTextColor(Color.parseColor("#" + questionChoicesStyle.fontColor))
    typeface = getFontTypeface(
        questionChoicesStyle.fontBold!!, questionChoicesStyle.fontItalic!!,
        questionChoicesStyle.fontFamily!!)
    val sp: Float =
        questionChoicesStyle.
        fontSize?.
        replace("px","")?.toFloat() ?: 12f  / resources.displayMetrics.scaledDensity+ 0.5f

    textSize = sp
    if (questionChoicesStyle.fontUnderline!!)
    {
        paintFlags = Paint.UNDERLINE_TEXT_FLAG
    }
}

fun RadioButton.singleChoiceStyle(questionChoicesStyle: QuestionChoicesStyle)
{
    setTextColor(Color.parseColor("#" + questionChoicesStyle.fontColor))
    typeface = getFontTypeface(
        questionChoicesStyle.fontBold!!, questionChoicesStyle.fontItalic!!,
        questionChoicesStyle.fontFamily!!)
    val sp: Float =
        questionChoicesStyle.
        fontSize?.
        replace("px","")?.toFloat() ?: 12f  / resources.displayMetrics.scaledDensity+ 0.5f

    textSize = sp
    if (questionChoicesStyle.fontUnderline!!)
    {
        paintFlags = Paint.UNDERLINE_TEXT_FLAG
    }
}

/**
 * start screen message
 */
fun TextView.startScreenMsgStyle(screenMsg: StartScreenMsgStyle?){
    setTextColor(Color.parseColor("#" + screenMsg?.fontColor))
    typeface = getFontTypeface(screenMsg?.fontBold!!, screenMsg.fontItalic!!,
        screenMsg.fontFamily!!)
    val sp: Float =
        screenMsg.
        fontSize?.
        replace("px","")?.toFloat() ?: 12f  / resources.displayMetrics.scaledDensity+ 0.5f

    textSize = sp

    if (screenMsg.fontUnderline!!)
    {
        paintFlags = Paint.UNDERLINE_TEXT_FLAG
    }
}

/**
 * end screen message style
 */
fun TextView.endScreenMsgStyle(screenMsg: EndScreenMsgStyle?){
    setTextColor(Color.parseColor("#" + screenMsg?.fontColor))
    typeface = getFontTypeface(screenMsg?.fontBold!!, screenMsg.fontItalic!!,
        screenMsg.fontFamily!!)
    val sp: Float =
        screenMsg.
        fontSize?.
        replace("px","")?.toFloat() ?: 12f  / resources.displayMetrics.scaledDensity+ 0.5f

    textSize = sp

    if (screenMsg.fontUnderline!!)
    {
        paintFlags = Paint.UNDERLINE_TEXT_FLAG
    }
}

/**
 * input answer style
 */
fun TextView.inputAnswerStyle(inputAnswerStyle: InputAnswerStyle?){
    setTextColor(Color.parseColor("#" + inputAnswerStyle?.fontColor))
    typeface = getFontTypeface(inputAnswerStyle?.fontBold!!, inputAnswerStyle.fontItalic!!,
        inputAnswerStyle.fontFamily!!)
    val sp: Float =
        inputAnswerStyle.
        fontSize?.
        replace("px","")?.toFloat() ?: 12f  / resources.displayMetrics.scaledDensity+ 0.5f

    textSize = sp

    if (inputAnswerStyle.fontUnderline!!)
    {
        paintFlags = Paint.UNDERLINE_TEXT_FLAG
    }
}

/**
 * Submit button style
 */
fun MaterialButton.submitButtonStyle(submitButton: SubmitButton?){
    val style = getFontTypeface(submitButton!!.fontBold!!,
        submitButton.fontItalic!!, submitButton.fontFamily!!)
    setBackgroundColor(Color.parseColor("#"+ submitButton.backgroundColor))
    setTextColor(Color.parseColor("#"+ submitButton.fontColor))
    typeface = style
    val sp: Float =
        submitButton.
        fontSize?.
        replace("px","")?.toFloat() ?: 12f  / resources.displayMetrics.scaledDensity+ 0.5f

    textSize = sp

    if (submitButton.fontUnderline!!)
    {
        paintFlags = Paint.UNDERLINE_TEXT_FLAG
    }
}

fun getFontTypeface(bold:Boolean,italic:Boolean,fontFamily: String): Typeface
{
    return if (bold && italic)
    {
        Typeface.create(fontFamily, Typeface.BOLD_ITALIC)
    }
    else if (bold && !italic)
    {
        Typeface.create(fontFamily, Typeface.BOLD)
    }
    else if (!bold && italic)
    {
        Typeface.create(fontFamily, Typeface.ITALIC)
    }
    else{
        Typeface.create(fontFamily, Typeface.NORMAL)
    }
}