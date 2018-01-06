package com.imploded.minaturer.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.imploded.minaturer.R
import com.imploded.minaturer.model.UiDeparture
import com.imploded.minaturer.utils.toColor
import kotlinx.android.synthetic.main.row_departure.view.*

class DeparturesAdapter(private val itemChecked: (UiDeparture, Int) -> Unit, private val itemClicked: (UiDeparture, Int) -> Unit): RecyclerView.Adapter<DeparturesAdapter.DepartureHolder>() {

    var showFilter: Boolean = false

    private var departureItems: List<UiDeparture> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): DeparturesAdapter.DepartureHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.row_departure, parent, false)
        return DeparturesAdapter.DepartureHolder(view, itemChecked, itemClicked)
    }

    override fun onBindViewHolder(holder: DeparturesAdapter.DepartureHolder, position: Int) {
        holder.bindDeparture(departureItems[position], showFilter)
    }
    override fun getItemCount(): Int {
        return departureItems.count()
    }
    fun updateItems(f: () -> List<UiDeparture>) {
        departureItems = f()
    }

    fun selectAll() {
        for(item in departureItems) item.checked = true
        notifyDataSetChanged()
    }
    fun selectNone() {
        for(item in departureItems) item.checked = false
        notifyDataSetChanged()
    }

    class DepartureHolder(view: View, private val itemChecked: (UiDeparture, Int) -> Unit, private val itemClicked: (UiDeparture, Int) -> Unit) : RecyclerView.ViewHolder(view) {

        fun bindDeparture(departureItem: UiDeparture, showFilter: Boolean) {
            with(departureItem) {

                if (showFilter) itemView.checkBox.visibility = View.VISIBLE else itemView.checkBox.visibility = View.GONE
                itemView.checkBox.isChecked = departureItem.checked
                itemView.checkBox.setOnClickListener{itemChecked(this, adapterPosition)}

                itemView.textViewLineNumber.text = departureItem.shortName
                itemView.textViewLineNumber.setBackgroundColor(departureItem.fgColor.toColor())
                itemView.textViewLineNumber.setTextColor(departureItem.bgColor.toColor())

                itemView.textViewDepTime.text = departureItem.time
                itemView.textViewDepTimeEta.text = departureItem.rtTime
                if (departureItem.rtTime.isEmpty()) itemView.textViewDepTimeEta.visibility = View.GONE else itemView.textViewDepTimeEta.visibility = View.VISIBLE
                itemView.textViewDirection.text = departureItem.direction

                itemView.setOnClickListener{itemClicked(this, adapterPosition)}
            }
        }
    }
}