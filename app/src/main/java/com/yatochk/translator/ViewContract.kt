package com.yatochk.translator

interface ViewContract {
    var translateText: String
    var fromLanguage: String
    var toLanguage: String
    fun openTranslateView()
    fun hideTranslateView()
    fun updateSpinnerAdapter(languages: LinkedHashMap<String, String>)
    fun showTranslatedText(text: String)
    fun showToast(message: String)
}