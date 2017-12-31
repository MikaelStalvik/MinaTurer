package com.imploded.trippinout.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.imploded.trippinout.R
import com.imploded.trippinout.model.UiStop
import kotlinx.android.synthetic.main.row_draggable_stop.view.*
import kotlinx.android.synthetic.main.row_stop.view.*

class LandingPageAdapter(private val itemClick: (UiStop) -> Unit): RecyclerView.Adapter<LandingPageAdapter.UiStopHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): LandingPageAdapter.UiStopHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.row_draggable_stop, parent, false)
        return LandingPageAdapter.UiStopHolder(view, itemClick)
    }

    override fun onBindViewHolder(holder: LandingPageAdapter.UiStopHolder, position: Int) {
        holder.bindStop(stopItems[position])
    }
    override fun getItemCount(): Int {
        return stopItems.count()
    }
    fun updateItems(f: () -> ArrayList<UiStop>) {
        stopItems = f()
    }

    fun removeItem(position: Int) {
        stopItems.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, stopItems.size)
    }

    private var stopItems: ArrayList<UiStop> = arrayListOf()

    class UiStopHolder(view: View, private val itemClick: (UiStop) -> Unit) : RecyclerView.ViewHolder(view) {

        fun viewForeground() : RelativeLayout = itemView.view_foreground

        fun bindStop(stopItem: UiStop) {
            with(stopItem) {
                itemView.textViewStopName.text = name
                itemView.setOnClickListener{itemClick(this)}
            }
        }
    }
}