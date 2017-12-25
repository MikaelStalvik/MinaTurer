package com.imploded.trippinout.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.imploded.trippinout.R
import com.imploded.trippinout.model.UiDeparture
import com.imploded.trippinout.utils.toColor
import kotlinx.android.synthetic.main.row_departure.view.*

class DeparturesAdapter(private val itemClick: (UiDeparture) -> Unit): RecyclerView.Adapter<DeparturesAdapter.DepartureHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): DeparturesAdapter.DepartureHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.row_departure, parent, false)
        return DeparturesAdapter.DepartureHolder(view, itemClick)
    }

    override fun onBindViewHolder(holder: DeparturesAdapter.DepartureHolder, position: Int) {
        holder.bindDeparture(departureItems[position])
    }
    override fun getItemCount(): Int {
        return departureItems.count()
    }
    fun updateItems(f: () -> List<UiDeparture>) {
        departureItems = f()
    }

    private var departureItems: List<UiDeparture> = listOf()

    class DepartureHolder(view: View, private val itemClick: (UiDeparture) -> Unit) : RecyclerView.ViewHolder(view) {

        fun bindDeparture(departureItem: UiDeparture) {
            with(departureItem) {
                itemView.textViewLineNumber.text = departureItem.shortName
                itemView.textViewLineNumber.setBackgroundColor(departureItem.fgColor.toColor())
                itemView.textViewLineNumber.setTextColor(departureItem.bgColor.toColor())

                itemView.textViewStopName.text = departureItem.stop

                itemView.textViewDepTime.text = departureItem.time
                itemView.textViewDepTimeEta.text = departureItem.rtTime
                itemView.textViewDirection.text = departureItem.direction

                itemView.setOnClickListener{itemClick(this)}
            }
        }
    }
}