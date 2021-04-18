package com.istnetworks.hivesdk.data.utils

import java.util.*


class Constants {
    companion object {
        const val BASE_URL = ""
        const val GET_TOKEN = "Authentication/GetToken"
        const val GET_RELEVANT_WEB_SURVEY = "Survey/GetRelevantWebSurvey"
        const val SAVE_WEB_SURVEY = "CustomerSurvey/SaveWebSurveyResponse"

        const val TOKEN_PREFERENCE = "TOKEN_PREFERENCE"
        const val TOKEN_OBJECT = "TOKEN_OBJECT"


        const val PASSWORD = "password"
        const val REFRESH_TOKEN = "refresh_token"


        fun checkTokenTime(seconds: Int?): Boolean {
            val calendar = Calendar.getInstance() // gets a calendar using the default time zone and locale.
            calendar.add(Calendar.SECOND, seconds!!)
            val currentTime = Calendar.getInstance().time
            if(calendar.time.compareTo(currentTime) > 0) {
               return true
            } else if(calendar.time.compareTo(currentTime) < 0) {
                return false
            } else  {
                return false
            }

        }

    }


}