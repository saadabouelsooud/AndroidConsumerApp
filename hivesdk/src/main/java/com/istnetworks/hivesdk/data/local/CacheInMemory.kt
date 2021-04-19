package com.istnetworks.hivesdk.data.local

import com.istnetworks.hivesdk.data.models.RelevantWebSurveyResponse


class CacheInMemory {

    companion object Singleton {

        private var hashMap: HashMap<String, Any> = HashMap<String, Any>()

        fun saveSurveyResponse(survey: RelevantWebSurveyResponse) {
            hashMap["SURVEY"] = survey
        }


        fun getSurveyResponse(): RelevantWebSurveyResponse {
            return hashMap["SURVEY"] as RelevantWebSurveyResponse
        }



        fun clearCached() {
            hashMap.remove("SURVEY")
        }


    }

}