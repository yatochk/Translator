package com.yatochk.translator.presenter

import com.yatochk.translator.ViewContract
import com.yatochk.translator.model.Model

class Presenter(val model: Model) : PresenterContract {

    lateinit var view: ViewContract
    private var isTranslateViewOpened = false

    init {
        model.presenter = this
    }

    override fun onTranslateComplete(translatedText: String) {
        view.showTranslatedText(translatedText)
    }

    override fun onLangDefinitionComplete(language: String) {
    }

    override fun onGetLanguageListComplete(languageList: LinkedHashMap<String, String>) {
    }

    fun translate() {
        if (!isTranslateViewOpened){
            view.openTranslateView()
            isTranslateViewOpened = true
        }
        model.translate(view.translateText, view.fromLanguage, view.toLanguage)
    }

    fun backPressed() : Boolean{
        if (isTranslateViewOpened){
            view.hideTranslateView()
            isTranslateViewOpened = false
            return false
        }

        return true
    }

    fun focusChangeInputText(hasFocused: Boolean){
        if (hasFocused && !isTranslateViewOpened)
            view.openTranslateView()
    }
}
