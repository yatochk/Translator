package com.yatochk.translator.model

interface ModelContract {
    fun onTranslateComplete(translatedText: String)
    fun onLangDefinitionComplete(language: String)
    fun onGetLanguageListComplete(languageList: LinkedHashMap<String, String>)
}