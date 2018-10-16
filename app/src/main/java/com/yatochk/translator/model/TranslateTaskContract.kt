package com.yatochk.translator.model

interface TranslateTaskContract {
    fun onTranslated(translateResult: String)
    fun onLangDefinition(definitionResult: String)
    fun onGetLanguageList(getLanguagesResult: String)
}