package com.yatochk.translator.model

interface ModelContract {
    interface Contract{
        fun translate(text: String, fromLang: String, toLang: String)
        fun languageList(text: String, fromLang: String, toLang: String)
    }

    interface OnModelTaskListener{
        fun onTranslateComplete(translatedText: String)
        fun onTranslateError(errorCode: Int)
        fun onGetLanguageListComplete(languageList: LinkedHashMap<String, String>)
        fun onGetLanguageListError(errorCode: Int)
    }

}