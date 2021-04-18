package com.istnetworks.hivesdk.data.utils

import android.app.Activity
import android.content.Context
import com.google.gson.Gson
import com.istnetworks.hivesdk.TokenResponse
import com.istnetworks.hivesdk.data.utils.Constants.Companion.TOKEN_OBJECT
import com.istnetworks.hivesdk.data.utils.Constants.Companion.TOKEN_PREFERENCE
import com.istnetworks.hivesdk.presentation.AppContainer


class Preferences {

    companion object {
        fun updateTokenResponse(activity: Context? = AppContainer.context, tokenResponse: TokenResponse?) {
            val gson = Gson()
            val json = gson.toJson(tokenResponse)
            val sharedPref =
                activity?.getSharedPreferences(TOKEN_PREFERENCE, Context.MODE_PRIVATE) ?: return
            with(sharedPref.edit()) {
                putString(TOKEN_OBJECT, json)
                apply()
            }
        }

        fun getTokenResponse(activity: Context = AppContainer.context): TokenResponse {
            val gson = Gson()
            val sharedPref = activity?.getSharedPreferences(TOKEN_PREFERENCE, Context.MODE_PRIVATE)
            val result = sharedPref.getString(TOKEN_OBJECT, null)
            if (result != null) {
                val tokenResponse: TokenResponse = gson.fromJson(result, TokenResponse::class.java)
                return tokenResponse
            } else {
                val tokenResponse: TokenResponse = TokenResponse()
                return tokenResponse
            }

        }

    }

}