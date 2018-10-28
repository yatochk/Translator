package com.yatochk.translator.presenter

interface PresenterContract {
    fun translateClick()
    fun deleteClick(itemId: String)
    fun longClickTranslatedText()
}