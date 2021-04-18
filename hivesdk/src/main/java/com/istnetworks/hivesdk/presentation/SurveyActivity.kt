package com.istnetworks.hivesdk.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.istnetworks.hivesdk.R
import com.istnetworks.hivesdk.presentation.emojis.RatingSelectListener
import com.istnetworks.hivesdk.presentation.emojis.SmileyRatingBar

class SurveyActivity : AppCompatActivity(), RatingSelectListener {
	private lateinit var viewModel: HiveSDKViewModel
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		val smileyRating=findViewById<SmileyRatingBar>(R.id.smiley_rating)
		smileyRating?.setRatingSelectListener(this)
		Log.v("current_rating", "" + smileyRating?.getRating())
		//viewModel = ViewModelProviders.of(this).get(HiveSDKViewModel::class.java)
		//HiveFrameWork.Builder(this).setPassword("Fdfd").setUserName("ssfs").build()

	}

	override fun ratingSelected(rating: Int) {
		Log.v("rating", "" + rating)
	}
}