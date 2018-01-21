package com.imploded.minaturer.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import com.google.gson.Gson
import com.imploded.minaturer.R
import com.imploded.minaturer.adapters.JourneyDetailsAdapter
import com.imploded.minaturer.interfaces.JourneyDetailViewModelInterface
import com.imploded.minaturer.interfaces.OnFragmentInteractionListener
import com.imploded.minaturer.model.UiDeparture
import com.imploded.minaturer.utils.*
import javax.inject.Inject

class JourneyDetailsFragment : Fragment() {

    @Inject lateinit var viewModel: JourneyDetailViewModelInterface

    private lateinit var sourceStopId: String
    private lateinit var departure: UiDeparture

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: JourneyDetailsAdapter
    private lateinit var progress: ProgressBar

    private var mListener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sourceStopId = arguments!!.getString(ARG_PARAM2)
        val json = arguments!!.getString(ARG_PARAM3)
        departure = Gson().fromJson<UiDeparture>(json)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        this.title(getString(R.string.journey_details))
        this.displayBackNavigation()
        val view =  inflater.inflate(R.layout.fragment_journey_details, container, false)
        this.inject()
        viewModel.setInputParameters(sourceStopId, departure)
        progress = view.findViewById(R.id.progress_bar)
        setHeader(view)

        adapter = createAdapter()
        recyclerView = view.findViewById(R.id.journeyRecycleView)
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        recyclerView.adapter = adapter
        viewModel.getJourneyDetails(::updateAdapter, ::initFetch)
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
                activity?.supportFragmentManager?.popBackStack()
                false
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initFetch() {
        progress.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
    }

    private fun updateAdapter() {
        progress.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
        adapter.updateItems { viewModel.stops }
        adapter.notifyDataSetChanged()
        recyclerView.applyAnimation()
        recyclerView.scrollToPosition(0)
    }

    private fun setHeader(view: View) {
        val textView = view.findViewById<TextView>(R.id.textViewLineNumber)
        textView.text = departure.shortName
        textView.setBackgroundColor(departure.bgColor.toColor())
        textView.setTextColor(departure.fgColor.toColor())
        view.findViewById<TextView>(R.id.textViewFrom).text = departure.stop
        view.findViewById<TextView>(R.id.textViewTo).text = departure.direction
        val timeTextView = view.findViewById<TextView>(R.id.textViewDepTime)
        if (departure.rtTime.isNullOrEmpty()) timeTextView.text = departure.time else timeTextView.text = departure.rtTime
    }
    private fun createAdapter(): JourneyDetailsAdapter {
        return JourneyDetailsAdapter(getString(R.string.track), getString(R.string.travel_time))
    }

    companion object {
        private const val ARG_PARAM2 = "stopid"
        private const val ARG_PARAM3 = "departure"

        fun newInstance(stopId: String, departure: UiDeparture): JourneyDetailsFragment {
            val fragment = JourneyDetailsFragment()
            val args = Bundle()
            args.putString(ARG_PARAM2, stopId)
            val json = Gson().toJson(departure)
            args.putString(ARG_PARAM3, json)
            fragment.arguments = args
            return fragment
        }
    }
}
