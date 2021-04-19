package com.istnetworks.hivesdk.presentation.hivecard

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.daasuu.ahp.AnimateHorizontalProgressBar
import com.istnetworks.hivesdk.data.models.Question
import com.istnetworks.hivesdk.R
import com.istnetworks.hivesdk.data.models.RelevantWebSurveyResponse
import com.istnetworks.hivesdk.data.utils.QuestionType
import com.istnetworks.hivesdk.presentation.emojis.SmileyRatingBar


private var tvSurveyTitle: TextView? = null
private var tvQuestionTitle: TextView? = null
private var animateProgressBar: AnimateHorizontalProgressBar? = null
private var smileyRatingBar: SmileyRatingBar? = null
private var npsContainer: ConstraintLayout? = null


class HiveCardDialogFragment(context: Context) : Dialog(context) {

    init {
        setCancelable(false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.hive_card)
        tvQuestionTitle = findViewById<TextView>(R.id.tv_question_title)
        tvSurveyTitle = findViewById<TextView>(R.id.tv_survey_title)
        smileyRatingBar = findViewById<SmileyRatingBar>(R.id.smiley_rating)
        animateProgressBar = findViewById<AnimateHorizontalProgressBar>(R.id.animate_progress_bar)
        npsContainer = findViewById<ConstraintLayout>(R.id.nps_container)
        //setNpsList()
    }

    fun setData(data: RelevantWebSurveyResponse) {
//        tvSurveyTitle?.text = (data.survey?.name)
//        tvQuestionTitle?.text = (data.survey?.questions?.get(0)?.title)
//        if (data.survey?.surveyOptions?.hasProgressBar != true)
//            animateProgressBar?.visibility = View.INVISIBLE else animateProgressBar?.visibility = View.VISIBLE
       // data.survey?.questions?.let { showQuestion(it) }
    }

    fun showQuestion(questions: List<Question>) {
        for (i in questions.indices) {
            if (questions[i].questionType == QuestionType.Emoji.value) {
                smileyRatingBar?.visibility = View.VISIBLE
                npsContainer?.visibility = View.GONE

                break
            }
            else if (questions[i].questionType == QuestionType.NPS.value) {
                smileyRatingBar?.visibility = View.VISIBLE
                npsContainer?.visibility = View.GONE
                break
            }

        }
    }

private fun setStartDesination(){
//    val navHostFragment = context.ac.supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
//    val inflater = navHostFragment.navController.navInflater
//    val graph = inflater.inflate(R.navigation.hive_graph)
//
//   // if (isTrue){
//        graph.startDestination = R.id.emojiFragment
////    }else {
////        graph.startDestination = R.id.OtherDetailsFragment
////    }
//
//    val navController = navHostFragment.navController
//    navController.graph = graph
}
    override fun onStart() {
        super.onStart()
        window?.setLayout(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        )
    }


}