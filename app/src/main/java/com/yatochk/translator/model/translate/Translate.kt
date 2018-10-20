package com.yatochk.translator.model.translate

object Translate {
    interface Contract {
        fun translate(text: String, fromLang: String, toLang: String)
        fun languageList(uiLanguage: String)
    }

    interface OnTranslateTaskListener {
        fun onTranslateComplete(translatedText: String, answerCode: Int)
        fun onGetLanguageListComplete(languageList: LinkedHashMap<String, String>, answerCode: Int)
    }
}

object ServerTaskListener {
    interface OnTranslateListener {
        fun onTranslated(translateResult: String)
    }

    interface OnGetLanguageListListener {
        fun onGetLanguageList(languagesResult: String)
    }
}