package com.yatochk.translator.presenter

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import com.yatochk.translator.R
import com.yatochk.translator.R.string.*
import com.yatochk.translator.model.Model
import com.yatochk.translator.model.ModelContract
import com.yatochk.translator.model.database.DatabaseTranslate
import com.yatochk.translator.model.translate.CONNECTION_ERROR
import com.yatochk.translator.model.translate.IMPOSSIBLY_ERROR
import com.yatochk.translator.model.translate.LENGTH_ERROR
import com.yatochk.translator.model.translate.VOID_TASK_ERROR
import com.yatochk.translator.view.ViewContract

class Presenter(val model: Model, val view: ViewContract) : PresenterContract {
    private val onModelTaskListener = object : ModelContract.OnModelTaskListener {
        override fun onGetSavedTranslate(arrayDatabaseTranslates: ArrayList<DatabaseTranslate>) {
            view.updateTranslateListAdapter(arrayDatabaseTranslates)
        }

        override fun onAddTranslate(translate: DatabaseTranslate) {
            view.addTranslate(translate)
        }

        override fun onRemoveTranslate(rowId: String) {
            view.removeTranslate(rowId)
        }

        override fun onTranslateComplete(translatedText: String) {
            view.showTranslatedText(translatedText)
            view.clearInputText()
        }

        override fun onTranslateError(errorCode: Int) {
            val message = view.context.getString(
                    when (errorCode) {
                        LENGTH_ERROR -> length_error
                        IMPOSSIBLY_ERROR -> impossible_translate
                        CONNECTION_ERROR -> connection_error
                        VOID_TASK_ERROR -> void_translate
                        else -> unknow_error
                    })

            view.showToast(message)
        }

        override fun onGetLanguageListComplete(languageList: LinkedHashMap<String, String>) {
            view.updateSpinnerAdapter(languageList)
        }

        override fun onGetLanguageListError(errorCode: Int) {
            var message = view.context.getString(R.string.langs_list_error)
            if (errorCode == CONNECTION_ERROR)
                message = view.context.getString(R.string.connection_error)

            view.showToast(message)
        }
    }

    init {
        model.context = view.context
        model.onModelTaskListener = onModelTaskListener
        model.languageList(view.context.getString(R.string.locale_cod))
        model.savedTranslate()
    }

    override fun translateClick() {
        model.translate(view.translateText, view.fromLanguage, view.toLanguage)
    }

    override fun deleteClick(itemId: String) {
        model.deleteTranslate(itemId)
    }

    override fun longClickTranslatedText() {
        val clipBoard = view.context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("text", view.translatedText)
        clipBoard.primaryClip = clip
        view.showToast(view.context.getString(R.string.copy_text))
    }
}
