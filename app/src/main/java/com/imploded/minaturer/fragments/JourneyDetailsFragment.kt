package com.imploded.minaturer.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.imploded.minaturer.R
import com.imploded.minaturer.adapters.JourneyDetailsAdapter
import com.imploded.minaturer.interfaces.OnFragmentInteractionListener
import com.imploded.minaturer.viewmodel.JourneyDetailViewModel

class JourneyDetailsFragment : Fragment() {

    private val viewModel: JourneyDetailViewModel by lazy {
        JourneyDetailViewModel(sourceRef, sourceStopId)
    }
    private lateinit var sourceRef: String
    private lateinit var sourceStopId: String

    /*
    private val appSettings: SettingsInterface by lazy {
        MinaTurerApp.prefs
    }*/

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: JourneyDetailsAdapter


    private var mListener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            sourceRef = arguments.getString(ARG_PARAM1)
            sourceStopId = arguments.getString(ARG_PARAM2)
        }
    }

    private fun updateAdapter() {
        adapter.updateItems { viewModel.stops }
        adapter.notifyDataSetChanged()
        recyclerView.scrollToPosition(0)
    }

    private fun createAdapter(): JourneyDetailsAdapter {
        return JourneyDetailsAdapter(getString(R.string.track), getString(R.string.travel_time), {})
    }
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        (activity as AppCompatActivity).supportActionBar!!.title = getString(R.string.journey_details)
        val view =  inflater!!.inflate(R.layout.fragment_journey_details, container, false)

        adapter = createAdapter()
        recyclerView = view.findViewById(R.id.journeyRecycleView)
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        recyclerView.adapter = adapter
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
        private val ARG_PARAM2 = "stopid"

        fun newInstance(ref: String, stopId: String): JourneyDetailsFragment {
            val fragment = JourneyDetailsFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, ref)
            args.putString(ARG_PARAM2, stopId)
            fragment.arguments = args
            return fragment
        }
    }
}
