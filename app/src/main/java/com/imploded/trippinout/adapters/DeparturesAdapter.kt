package com.imploded.trippinout.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.imploded.trippinout.R
import com.imploded.trippinout.model.UiDeparture
import kotlinx.android.synthetic.main.row_stop.view.*

class DeparturesAdapter(private val itemClick: (UiDeparture) -> Unit): RecyclerView.Adapter<DeparturesAdapter.DepartureHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): DeparturesAdapter.DepartureHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.row_stop, parent, false)
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
                itemView.textViewName.text = name
                itemView.setOnClickListener{itemClick(this)}
            }
        }
    }
}