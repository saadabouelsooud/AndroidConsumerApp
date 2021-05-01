package com.unicomg.testhivesdk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.istnetworks.hivesdk.presentation.SurveyActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener {
         SurveyActivity.startSurvey(this,"InAppUser","InApp2021")
        }

    }
}