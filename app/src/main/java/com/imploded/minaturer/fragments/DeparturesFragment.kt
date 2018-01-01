package com.imploded.minaturer.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import com.imploded.minaturer.R
import com.imploded.minaturer.adapters.DeparturesAdapter
import com.imploded.minaturer.interfaces.OnFragmentInteractionListener
import com.imploded.minaturer.interfaces.SettingsInterface
import com.imploded.minaturer.model.FilteredDepartures
import com.imploded.minaturer.model.FilteredLines
import com.imploded.minaturer.model.UiDeparture
import com.imploded.minaturer.model.UiStop
import com.imploded.minaturer.ui.ChooseFilterTypeDialog
import com.imploded.minaturer.ui.OnDialogInteraction
import com.imploded.minaturer.utils.MinaTurerApp
import com.imploded.minaturer.viewmodel.DeparturesViewModel
import org.jetbrains.anko.support.v4.alert


class DeparturesFragment : Fragment(), OnDialogInteraction {

    private val appSettings: SettingsInterface by lazy {
        MinaTurerApp.prefs
    }

    private val viewModel: DeparturesViewModel = DeparturesViewModel()
    private var mListener: OnFragmentInteractionListener? = null

    private lateinit var selectedItem: UiDeparture
    private lateinit var stopId: String
    private lateinit var stopName: String

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: DeparturesAdapter
    private lateinit var swipeRefresh: SwipeRefreshLayout

    override fun onPositiveClick(selectedIndex: Int) {
        when(selectedIndex) {
            ChooseFilterTypeDialog.FilterByLine -> {
                FilteredLines.addFilteredLine(stopId, selectedItem.shortName)
                FilteredLines.saveData(MinaTurerApp.prefs)
                viewModel.getDepartures(stopId, ::updateAdapter)
            }
            ChooseFilterTypeDialog.FilterByLineAndDirection -> {
                FilteredDepartures.addFilteredTrip(stopId, selectedItem.shortName, selectedItem.direction)
                FilteredDepartures.saveData(MinaTurerApp.prefs)
                viewModel.getDepartures(stopId, ::updateAdapter)
            }
        }
    }

    override fun onNegativeClick() {
    }

    private fun updateAdapter() {
        adapter.updateItems { viewModel.uiDepartures }
        adapter.notifyDataSetChanged()
        recyclerView.scrollToPosition(0)
        if (viewModel.filtersActive(stopId)) {
            (activity as AppCompatActivity).supportActionBar!!.title = stopName + " (" + getString(R.string.filtered) + ")"
        } else {
            (activity as AppCompatActivity).supportActionBar!!.title = stopName
        }
    }

    private fun createAdapter(): DeparturesAdapter {
        viewModel.uiDepartures
        return DeparturesAdapter({
            selectedItem = it
            val dialog = ChooseFilterTypeDialog()
            dialog.setInteraction(this)
            dialog.show(fragmentManager, "Dialog")
        })
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        stopId = arguments.getString(ARG_PARAM1)
        stopName = arguments.getString(ARG_PARAM2)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_departures, container, false)

        swipeRefresh = view.findViewById(R.id.simpleSwipeRefreshLayout)
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
        showHint()

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.departure_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item == null) return super.onOptionsItemSelected(item)
        return when(item.itemId) {
            R.id.action_reset -> {
                viewModel.resetFilterForStop(stopId)
                viewModel.getDepartures(stopId, ::updateAdapter)
                false
            }
            else -> super.onOptionsItemSelected(item)
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

    private fun showHint() {
        val settings = appSettings.loadSettings()
        if (settings.DeparturesHintShown) return
        alert(getString(R.string.departure_page_hint), getString(R.string.tip)) {
            positiveButton(getString(R.string.got_it)) {
                settings.DeparturesHintShown = true
                appSettings.saveSettings(settings)
            }
        }.show()
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
