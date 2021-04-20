package com.istnetworks.hivesdk.data.network

import com.istnetworks.hivesdk.data.models.RelevantWebSurveyBody
import com.istnetworks.hivesdk.data.models.RelevantWebSurveyResponse
import com.istnetworks.hivesdk.data.models.SaveWebSurveyBody
import com.istnetworks.hivesdk.data.models.TokenResponse
import com.istnetworks.hivesdk.data.utils.Constants
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @FormUrlEncoded
    @POST(Constants.GET_TOKEN)
    suspend fun getToken(@Field("username") userName :String, @Field ("grant_type") grantType :String, @Field ("password") password :String): Response<TokenResponse>

    @POST(Constants.GET_RELEVANT_WEB_SURVEY)
    suspend fun getRelevantWebSurvey(@Body body: RelevantWebSurveyBody): Response<RelevantWebSurveyResponse>

    @POST(Constants.SAVE_WEB_SURVEY)
    suspend fun saveWebSurvey(@Body body: SaveWebSurveyBody?): RelevantWebSurveyResponse


}