package com.yatochk.translator

interface PresenterContractView {
    fun openTranslateView()
    fun hideTranslateView()
    fun showTranslatedText(text: String)
}