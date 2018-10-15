package com.yatochk.translator

import com.yatochk.translator.model.Model

class Presenter(val model: Model) {

    lateinit var view: TranslateContractView

    fun attachView(view: TranslateContractView) {
        this.view = view
    }

}
