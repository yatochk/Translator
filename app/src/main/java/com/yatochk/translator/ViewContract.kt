package com.yatochk.translator

interface ViewContract {
    var translateText: String
    var fromLanguage: String
    var toLanguage: String
    fun openTranslateView()
    fun hideTranslateView()
    fun showTranslatedText(text: String)
    fun showToast(message: String)
}