package com.yatochk.translator.view

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yatochk.translator.R
import com.yatochk.translator.model.database.DatabaseTranslate
import kotlinx.android.synthetic.main.translate_recycle_item.view.*

class TranslateRecyclerViewAdapter(private val translates: ArrayList<DatabaseTranslate>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            TranslateViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                            R.layout.translate_recycle_item,
                            parent,
                            false)
            )

    override fun getItemCount(): Int = translates.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val mHolder = holder as TranslateViewHolder
        val itemView = mHolder.itemView
        with(itemView) {
            from_text.text = translates[position].from_text
            to_text.text = translates[position].to_text
            from_lang.text = translates[position].fromLanguage
            to_lang.text = translates[position].toLanguage
        }
    }

    class TranslateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}