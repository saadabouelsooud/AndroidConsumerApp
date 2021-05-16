package com.istnetworks.hivesdk.presentation.viewmodel

import com.istnetworks.hivesdk.data.models.SkipLogic
import com.istnetworks.hivesdk.data.models.response.Question

class SkipLogicHandler (val skipLogic: List<SkipLogic>?,questionList: MutableList<Question>?){


     fun hasSkipLogic(qId: String?): Boolean {
        return skipLogic?.any { it.questionGUID.equals(qId) } ?: false
    }

    fun freeInputAndDateSkip(question: Question?): String? {
        return skipLogic?.findLast {
            it.questionGUID.equals(question?.surveyQuestionGUID)
        }?.skipToQuestionGUID

    }

    fun singleChoiceQuestionsSkip(question: Question?, numberResponse: Int?): String? {
        return try {
            skipLogic?.find {
                it.questionGUID.equals(question?.surveyQuestionGUID) &&
                        numberResponse in it.minValue!!..it.maxValue!!
            }?.skipToQuestionGUID
        } catch (e:Exception) {
            e.printStackTrace()
           "00000000-0000-0000-0000-000000000000"
        }


    }
}