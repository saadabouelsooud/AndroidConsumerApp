package com.istnetworks.hivesdk.presentation

import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.istnetworks.hivesdk.data.repository.HiveSDKRepositoryImpl
import com.istnetworks.hivesdk.presentation.mainfragment.MainFragment
import com.istnetworks.hivesdk.presentation.surveyExtension.questionTitleStyle
import com.istnetworks.hivesdk.presentation.viewmodel.HiveSDKViewModel
import com.istnetworks.hivesdk.presentation.viewmodel.factory.HiveSDKViewModelFactory


open class BaseQuestionFragment : Fragment() {
    open val viewModel: HiveSDKViewModel by activityViewModels {
        HiveSDKViewModelFactory(
            HiveSDKRepositoryImpl()
        )
    }

    fun stylingQuestionTitle(tvQuestionTitle: TextView) {
        val theme = viewModel.getSurveyTheme()
        tvQuestionTitle.questionTitleStyle(theme?.questionTitleStyle)
    }

    fun updatePagerHeight(view: View) {
        view.requestLayout()
        (requireParentFragment() as MainFragment).updatePagerHeightForChild(view)
    }
}