package com.yatochk.translator.model.translate

object Translate {
    interface Contract {
        fun translate(text: String, fromLang: String, toLang: String)
        fun languageList(uiLanguage: String)
    }

    interface OnTranslateTaskListener {
        fun onTranslateComplete(answerCode: Int, text: String, translatedText: String, fromLang: String, toLang: String)
        fun onGetLanguageListComplete(languageList: LinkedHashMap<String, String>, answerCode: Int)
    }
}