package com.imploded.trippinout.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.imploded.trippinout.R
import com.imploded.trippinout.model.UiStop
import kotlinx.android.synthetic.main.row_stop.view.*

class LandingPageAdapter(private val itemClick: (UiStop) -> Unit): RecyclerView.Adapter<LandingPageAdapter.UiStopHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): LandingPageAdapter.UiStopHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.row_stop, parent, false)
        return LandingPageAdapter.UiStopHolder(view, itemClick)
    }

    override fun onBindViewHolder(holder: LandingPageAdapter.UiStopHolder, position: Int) {
        holder.bindStop(stopItems[position])
    }
    override fun getItemCount(): Int {
        return stopItems.count()
    }
    fun updateItems(f: () -> List<UiStop>) {
        stopItems = f()
    }

    private var stopItems: List<UiStop> = listOf()

    class UiStopHolder(view: View, private val itemClick: (UiStop) -> Unit) : RecyclerView.ViewHolder(view) {

        fun bindStop(stopItem: UiStop) {
            with(stopItem) {
                itemView.textViewName.text = name
                itemView.setOnClickListener{itemClick(this)}
            }
        }
    }
}