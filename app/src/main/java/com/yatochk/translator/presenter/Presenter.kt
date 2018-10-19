package com.yatochk.translator.presenter

import com.yatochk.translator.ViewContract
import com.yatochk.translator.model.Model
import com.yatochk.translator.model.ModelContract
import com.yatochk.translator.model.translate.IMPOSSIBLY_ERROR
import com.yatochk.translator.model.translate.LENGTH_ERROR

class Presenter(val model: Model) : PresenterContract, ModelContract.OnModelTaskListener {

    lateinit var view: ViewContract
    private var isTranslateViewOpened = false

    init {
        model.presenter = this
    }

    override fun translateClick() {
        if (!isTranslateViewOpened) {
            view.openTranslateView()
            isTranslateViewOpened = true
        }
        model.translate(view.translateText, view.fromLanguage, view.toLanguage)
    }

    fun backPressed(): Boolean {
        if (isTranslateViewOpened) {
            view.hideTranslateView()
            isTranslateViewOpened = false
            return false
        }

        return true
    }

    fun focusChangeInputText(hasFocused: Boolean) {
        if (hasFocused && !isTranslateViewOpened)
            view.openTranslateView()
    }

    override fun onTranslateComplete(translatedText: String) {
        view.showTranslatedText(translatedText)
    }

    override fun onTranslateError(errorCode: Int) {
        val message = when (errorCode) {
            LENGTH_ERROR -> "Your text is too long"
            IMPOSSIBLY_ERROR -> "Your text cannot be translated"
            else -> "Unknown translation error"
        }

        view.showToast(message)
    }

    override fun onGetLanguageListComplete(languageList: LinkedHashMap<String, String>) {

    }

    override fun onGetLanguageListError(errorCode: Int) {
        view.showToast("Failed to get language list")
    }
}
