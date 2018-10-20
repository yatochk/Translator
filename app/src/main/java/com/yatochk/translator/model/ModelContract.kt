package com.yatochk.translator.model

object ModelContract {
    interface Contract {
        fun translate(text: String, fromLang: String, toLang: String)
        fun languageList(uiLanguage: String)
    }

    interface OnModelTaskListener {
        fun onTranslateComplete(translatedText: String)
        fun onTranslateError(errorCode: Int)
        fun onGetLanguageListComplete(languageList: LinkedHashMap<String, String>)
        fun onGetLanguageListError(errorCode: Int)
    }
}