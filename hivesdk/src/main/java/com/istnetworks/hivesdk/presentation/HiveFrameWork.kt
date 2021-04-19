package com.istnetworks.hivesdk.presentation

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.istnetworks.hivesdk.data.models.RelevantWebSurveyResponse
import com.istnetworks.hivesdk.presentation.hivecard.HiveCardDialogFragment

class HiveFrameWork private constructor(private val builder: Builder) {

    private val password: String?
    private val userName: String?

    init {
        this.password = builder.password
        this.userName = builder.userName
    }

    data class Builder(val context: FragmentActivity) {
        var password: String? = null
            private set
        var userName: String? = null
            private set

        fun setPassword(password: String?): Builder {
            if (!password.isNullOrEmpty() && password.isNotBlank())
                this.password = password
            else throw  IllegalStateException("Password can not be empty!")
            return this
        }

        fun setUserName(userName: String?): Builder {
            if (!userName.isNullOrEmpty() && userName.isNotBlank())
                this.userName = userName
            else throw  IllegalStateException("User name can not be empty!")
            return this
        }


        fun build(): HiveFrameWork {
            val viewModel = ViewModelProviders.of(context).get(HiveSDKViewModel::class.java)
            userName?.let { password?.let { pass -> viewModel.getSurvey(it, pass) } }
            viewModel.getSurveyResponse.observe(context, Observer { it ->
                showCardDialog(it)
            })
            return HiveFrameWork(this)
        }

        private fun showCardDialog(data: RelevantWebSurveyResponse) {
            val hiveCardDialogFragment = HiveCardDialogFragment(context)
            hiveCardDialogFragment.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            hiveCardDialogFragment.show()
            hiveCardDialogFragment.setData(data)
        }
    }

}