package com.imploded.trippinout.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.google.gson.Gson
import com.imploded.trippinout.R
import com.imploded.trippinout.adapters.LandingPageAdapter
import com.imploded.trippinout.adapters.StopsAdapter
import com.imploded.trippinout.interfaces.OnFragmentInteractionListener
import com.imploded.trippinout.model.UiStop
import com.imploded.trippinout.utils.TrippinOutApp
import com.imploded.trippinout.utils.fromJson
import com.imploded.trippinout.viewmodel.LandingViewModel
import java.util.ArrayList


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [LandingPageFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [LandingPageFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LandingPageFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: LandingPageAdapter
    private val viewModel: LandingViewModel = LandingViewModel()

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
        // Inflate the layout for this fragment
        var view = inflater!!.inflate(R.layout.fragment_landing_page, container, false)
        var button = view.findViewById<Button>(R.id.addStopsButton)
        button.setOnClickListener {
            if (mListener != null) {
                mListener!!.onFragmentInteraction(ArgChangeToFindStopsView)
            }
        }

        adapter = createAdapter()
        recyclerView = view.findViewById(R.id.recyclerViewStops)
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        recyclerView.adapter = adapter
        updateAdapter()

        return view
    }

    private fun updateAdapter() {
        adapter.updateItems { viewModel.selectedStops }
        adapter.notifyDataSetChanged()
        recyclerView.scrollToPosition(0)
    }


    private fun createAdapter(): LandingPageAdapter {
        viewModel.getStops()
        return LandingPageAdapter({ mListener.onFragmentInteraction()})
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

        val ArgChangeToFindStopsView = 1

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
         * @return A new instance of fragment LandingPageFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String, param2: String): LandingPageFragment {
            val fragment = LandingPageFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor
