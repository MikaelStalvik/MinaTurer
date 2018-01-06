package com.imploded.minaturer.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.imploded.minaturer.R
import com.imploded.minaturer.model.UiStop
import com.imploded.minaturer.utils.AppConstants
import com.imploded.minaturer.utils.toColor
import kotlinx.android.synthetic.main.row_draggable_stop.view.*

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
    //fun activeStops(): List<UiStop> = stopItems

    class UiStopHolder(view: View, private val itemClick: (UiStop) -> Unit) : RecyclerView.ViewHolder(view) {

        fun viewForeground() : RelativeLayout = itemView.view_foreground

        fun bindStop(stopItem: UiStop) {
            with(stopItem) {
                itemView.textViewDirection.text = name
                itemView.setOnClickListener{itemClick(this)}
            }
            if (adapterPosition % 2 == 1) itemView.view_foreground.setBackgroundColor(AppConstants.oddRowColor.toColor())
        }
    }
}