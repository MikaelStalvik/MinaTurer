package com.imploded.minaturer.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.imploded.minaturer.R
import com.imploded.minaturer.interfaces.OnFragmentInteractionListener
import com.imploded.minaturer.model.UiDeparture
import com.imploded.minaturer.model.UiStop
import com.imploded.minaturer.viewmodel.JourneyDetailViewModel

class JourneyDetailsFragment : Fragment() {

    private val viewModel: JourneyDetailViewModel by lazy {
        JourneyDetailViewModel(sourceRef, sourceLat, sourceLon)
    }
    private lateinit var sourceRef: String
    private lateinit var sourceLat: String
    private lateinit var sourceLon: String


    private var mListener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            sourceRef = arguments.getString(ARG_PARAM1)
            sourceLat = arguments.getString(ARG_PARAM2)
            sourceLon = arguments.getString(ARG_PARAM3)
        }
    }

    private fun updateAdapter() {
        for(stop in viewModel.stops) Log.d("STOP", stop.name + " " + stop.depTime + " arrTime:" + stop.arrTime)
        /*
        adapter.updateItems { viewModel.uiDepartures }
        adapter.notifyDataSetChanged()
        recyclerView.scrollToPosition(0)
        if (viewModel.filtersActive()) {
            (activity as AppCompatActivity).supportActionBar!!.title = stopName + " (" + getString(R.string.filtered) + ")"
        } else {
            (activity as AppCompatActivity).supportActionBar!!.title = stopName
        }*/
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        (activity as AppCompatActivity).supportActionBar!!.title = getString(R.string.journey_details)
        val view =  inflater!!.inflate(R.layout.fragment_journey_details, container, false)

        viewModel.getJourneyDetails(::updateAdapter)

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

    companion object {
        private val ARG_PARAM1 = "ref"
        private val ARG_PARAM2 = "lat"
        private val ARG_PARAM3 = "lon"

        fun newInstance(ref: String, lat: String, lon: String): JourneyDetailsFragment {
            val fragment = JourneyDetailsFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, ref)
            args.putString(ARG_PARAM2, lat)
            args.putString(ARG_PARAM3, lon)
            fragment.arguments = args
            return fragment
        }
    }
}
