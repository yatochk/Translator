package com.yatochk.translator.presenter

import com.yatochk.translator.view.ViewContract

interface PresenterContract {
    fun attachView(view: ViewContract)
    fun translateClick()
    fun deleteClick(itemId: String)
    fun longClickTranslatedText()
}