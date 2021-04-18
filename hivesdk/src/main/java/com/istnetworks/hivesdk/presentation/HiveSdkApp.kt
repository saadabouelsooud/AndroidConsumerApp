package com.istnetworks.hivesdk.presentation

import android.app.Application
import android.content.Context

class HiveSdkApp : Application() {
    lateinit var context:Context
    override fun onCreate() {
        super.onCreate()
        AppContainer.context = applicationContext

    }


}