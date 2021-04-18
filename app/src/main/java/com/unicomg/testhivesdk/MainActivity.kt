package com.unicomg.testhivesdk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import com.istnetworks.hivesdk.presentation.HiveFrameWork
import com.istnetworks.hivesdk.presentation.HiveSDKViewModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener {
            HiveFrameWork.Builder(this).setPassword("InApp2021").setUserName("InAppUser").build()
        }

    }
}