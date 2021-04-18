package com.istnetworks.hivesdk.presentation.emojis

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.daasuu.ahp.AnimateHorizontalProgressBar
import com.istnetworks.hivesdk.R
import com.istnetworks.hivesdk.data.local.CacheInMemory
import com.istnetworks.hivesdk.data.utils.QuestionType
import com.istnetworks.hivesdk.presentation.HiveSDKViewModel


class EmojiFragment : Fragment(), RatingSelectListener {

      var viewOfLayout: View? = null
    private lateinit var smileyRating: SmileyRatingBar
    private lateinit var tvSurveyTitle: TextView
    private lateinit var tvQuestionTitle: TextView
    private lateinit var ivPrevQuestion: ImageView
    private lateinit var ivNextQuestion: ImageView
    private var isRequired: Boolean = false
    private var rateValue: Int = -1
    private lateinit var bar: AnimateHorizontalProgressBar
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        if(viewOfLayout==null)
        viewOfLayout = inflater.inflate(R.layout.fragment_emoji, container, false)
        smileyRating = viewOfLayout!!.findViewById(R.id.smiley_rating)
        tvSurveyTitle = viewOfLayout!!.findViewById(R.id.tv_survey_title)
        tvQuestionTitle = viewOfLayout!!.findViewById(R.id.tv_question_title)
        bar = viewOfLayout!!.findViewById(R.id.animate_progress_bar)
        ivNextQuestion = viewOfLayout!!.findViewById(R.id.iv_next_question)
        ivPrevQuestion = viewOfLayout!!.findViewById(R.id.iv_prev_question)
        observeSurvey()
        smileyRating.setRatingSelectListener(this)
        ivNextQuestion.setOnClickListener {
            validateNextButton()
        }
        return viewOfLayout
    }

    private fun observeSurvey() {
        val surveyResponse = CacheInMemory.getSurveyResponse()
        if (surveyResponse.survey?.surveyOptions?.hasProgressBar == true)
            bar.visibility = View.VISIBLE
        tvSurveyTitle.text = surveyResponse.survey?.name
        for (i in surveyResponse.survey?.questions?.indices!!) {
            if (surveyResponse.survey?.questions?.get(i)?.questionType == QuestionType.Emoji.value) {
                tvQuestionTitle.text = surveyResponse.survey.questions[i].title
                isRequired = surveyResponse.survey.questions[i].isRequired!!
                if (i == 0)
                    ivPrevQuestion.visibility = View.GONE
                break

            }


        }
    }

    private fun validateNextButton() {
        if (isRequired) {
            if (rateValue >= 0) {
                moveToNpsQuestion()
            } else Toast.makeText(activity, getString(R.string.required), Toast.LENGTH_LONG).show()
        } else moveToNpsQuestion()
    }

    private fun moveToNpsQuestion() {
        val navController =  view?.let { Navigation.findNavController(it) }
        if (navController?.currentDestination?.id == R.id.emojiFragment)
            navController.navigate(R.id.action_emoji_to_nps)
    }

    override fun ratingSelected(rating: Int) {
        rateValue = rating
    }


}