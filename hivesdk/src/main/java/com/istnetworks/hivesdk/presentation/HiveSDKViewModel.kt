package com.istnetworks.hivesdk.presentation

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.istnetworks.hivesdk.*
import com.istnetworks.hivesdk.data.network.ApiService
import com.istnetworks.hivesdk.data.repository.HiveSDKRepository
import com.istnetworks.hivesdk.data.repository.HiveSDKRepositoryImpl
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
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
        servayBody.channel="MOBILE_SDK"
        servayBody.customerCIC="MOBILE_SDK"

        val surveyResult = hiveSDKRepository.getRelevantWebSurveyResource(username,password,servayBody)
        getSurveyResponse.value = surveyResult.data


    }



    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }}