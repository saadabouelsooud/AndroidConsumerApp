package com.istnetworks.hivesdk.presentation

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ProgressBar
import androidx.activity.viewModels
import androidx.annotation.Keep
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.istnetworks.hivesdk.R
import com.istnetworks.hivesdk.data.repository.HiveSDKRepositoryImpl
import com.istnetworks.hivesdk.data.utils.HiveSDKType
import com.istnetworks.hivesdk.data.utils.extensions.shouldShow
import com.istnetworks.hivesdk.presentation.BottomSheet.BottomSheetFragment
import com.istnetworks.hivesdk.presentation.fullScreen.VerticalMainFragment
import com.istnetworks.hivesdk.presentation.mainfragment.MainFragment
import com.istnetworks.hivesdk.presentation.viewmodel.HiveSDKViewModel
import com.istnetworks.hivesdk.presentation.viewmodel.factory.HiveSDKViewModelFactory

private const val HVE_ARG_USER_NAME: String = "HVE_ARG_USER_NAME"
private const val HVE_ARG_PASSWORD: String = "HVE_ARG_PASSWORD"

class SurveyActivity : AppCompatActivity() {
    private val viewModel: HiveSDKViewModel by viewModels {
        HiveSDKViewModelFactory(
            HiveSDKRepositoryImpl()
        )
    }
    private val userName: String? by lazy { intent.getStringExtra(HVE_ARG_USER_NAME) }
    private val password: String? by lazy { intent.getStringExtra(HVE_ARG_PASSWORD) }

    @Keep
    companion object {
        @JvmStatic
        fun startSurvey(
            @NonNull context: Activity,
            @NonNull userName: String,
            @NonNull password: String
        ) {
            val starter = Intent(context, SurveyActivity::class.java)
                .putExtra(HVE_ARG_USER_NAME, userName)
                .putExtra(HVE_ARG_PASSWORD, password)
            context.startActivity(starter)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_survey)
        //todo validate params
        viewModel.getSurvey(userName!!, password!!)
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.getSurveyResponseLD.observe(this, {
            when (it?.survey?.surveyOptions?.displayMode) {
                HiveSDKType.FULLSCREEN.value -> showCardDialog(VerticalMainFragment())
                HiveSDKType.BOTTOM_CARD.value -> showCardDialog(BottomSheetFragment())
                else -> showCardDialog(MainFragment())
            }

        })
        viewModel.isLoading.observe(this,{
            findViewById<ProgressBar>(R.id.hve_progress_bar).shouldShow(it)
        })

    }

    private fun showCardDialog(f: Fragment) {
        val beginTransaction = supportFragmentManager.beginTransaction()
        beginTransaction.add(android.R.id.content, f, "").commit()
    }
}