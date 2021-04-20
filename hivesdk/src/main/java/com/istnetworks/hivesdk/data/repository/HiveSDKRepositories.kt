package com.istnetworks.hivesdk.data.repository

import com.istnetworks.hivesdk.*
import com.istnetworks.hivesdk.data.local.CacheInMemory
import com.istnetworks.hivesdk.data.models.*
import com.istnetworks.hivesdk.data.models.Resource.Companion.success
import com.istnetworks.hivesdk.data.network.ApiService
import com.istnetworks.hivesdk.data.network.apiService
import com.istnetworks.hivesdk.data.utils.Constants.Companion.PASSWORD
import com.istnetworks.hivesdk.data.utils.Constants.Companion.REFRESH_TOKEN
import com.istnetworks.hivesdk.data.utils.Constants.Companion.checkTokenTime
import com.istnetworks.hivesdk.data.utils.Preferences
import com.istnetworks.hivesdk.data.utils.Preferences.Companion.updateTokenResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

val hiveRepository: HiveSDKRepository by lazy { HiveSDKRepositoryImpl() }

interface HiveSDKRepository {
    suspend fun getToken(
        userName: String,
        grantType: String,
        password: String
    ): Response<TokenResponse>

    suspend fun getRelevantWebSurvey(body: RelevantWebSurveyBody): Response<RelevantWebSurveyResponse>
    suspend fun saveWebSurvey(body: SaveWebSurveyBody?): Resource<RelevantWebSurveyResponse>
    suspend fun getRelevantWebSurveyResource(
        userName: String,
        password: String,
        surveyBody: RelevantWebSurveyBody
    ): Resource<RelevantWebSurveyResponse>
}

class HiveSDKRepositoryImpl(
    private val server: ApiService = apiService
) : HiveSDKRepository {
    override suspend fun getToken(
        userName: String,
        grantType: String,
        password: String
    ): Response<TokenResponse> {
        return server.getToken(userName, grantType, password)
    }

    override suspend fun getRelevantWebSurvey(body: RelevantWebSurveyBody): Response<RelevantWebSurveyResponse> {
        return server.getRelevantWebSurvey(body)
    }

    override suspend fun saveWebSurvey(body: SaveWebSurveyBody?): Resource<RelevantWebSurveyResponse> {
        return try {
            val resp = server.saveWebSurvey(body)
            return if (resp.isSuccess == true) {
                Resource.success(resp)
            } else
                Resource.error(resp.message ?: "", null)
        } catch (e: Exception) {
            Resource.error(e.message ?: e.localizedMessage, null)
        }
    }


    suspend fun getTokenResource(
        userName: String,
        grantType: String,
        password: String
    ): Resource<TokenResponse> = withContext(Dispatchers.IO) {
        try {
            val remoteTasks = getToken(userName, grantType, password)
            if (remoteTasks.body() != null) return@withContext success(remoteTasks.body()) else {
                error("token error")
            }
        } catch (e: Exception) {
            error("General token error")
        }
    }


    override suspend fun getRelevantWebSurveyResource(
        userName: String,
        password: String,
        surveyBody: RelevantWebSurveyBody
    ): Resource<RelevantWebSurveyResponse> {
        try {
            var grantType: String = PASSWORD

            if (Preferences.getTokenResponse().expiresIn != 0 && !checkTokenTime(Preferences.getTokenResponse().expiresIn))
                grantType = REFRESH_TOKEN
            val tokenBody = getTokenResource(userName, grantType, password)
            updateTokenResponse(tokenResponse = tokenBody.data)

            return if (tokenBody.status == Status.SUCCESS) {
                try {
                    val survey = getRelevantWebSurvey(surveyBody)
                    survey.body()?.let { CacheInMemory.saveSurveyResponse(survey = it) }
                    success(survey.body())
                } catch (e: Exception) {
                    Resource.error("survey error", null)

                }
            } else {
                Resource.error("token error", null)
            }
        } catch (e: Exception) {
            return Resource.error("token error", null)
        }

    }


}