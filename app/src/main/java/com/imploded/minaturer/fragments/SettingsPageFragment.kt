package com.imploded.minaturer.fragments

import android.content.Context
import android.graphics.Canvas
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.*
import com.imploded.minaturer.R
import com.imploded.minaturer.adapters.LandingPageAdapter
import com.imploded.minaturer.interfaces.LandingViewModelInterface
import com.imploded.minaturer.interfaces.OnFragmentInteractionListener
import com.imploded.minaturer.interfaces.SettingsInterface
import com.imploded.minaturer.utils.*
import org.jetbrains.anko.support.v4.alert
import javax.inject.Inject

class SettingsPageFragment : Fragment() {

    @Inject
    lateinit var viewModel: LandingViewModelInterface
    @Inject
    lateinit var appSettings: SettingsInterface

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: LandingPageAdapter

    private var mListener: OnFragmentInteractionListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        this.title(getString(R.string.settings))
        this.hideBackNavigation()
        val view = inflater.inflate(R.layout.fragment_settings, container, false)
        /*
        this.inject()
        view.findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            if (mListener != null) {
                mListener!!.onFindStopsSelected(ArgChangeToFindStopsView)
            }
        }

        adapter = createAdapter()
        recyclerView = view.findViewById(R.id.recyclerViewStops)
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        recyclerView.adapter = adapter
        initSwipe()
        updateAdapter()

        showHint()
        */
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


    private fun showHint() {
        val settings =  appSettings.loadSettings()
        if (settings.LandingHintPageShown) return
        alert(getString(R.string.landing_page_hint), getString(R.string.tip)) {
            positiveButton(getString(R.string.got_it)) {
                settings.LandingHintPageShown = true
                appSettings.saveSettings(settings)
            }
        }.show()
    }

    companion object {
        const val ArgChangeToFindStopsView = 1
        fun newInstance(): SettingsPageFragment {
            return SettingsPageFragment()
        }
    }
}
