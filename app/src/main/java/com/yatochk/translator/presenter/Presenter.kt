package com.yatochk.translator.presenter

import com.yatochk.translator.R
import com.yatochk.translator.model.Model
import com.yatochk.translator.model.ModelContract
import com.yatochk.translator.model.database.DatabaseTranslate
import com.yatochk.translator.model.translate.IMPOSSIBLY_ERROR
import com.yatochk.translator.model.translate.LENGTH_ERROR
import com.yatochk.translator.view.ViewContract

class Presenter(val model: Model, val view: ViewContract) : PresenterContract {

    private var isTranslateViewOpened = false

    private val onModelTaskListener = object : ModelContract.OnModelTaskListener {

        override fun onGetSavedTranslate(arrayDatabaseTranslates: ArrayList<DatabaseTranslate>) {
            view.updateTranslateListAdapter(arrayDatabaseTranslates)
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
            view.updateSpinnerAdapter(languageList)
        }

        override fun onGetLanguageListError(errorCode: Int) {
            view.showToast("Failed to get language list")
        }
    }

    init {
        model.context = view.context
        model.onModelTaskListener = onModelTaskListener
        model.languageList(R.string.locale_cod.toString())
        model.savedTranslate()
    }

    override fun translateClick() {
        if (!isTranslateViewOpened) {
            view.openTranslateView()
            isTranslateViewOpened = true
        }
        model.translate(view.translateText, view.fromLanguage, view.toLanguage)
    }

    override fun backPressed(): Boolean {
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
}
