package com.istnetworks.hivesdk.presentation

import androidx.fragment.app.FragmentActivity

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



    }

}