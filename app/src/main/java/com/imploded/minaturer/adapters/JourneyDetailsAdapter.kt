package com.imploded.minaturer.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.imploded.minaturer.R
import com.imploded.minaturer.model.Stop
import com.imploded.minaturer.utils.AppConstants
import com.imploded.minaturer.utils.toColor
import kotlinx.android.synthetic.main.row_journey_detail.view.*


class JourneyDetailsAdapter(private val trackText: String, private val travelTimeString: String): RecyclerView.Adapter<JourneyDetailsAdapter.JourneyDetailsHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): JourneyDetailsHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.row_journey_detail, parent, false)
        return JourneyDetailsHolder(view)
    }

    override fun onBindViewHolder(holder: JourneyDetailsHolder, position: Int) {
        holder.bindStop(trackText, travelTimeString, stopItems[position])
    }
    override fun getItemCount(): Int {
        return stopItems.count()
    }
    fun updateItems(f: () -> List<Stop>) {
        stopItems = f()
    }

    private var stopItems: List<Stop> = listOf()

    class JourneyDetailsHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bindStop(trackText: String, travelTimeString: String, stopItem: Stop) {
            var bgColor = AppConstants.fragmentBgColor
            if (adapterPosition % 2 == 1) bgColor = AppConstants.oddRowColor
            itemView.rootLayout.setBackgroundColor(bgColor.toColor())
            with(stopItem) {
                if (depTime.isEmpty()) {
                    itemView.textViewDepTime.text = arrTime
                }
                else {
                    itemView.textViewDepTime.text = depTime
                }
                if (depDate != rtDepDate) itemView.textViewDepTimeRt.text = rtDepTime else itemView.textViewDepTimeRt.text = ""
                itemView.textViewStopName.text = name

                if (track.isEmpty()) itemView.textViewTrack.text = "" else itemView.textViewTrack.text = trackText + ": " + track

                val sb = StringBuilder()
                if (timeDiff > 0) sb.append(" ").append(timeDiff/60).append(" ").append(travelTimeString) else sb.append("-")
                itemView.textViewInfo.text = sb
            }
        }
    }
}