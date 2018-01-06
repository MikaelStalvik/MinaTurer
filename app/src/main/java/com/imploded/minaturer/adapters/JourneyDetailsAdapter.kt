package com.imploded.minaturer.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.imploded.minaturer.R
import com.imploded.minaturer.model.Stop
import kotlinx.android.synthetic.main.row_journey_detail.view.*


class JourneyDetailsAdapter(private val trackText: String, private val itemClick: (Stop) -> Unit): RecyclerView.Adapter<JourneyDetailsAdapter.JourneyDetailsHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): JourneyDetailsHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.row_journey_detail, parent, false)
        return JourneyDetailsHolder(view, itemClick)
    }

    override fun onBindViewHolder(holder: JourneyDetailsHolder, position: Int) {
        holder.bindStop(trackText, stopItems[position])
    }
    override fun getItemCount(): Int {
        return stopItems.count()
    }
    fun updateItems(f: () -> List<Stop>) {
        stopItems = f()
    }

    private var stopItems: List<Stop> = listOf()

    class JourneyDetailsHolder(view: View, private val itemClick: (Stop) -> Unit) : RecyclerView.ViewHolder(view) {

        fun bindStop(trackText: String, stopItem: Stop) {
            with(stopItem) {
                if (stopItem.depTime.isEmpty()) {
                    itemView.textViewDepTime.text = stopItem.arrTime
                }
                else {
                    itemView.textViewDepTime.text = stopItem.depTime
                }
                if (stopItem.depDate != stopItem.rtDepDate) itemView.textViewDepTimeRt.text = stopItem.rtDepTime else itemView.textViewDepTimeRt.text = ""
                itemView.textViewStopName.text = stopItem.name
                if (stopItem.track.isEmpty()) itemView.textViewTrack.text = "" else itemView.textViewTrack.text = trackText + ": " + stopItem.track
            }
        }
    }
}