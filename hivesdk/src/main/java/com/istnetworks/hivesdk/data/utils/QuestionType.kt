package com.istnetworks.hivesdk.data.utils

enum class QuestionType(val value: Int) {

    MultipleChoiceQuestion(0),
    ListQuestion(4),
    DateQuestion(5),
    SlideQuestion(6),
    StarQuestion(7),
    NPS(8),
    TextInput(9),
    NumberInput(10),
    EmailInput(11),
    PhoneNumberInput(12),
    PostalCodeInput(13),
    URLInput(14),
    SingleChoice(15),
    Emoji(16),
    ImageMCQ(17),
    ImageSingleChoice(18),
    CSAT(19)
}