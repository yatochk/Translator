package com.yatochk.translator

import android.animation.Animator
import android.animation.AnimatorInflater
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.yatochk.translator.model.Model
import com.yatochk.translator.presenter.Presenter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.input_words.view.*
import javax.inject.Inject

class MainActivity : AppCompatActivity(), ViewContract {
    override var translateText: String = ""
    override var fromLanguage: String = "English"
    override var toLanguage: String = "Russian"

    @Inject
    lateinit var model: Model


    private lateinit var openAnimator: Animator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        App.component.injectsMainActivity(this)
        val presenter = Presenter(model)
        presenter.view = this

        openAnimator = AnimatorInflater.loadAnimator(this, R.animator.open_translated)
        openAnimator.setTarget(input_layout)
        input_layout.go_translate.setOnClickListener {
            translateText = input_layout.translate_text.text.toString()
            fromLanguage = input_layout.fromLang.text.toString()
            toLanguage = input_layout.toLang.text.toString()
            presenter.translate()
        }
    }

    override fun openTranslateView() {
        openAnimator.start()
    }

    override fun hideTranslateView() {

    }

    override fun showTranslatedText(text: String) {

    }
}
