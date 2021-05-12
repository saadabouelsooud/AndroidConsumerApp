package com.istnetworks.hivesdk.presentation.interfaces

import com.istnetworks.hivesdk.data.utils.QuestionType

interface ValidationErrorInterface {
    fun showNotValidError(questionType: Int?)
}