package com.istnetworks.hivesdk.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.istnetworks.hivesdk.data.models.RelevantWebSurveyBody
import com.istnetworks.hivesdk.data.models.RelevantWebSurveyResponse
import com.istnetworks.hivesdk.data.models.TokenResponse
import com.istnetworks.hivesdk.data.repository.HiveSDKRepositoryImpl
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import kotlinx.coroutines.launch

class HiveSDKViewModel : ViewModel(
) {
    val hiveSDKRepository: HiveSDKRepositoryImpl by lazy {
        HiveSDKRepositoryImpl()
    }
    val tokenResponse = PublishSubject.create<TokenResponse>()
    val getSurveyResponse = MutableLiveData<RelevantWebSurveyResponse>()
    val saveSurveyResponse = MutableLiveData<RelevantWebSurveyResponse>()
    val isLoading = MutableLiveData<Boolean>()
    private val compositeDisposable = CompositeDisposable()


    fun getSurvey(username :String , password :String) = viewModelScope.launch {
        val servayBody = RelevantWebSurveyBody()
        servayBody.language="en"
        servayBody.channel="InApp"
        servayBody.customerName="saad"
        servayBody.customerEmail="ss@ss.com"
        servayBody.customerPhone="01234567890"

        val surveyResult = hiveSDKRepository.getRelevantWebSurveyResource(username,password,servayBody)
        getSurveyResponse.value = surveyResult.data


    }



    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }}