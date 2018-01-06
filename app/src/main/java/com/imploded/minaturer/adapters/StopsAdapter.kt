package com.imploded.minaturer.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.imploded.minaturer.R
import com.imploded.minaturer.model.StopLocation
import com.imploded.minaturer.utils.AppConstants
import com.imploded.minaturer.utils.toColor
import kotlinx.android.synthetic.main.row_stop.view.*

class StopsAdapter(private val itemClick: (StopLocation) -> Unit): RecyclerView.Adapter<StopsAdapter.StopsHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): StopsHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.row_stop, parent, false)
        return StopsHolder(view, itemClick)
    }

    override fun onBindViewHolder(holder: StopsHolder, position: Int) {
        holder.bindStop(stopItems[position])
    }
    override fun getItemCount(): Int {
        return stopItems.count()
    }
    fun updateItems(f: () -> List<StopLocation>) {
        stopItems = f()
    }

    private var stopItems: List<StopLocation> = listOf()

    class StopsHolder(view: View, private val itemClick: (StopLocation) -> Unit) : RecyclerView.ViewHolder(view) {

        fun bindStop(stopItem: StopLocation) {
            var bgColor = AppConstants.fragmentBgColor
            if (adapterPosition % 2 == 1) bgColor = AppConstants.oddRowColor
            itemView.searchBackgroundView.setBackgroundColor(bgColor.toColor())

            with(stopItem) {
                itemView.textViewName.text = name
                itemView.setOnClickListener{itemClick(this)}
            }
        }
    }
}