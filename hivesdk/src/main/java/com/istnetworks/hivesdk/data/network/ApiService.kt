package com.istnetworks.hivesdk.data.network

import com.istnetworks.hivesdk.*
import com.istnetworks.hivesdk.data.utils.Constants
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @FormUrlEncoded
    @POST(Constants.GET_TOKEN)
    suspend fun getToken(@Field("username") userName :String, @Field ("grant_type") grantType :String, @Field ("password") password :String): Response<TokenResponse>

    @POST(Constants.GET_RELEVANT_WEB_SURVEY)
    suspend fun getRelevantWebSurvey(@Body body: RelevantWebSurveyBody): Response<RelevantWebSurveyResponse>

    @POST(Constants.SAVE_WEB_SURVEY)
    suspend fun saveWebSurvey(@Body body: SaveWebSurveyBody): Response<RelevantWebSurveyResponse>


}