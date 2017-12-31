package com.imploded.trippinout.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.*
import com.imploded.trippinout.R
import com.imploded.trippinout.adapters.DeparturesAdapter
import com.imploded.trippinout.interfaces.OnFragmentInteractionListener
import com.imploded.trippinout.model.FilteredDepartures
import com.imploded.trippinout.model.UiStop
import com.imploded.trippinout.utils.TrippinOutApp
import com.imploded.trippinout.viewmodel.DeparturesViewModel


class DeparturesFragment : Fragment() {

    private lateinit var stopId: String
    private lateinit var stopName: String

    private val viewModel: DeparturesViewModel = DeparturesViewModel()
    private var mListener: OnFragmentInteractionListener? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: DeparturesAdapter
    private lateinit var swipeRefresh: SwipeRefreshLayout

    private fun updateAdapter() {
        adapter.updateItems { viewModel.uiDepartures }
        adapter.notifyDataSetChanged()
        recyclerView.scrollToPosition(0)
        (activity as AppCompatActivity).supportActionBar!!.title = stopName + " (" + FilteredDepartures.filterCountForStop(stopId).toString() + ")"
    }


    private fun createAdapter(): DeparturesAdapter {
        viewModel.uiDepartures
        return DeparturesAdapter({
            FilteredDepartures.addFilteredTrip(stopId, it.shortName, it.direction)
            FilteredDepartures.saveData(TrippinOutApp.prefs)
            viewModel.getDepartures(stopId, ::updateAdapter)
        })
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        stopId = arguments.getString(ARG_PARAM1)
        stopName = arguments.getString(ARG_PARAM2)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater!!.inflate(R.layout.fragment_departures, container, false)

        swipeRefresh = view.findViewById<SwipeRefreshLayout>(R.id.simpleSwipeRefreshLayout)
        swipeRefresh.setOnRefreshListener {
            viewModel.getDepartures(stopId, ::updateAdapter)
            swipeRefresh.isRefreshing = false
        }

        adapter = createAdapter()
        recyclerView = view.findViewById(R.id.departuresList)
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        recyclerView.adapter = adapter
        viewModel.getDepartures(stopId, ::updateAdapter)
        setHasOptionsMenu(true)

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.departure_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item == null) return super.onOptionsItemSelected(item)
        when(item.itemId) {
            R.id.action_reset -> {
                FilteredDepartures.resetFilterForStop(stopId)
                viewModel.getDepartures(stopId, ::updateAdapter)
                return false
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            mListener = context
        } else {
            throw RuntimeException(context!!.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    companion object {
        private val ARG_PARAM1 = "stopId"
        private val ARG_PARAM2 = "stopName"

        fun newInstance(stop: UiStop): DeparturesFragment {
            val fragment = DeparturesFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, stop.id)
            args.putString(ARG_PARAM2, stop.name)
            fragment.arguments = args
            return fragment
        }
    }
}
