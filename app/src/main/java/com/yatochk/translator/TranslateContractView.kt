package com.yatochk.translator

interface TranslateContractView {

    fun openTranslateView()
    fun hideTranslateView()
    fun showTranslatedText(text: String)
}