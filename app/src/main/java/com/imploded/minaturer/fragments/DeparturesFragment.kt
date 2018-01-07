package com.imploded.minaturer.fragments

import android.content.Context
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.Button
import com.imploded.minaturer.R
import com.imploded.minaturer.adapters.DeparturesAdapter
import com.imploded.minaturer.interfaces.OnFragmentInteractionListener
import com.imploded.minaturer.interfaces.SettingsInterface
import com.imploded.minaturer.model.FilteredDepartures
import com.imploded.minaturer.model.UiDeparture
import com.imploded.minaturer.model.UiStop
import com.imploded.minaturer.repository.WebServiceRepository
import com.imploded.minaturer.ui.ChooseFilterTypeDialog
import com.imploded.minaturer.ui.OnDialogInteraction
import com.imploded.minaturer.utils.MinaTurerApp
import com.imploded.minaturer.utils.tintMenuIcon
import com.imploded.minaturer.viewmodel.DeparturesViewModel
import org.jetbrains.anko.support.v4.alert


class DeparturesFragment : Fragment(), OnDialogInteraction {

    private val appSettings: SettingsInterface by lazy {
        MinaTurerApp.prefs
    }

    private val viewModel: DeparturesViewModel by lazy {
        DeparturesViewModel(stopId, appSettings, WebServiceRepository())
    }
    private var mListener: OnFragmentInteractionListener? = null

    private lateinit var selectedItem: UiDeparture
    private lateinit var stopId: String
    private lateinit var stopName: String

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: DeparturesAdapter
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var bottomToolbar: ConstraintLayout
    private lateinit var rootLayout: ConstraintLayout


    override fun onPositiveClick(selectedIndex: Int) {
        when(selectedIndex) {
            ChooseFilterTypeDialog.FilterByLine -> {
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
        if (viewModel.filtersActive()) {
            (activity as AppCompatActivity).supportActionBar!!.title = stopName + " (" + getString(R.string.filtered) + ")"
        } else {
            (activity as AppCompatActivity).supportActionBar!!.title = stopName
        }
    }

    private fun createAdapter(): DeparturesAdapter {
        viewModel.uiDepartures
        return DeparturesAdapter({ _, position ->
            viewModel.uiDepartures[position].checked = !viewModel.uiDepartures[position].checked
            adapter.notifyDataSetChanged()
        }, {item, _ ->
            mListener!!.onJourneyDetailsSelected(item.journeyRefIds.ref, item.stopId, item)
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

        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        swipeRefresh = view.findViewById(R.id.simpleSwipeRefreshLayout)
        swipeRefresh.setOnRefreshListener {
            viewModel.getDepartures(stopId, ::updateAdapter)
            swipeRefresh.isRefreshing = false
        }
        rootLayout = view.findViewById(R.id.rootLayout)
        bottomToolbar = view.findViewById(R.id.bottomToolbarLayout)
        bottomToolbar.visibility = View.GONE

        val allButton = view.findViewById<Button>(R.id.buttonAll)
        allButton.setOnClickListener {
            viewModel.selectAll()
            adapter.updateItems { viewModel.uiDepartures }
            adapter.notifyDataSetChanged()
        }
        val noneButton = view.findViewById<Button>(R.id.buttonNone)
        noneButton.setOnClickListener{
            viewModel.selectNone()
            adapter.updateItems { viewModel.uiDepartures }
            adapter.notifyDataSetChanged()
        }
        val applyButton = view.findViewById<Button>(R.id.buttonApply)
        applyButton.setOnClickListener{
            viewModel.applyFilters()

            viewModel.toggleFilterMode()
            //##TransitionManager.beginDelayedTransition(rootLayout)
            if (viewModel.filterMode) bottomToolbar.visibility = View.VISIBLE else bottomToolbar.visibility = View.GONE
            adapter.showFilter = viewModel.filterMode
            viewModel.getDepartures(stopId, ::updateAdapter)
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
        menu?.findItem(R.id.action_filter_mode)?.tintMenuIcon(context, android.R.color.white)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item == null) return super.onOptionsItemSelected(item)
        return when(item.itemId) {
            R.id.action_reset -> {
                viewModel.resetFilterForStop()
                viewModel.getDepartures(stopId, ::updateAdapter)
                false
            }
            R.id.action_filter_mode -> {
                viewModel.toggleFilterMode()
                adapter.showFilter = viewModel.filterMode
                //##TransitionManager.beginDelayedTransition(rootLayout)
                if (viewModel.filterMode) bottomToolbar.visibility = View.VISIBLE else bottomToolbar.visibility = View.GONE
                adapter.notifyDataSetChanged()
                false
            }
            android.R.id.home -> {
                activity.supportFragmentManager.popBackStack()
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
