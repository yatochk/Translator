package com.yatochk.translator.model.translate

interface Translate {
    interface TaskContract {
        fun onTranslated(translateResult: String)
        fun onGetLanguageList(languagesResult: String)
    }

    interface OnTranslateTaskListener {
        fun onTranslateComplete(translatedText: String, answerCode: Int)
        fun onGetLanguageListComplete(languageList: LinkedHashMap<String, String>, answerCode: Int)
    }
}