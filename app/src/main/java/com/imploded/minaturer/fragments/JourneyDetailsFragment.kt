package com.imploded.minaturer.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.gson.Gson
import com.imploded.minaturer.R
import com.imploded.minaturer.adapters.JourneyDetailsAdapter
import com.imploded.minaturer.interfaces.OnFragmentInteractionListener
import com.imploded.minaturer.model.UiDeparture
import com.imploded.minaturer.utils.fromJson
import com.imploded.minaturer.utils.toColor
import com.imploded.minaturer.viewmodel.JourneyDetailViewModel

class JourneyDetailsFragment : Fragment() {

    private val viewModel: JourneyDetailViewModel by lazy {
        JourneyDetailViewModel(sourceRef, sourceStopId)
    }
    private lateinit var sourceRef: String
    private lateinit var sourceStopId: String
    private lateinit var departure: UiDeparture

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: JourneyDetailsAdapter

    private var mListener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            sourceRef = arguments.getString(ARG_PARAM1)
            sourceStopId = arguments.getString(ARG_PARAM2)
            val json = arguments.getString(ARG_PARAM3)
            departure = Gson().fromJson<UiDeparture>(json)
        }
    }

    private fun updateAdapter() {
        adapter.updateItems { viewModel.stops }
        adapter.notifyDataSetChanged()
        recyclerView.scrollToPosition(0)
    }

    private fun setHeader(view: View) {
        val textView = view.findViewById<TextView>(R.id.textViewLineNumber)
        textView.text = departure.shortName
        textView.setBackgroundColor(departure.fgColor.toColor())
        textView.setTextColor(departure.bgColor.toColor())
        view.findViewById<TextView>(R.id.textViewFrom).text = departure.stop
        view.findViewById<TextView>(R.id.textViewTo).text = departure.direction
        val timeTextView = view.findViewById<TextView>(R.id.textViewDepTime)
        if (departure.rtTime.isNullOrEmpty()) timeTextView.text = departure.time else timeTextView.text = departure.rtTime
    }
    private fun createAdapter(): JourneyDetailsAdapter {
        return JourneyDetailsAdapter(getString(R.string.track), getString(R.string.travel_time))
    }
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        (activity as AppCompatActivity).supportActionBar!!.title = getString(R.string.journey_details)
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        val view =  inflater!!.inflate(R.layout.fragment_journey_details, container, false)

        setHeader(view)

        adapter = createAdapter()
        recyclerView = view.findViewById(R.id.journeyRecycleView)
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        recyclerView.adapter = adapter
        viewModel.getJourneyDetails(::updateAdapter)
        setHasOptionsMenu(true)

        return view
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

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item == null) return super.onOptionsItemSelected(item)
        return when(item.itemId) {
            android.R.id.home -> {
                activity.supportFragmentManager.popBackStack()
                false
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        private val ARG_PARAM1 = "ref"
        private val ARG_PARAM2 = "stopid"
        private val ARG_PARAM3 = "departure"

        fun newInstance(ref: String, stopId: String, departure: UiDeparture): JourneyDetailsFragment {
            val fragment = JourneyDetailsFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, ref)
            args.putString(ARG_PARAM2, stopId)
            val json = Gson().toJson(departure)
            args.putString(ARG_PARAM3, json)
            fragment.arguments = args
            return fragment
        }
    }
}
