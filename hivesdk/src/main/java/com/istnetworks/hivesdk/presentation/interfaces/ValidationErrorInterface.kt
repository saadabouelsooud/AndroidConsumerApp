package com.istnetworks.hivesdk.presentation.interfaces


interface ValidationErrorInterface {
    fun showNotValidError(questionType: Int?)
    fun hideNotValidError(questionType: Int?)
}