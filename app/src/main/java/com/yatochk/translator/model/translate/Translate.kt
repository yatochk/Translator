package com.yatochk.translator.model.translate

interface Translate {
    interface Contract {
        fun translate(text: String, fromLang: String, toLang: String)
        fun languageList(uiLanguage: String)
    }

    interface OnTranslateTaskListener {
        fun onTranslateComplete(translatedText: String, answerCode: Int)
        fun onGetLanguageListComplete(languageList: LinkedHashMap<String, String>, answerCode: Int)
    }
}

interface ServerTaskListener {
    fun onTranslated(translateResult: String)
    fun onGetLanguageList(languagesResult: String)
}