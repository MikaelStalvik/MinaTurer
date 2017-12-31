package com.imploded.trippinout.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.imploded.trippinout.R
import com.imploded.trippinout.adapters.StopsAdapter
import com.imploded.trippinout.interfaces.OnFragmentInteractionListener
import com.imploded.trippinout.utils.afterTextChanged
import com.imploded.trippinout.viewmodel.FindStopsViewModel
import org.jetbrains.anko.runOnUiThread
import kotlin.concurrent.fixedRateTimer

class FindStopFragment : Fragment() {

    private val viewModel: FindStopsViewModel = FindStopsViewModel()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: StopsAdapter

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null

    private var mListener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = arguments.getString(ARG_PARAM1)
            mParam2 = arguments.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        (activity as AppCompatActivity).supportActionBar!!.title = getString(R.string.find_stop)
        val view = inflater!!.inflate(R.layout.fragment_find_stop, container, false)
        val editText = view.findViewById<EditText>(R.id.editTextSearch)
        editText.afterTextChanged {
            fixedRateTimer("timer", false, 0, 750, {
                this.cancel()
                val filter = editText.text.toString()
                if (viewModel.updateFiltering(filter)) {
                    context.runOnUiThread {
                        if (!viewModel.isSearching) {
                            viewModel.isSearching = true
                            viewModel.getStops(::updateAdapter)
                        }
                    }
                }
            })
        }
        adapter = createAdapter()
        recyclerView = view.findViewById(R.id.recyclerViewStops)
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        recyclerView.adapter = adapter

        return view
    }

    private fun updateAdapter() {
        adapter.updateItems { viewModel.locations.locationList.stopLocations }
        adapter.notifyDataSetChanged()
        recyclerView.scrollToPosition(0)
        viewModel.isSearching = false
    }


    private fun createAdapter(): StopsAdapter {
        return StopsAdapter({
            viewModel.addStop(it)
            activity.supportFragmentManager.popBackStack()
        })
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
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FindStopFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String, param2: String): FindStopFragment {
            val fragment = FindStopFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor
