package com.istnetworks.hivesdk.presentation.viewmodel.factory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.istnetworks.hivesdk.data.repository.HiveSDKRepository
import com.istnetworks.hivesdk.presentation.viewmodel.HiveSDKViewModel

class HiveSDKViewModelFactory(
    private val repo: HiveSDKRepository
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HiveSDKViewModel::class.java)) {
            return HiveSDKViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}