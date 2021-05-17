package com.istnetworks.hivesdk.presentation.viewmodel

import com.istnetworks.hivesdk.data.models.SkipLogic
import com.istnetworks.hivesdk.data.models.response.Question

class SkipLogicHandler (val skipLogic: List<SkipLogic>?,questionList: MutableList<Question>?){


    fun hasSkipLogic(qId: String?): Boolean {
        return skipLogic?.any { it.questionGUID.equals(qId) } ?: false
    }

    fun getSkipToByQuestionGuid(question: Question?): String? {
        return skipLogic?.findLast {
            it.questionGUID.equals(question?.surveyQuestionGUID)
        }?.skipToQuestionGUID
    }

    fun getSkipToByNumberAndQuestionGuid(question: Question?, numberResponse: Int?): String? {
        return skipLogic?.find {
            it.questionGUID.equals(question?.surveyQuestionGUID) &&
                    numberResponse in it.minValue!!..it.maxValue!!
        }?.skipToQuestionGUID
    }

    fun getSkipToByQuestionGuidAndChoiceGuid(questionGuid: String?, choiceGuid: String?): String? {
        return skipLogic?.find {
            it.questionGUID.equals(questionGuid) && choiceGuid == it.qChoiceGUID
        }?.skipToQuestionGUID
    }
}