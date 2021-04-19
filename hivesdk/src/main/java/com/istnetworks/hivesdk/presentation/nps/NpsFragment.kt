package com.istnetworks.hivesdk.presentation.nps

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.daasuu.ahp.AnimateHorizontalProgressBar
import com.istnetworks.hivesdk.R
import com.istnetworks.hivesdk.data.local.CacheInMemory
import com.istnetworks.hivesdk.data.utils.QuestionType
import com.istnetworks.hivesdk.presentation.emojis.SmileyRatingBar


class NpsFragment : Fragment() {
    private lateinit var viewOfLayout: View
    private lateinit var tvSurveyTitle: TextView
    private lateinit var tvQuestionTitle: TextView
    private lateinit var ivPrevQuestion: ImageView
    private lateinit var ivNextQuestion: ImageView
    private lateinit var recyclerView: RecyclerView
    private lateinit var bar: AnimateHorizontalProgressBar
    private var isRequired: Boolean = false
    private var npsValue: Int = -1
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
       // if (viewOfLayout==null)
        viewOfLayout = inflater.inflate(R.layout.fragment_nps, container, false)
        tvSurveyTitle = viewOfLayout.findViewById(R.id.tv_survey_title)
        tvQuestionTitle = viewOfLayout.findViewById(R.id.tv_question_title)
        bar = viewOfLayout.findViewById(R.id.animate_progress_bar)
        ivNextQuestion = viewOfLayout.findViewById(R.id.iv_next_question)
        ivPrevQuestion = viewOfLayout.findViewById(R.id.iv_prev_question)
         recyclerView = viewOfLayout.findViewById(R.id.nps_recycler_view)
        setNpsList()
        observeSurvey()
        ivPrevQuestion.setOnClickListener {
            val navController =  view?.let { Navigation.findNavController(it) }
            if (navController?.currentDestination?.id == R.id.npsFragment)
                navController.popBackStack()
        }
        ivNextQuestion.setOnClickListener {
           validateNextButton()
        }
        return viewOfLayout
    }


    private fun observeSurvey() {
        val surveyResponse = CacheInMemory.getSurveyResponse()
        if (surveyResponse.survey?.surveyOptions?.hasProgressBar == true)
            bar.visibility = View.VISIBLE
        tvSurveyTitle.text = surveyResponse.survey?.title
        for (i in surveyResponse.survey?.questions?.indices!!) {
            if (surveyResponse.survey?.questions?.get(i)?.questionType == QuestionType.NPS.value) {
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
            if (npsValue >= 0) {

            } else Toast.makeText(activity, getString(R.string.required), Toast.LENGTH_LONG).show()
        }

    }



    private fun setNpsList() {
        val nps: ArrayList<NpsModel> = ArrayList()
        nps.add(NpsModel(npsBackgroundColor = "#e43e3d", npsSelectedHeight = 70, npsSelectedWidth = 70, npsUnselectedHeight = 60, npsUnselectedWidth = 60, npsText = "0"))
        nps.add(NpsModel(npsBackgroundColor = "#e43e3d", npsSelectedHeight = 70, npsSelectedWidth = 70, npsUnselectedHeight = 60, npsUnselectedWidth = 60, npsText = "1"))
        nps.add(NpsModel(npsBackgroundColor = "#ea484d", npsSelectedHeight = 70, npsSelectedWidth = 70, npsUnselectedHeight = 60, npsUnselectedWidth = 60, npsText = "2"))
        nps.add(NpsModel(npsBackgroundColor = "#ec654e", npsSelectedHeight = 70, npsSelectedWidth = 70, npsUnselectedHeight = 60, npsUnselectedWidth = 60, npsText = "3"))
        nps.add(NpsModel(npsBackgroundColor = "#f3a84c", npsSelectedHeight = 70, npsSelectedWidth = 70, npsUnselectedHeight = 60, npsUnselectedWidth = 60, npsText = "4"))
        nps.add(NpsModel(npsBackgroundColor = "#f8c43e", npsSelectedHeight = 70, npsSelectedWidth = 70, npsUnselectedHeight = 60, npsUnselectedWidth = 60, npsText = "5"))
        nps.add(NpsModel(npsBackgroundColor = "#e1c63b", npsSelectedHeight = 70, npsSelectedWidth = 70, npsUnselectedHeight = 60, npsUnselectedWidth = 60, npsText = "6"))
        nps.add(NpsModel(npsBackgroundColor = "#e1c63b", npsSelectedHeight = 70, npsSelectedWidth = 70, npsUnselectedHeight = 60, npsUnselectedWidth = 60, npsText = "7"))
        nps.add(NpsModel(npsBackgroundColor = "#9fce35", npsSelectedHeight = 70, npsSelectedWidth = 70, npsUnselectedHeight = 60, npsUnselectedWidth = 60, npsText = "8"))
        nps.add(NpsModel(npsBackgroundColor = "#7fcd31", npsSelectedHeight = 70, npsSelectedWidth = 70, npsUnselectedHeight = 60, npsUnselectedWidth = 60, npsText = "9"))
        nps.add(NpsModel(npsBackgroundColor = "#5aaf2b", npsSelectedHeight = 70, npsSelectedWidth = 70, npsUnselectedHeight = 60, npsUnselectedWidth = 60, npsText = "10"))

        recyclerView.layoutManager = GridLayoutManager(context, 11)
        val adapter = NpsAdapter(nps)
        recyclerView.adapter = adapter
    }
}