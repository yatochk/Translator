package com.yatochk.translator.presenter

interface PresenterContract {
    fun onTranslateComplete(translatedText: String)
    fun onLangDefinitionComplete(language: String)
    fun onGetLanguageListComplete(languageList: LinkedHashMap<String, String>)
}