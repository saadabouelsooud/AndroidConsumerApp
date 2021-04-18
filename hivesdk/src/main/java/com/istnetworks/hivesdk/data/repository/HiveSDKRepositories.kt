package com.istnetworks.hivesdk.data.repository

import com.istnetworks.hivesdk.*
import com.istnetworks.hivesdk.data.local.CacheInMemory
import com.istnetworks.hivesdk.data.models.Resource
import com.istnetworks.hivesdk.data.models.Resource.Companion.success
import com.istnetworks.hivesdk.data.models.Status
import com.istnetworks.hivesdk.data.network.ApiService
import com.istnetworks.hivesdk.data.network.apiService
import com.istnetworks.hivesdk.data.utils.Constants.Companion.PASSWORD
import com.istnetworks.hivesdk.data.utils.Constants.Companion.REFRESH_TOKEN
import com.istnetworks.hivesdk.data.utils.Constants.Companion.checkTokenTime
import com.istnetworks.hivesdk.data.utils.Preferences
import com.istnetworks.hivesdk.data.utils.Preferences.Companion.updateTokenResponse
import com.istnetworks.hivesdk.presentation.AppContainer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

val hiveRepository: HiveSDKRepository by lazy { HiveSDKRepositoryImpl() }
interface HiveSDKRepository {
    suspend fun getToken( userName:String, grantType:String, password:String): Response<TokenResponse>
    suspend fun getRelevantWebSurvey( body: RelevantWebSurveyBody): Response<RelevantWebSurveyResponse>
    suspend fun saveWebSurvey( body: SaveWebSurveyBody): Response<RelevantWebSurveyResponse>
}
class HiveSDKRepositoryImpl (
    private val server: ApiService =apiService
) : HiveSDKRepository {
    override suspend  fun getToken( userName:String, grantType:String, password:String): Response<TokenResponse> {
        return server.getToken(userName, grantType, password)
    }

    override  suspend fun getRelevantWebSurvey(body: RelevantWebSurveyBody): Response<RelevantWebSurveyResponse> {
       return server.getRelevantWebSurvey(body)
    }

    override suspend fun saveWebSurvey(body: SaveWebSurveyBody): Response<RelevantWebSurveyResponse> {
        return server.saveWebSurvey(body)
    }


     suspend fun getTokenResource(userName:String, grantType:String, password:String): Resource<TokenResponse> = withContext(Dispatchers.IO) {
        try {
            val remoteTasks = getToken(userName, grantType, password)
            if (remoteTasks.body() != null) return@withContext success(remoteTasks.body()) else {
                 error("token error")
            }
        } catch (e: Exception) {
            error("General token error")
        }
    }


    suspend fun getRelevantWebSurveyResource(userName:String, password:String, surveyBody: RelevantWebSurveyBody):
            Resource<RelevantWebSurveyResponse> = withContext(Dispatchers.IO) {
       try {
           var grantType:String = PASSWORD
           if(!checkTokenTime(Preferences.getTokenResponse().expiresIn))
               grantType = REFRESH_TOKEN
           val tokenBody = getTokenResource(userName,grantType ,password)
           updateTokenResponse(tokenResponse = tokenBody.data)

           if( tokenBody.status== Status.SUCCESS){
               try {
                   val survey = getRelevantWebSurvey(surveyBody)
                   survey.body()?.let { CacheInMemory.saveSurveyResponse(survey = it) }
                   return@withContext success(survey.body())
               }catch (e:Exception){
                   error("survey error")

               }
           }else{
                error("token error")
           }
           }catch (e:Exception){
            error("token error")
       }

    }



}