package com.imploded.minaturer.fragments

import android.content.Context
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.Button
import android.widget.ProgressBar
import com.imploded.minaturer.R
import com.imploded.minaturer.adapters.DeparturesAdapter
import com.imploded.minaturer.interfaces.DeparturesViewModelInterface
import com.imploded.minaturer.interfaces.OnFragmentInteractionListener
import com.imploded.minaturer.interfaces.SettingsInterface
import com.imploded.minaturer.model.UiStop
import com.imploded.minaturer.utils.*
import org.jetbrains.anko.support.v4.alert
import javax.inject.Inject

class DeparturesFragment : Fragment() {

    @Inject lateinit var viewModel: DeparturesViewModelInterface
    @Inject lateinit var appSettings: SettingsInterface

    private var mListener: OnFragmentInteractionListener? = null

    private lateinit var stopId: String
    private lateinit var stopName: String

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: DeparturesAdapter
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var bottomToolbar: ConstraintLayout
    private lateinit var rootLayout: ConstraintLayout
    private lateinit var progress: ProgressBar

    private fun initFetch() {
        progress.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
    }

    private fun updateAdapter() {
        progress.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
        adapter.updateItems { viewModel.uiDepartures }
        adapter.notifyDataSetChanged()
        recyclerView.applyAnimation()
        //runLayoutAnimation(recyclerView)
        recyclerView.scrollToPosition(0)
        if (viewModel.filtersActive()) {
            this.title("$stopName (${getString(R.string.filtered)})")
        } else {
            this.title(stopName)
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
        stopId = arguments!!.getString(ARG_PARAM1)
        stopName = arguments!!.getString(ARG_PARAM2)
    }

    private fun setupButtons(view: View) {
        val allButton = view.findViewById<Button>(R.id.buttonAll)
        allButton.setOnClickListener {
            mListener?.sendFirebaseEvent(FirebaseConstants.SelectAll)
            viewModel.selectAll()
            adapter.updateItems { viewModel.uiDepartures }
            adapter.notifyDataSetChanged()
        }
        val noneButton = view.findViewById<Button>(R.id.buttonNone)
        noneButton.setOnClickListener{
            mListener?.sendFirebaseEvent(FirebaseConstants.SelectNone)
            viewModel.selectNone()
            adapter.updateItems { viewModel.uiDepartures }
            adapter.notifyDataSetChanged()
        }
        val applyButton = view.findViewById<Button>(R.id.buttonApply)
        applyButton.setOnClickListener{
            mListener?.sendFirebaseEvent(FirebaseConstants.ApplyFilter)
            viewModel.applyFilters()

            viewModel.toggleFilterMode()
            //TransitionManager.beginDelayedTransition(rootLayout)
            if (viewModel.filterActive) bottomToolbar.visibility = android.view.View.VISIBLE else bottomToolbar.visibility = android.view.View.GONE
            adapter.showFilter = viewModel.filterActive
            viewModel.getDepartures(stopId, ::updateAdapter, ::initFetch)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_departures, container, false)
        this.inject()
        this.displayBackNavigation()
        viewModel.setStopId(stopId)

        setupUi(view)

        adapter = createAdapter()
        recyclerView = view.findViewById(R.id.departuresList)
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        recyclerView.adapter = adapter
        progress = view.findViewById(R.id.progress_bar)
        viewModel.getDepartures(stopId, ::updateAdapter, ::initFetch)
        setHasOptionsMenu(true)
        showHint()

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.departure_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
        menu?.findItem(R.id.action_filter_mode)?.tintMenuIcon(context!!, android.R.color.white)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item == null) return super.onOptionsItemSelected(item)
        return when(item.itemId) {
            R.id.action_reset -> {
                viewModel.resetFilterForStop()
                viewModel.getDepartures(stopId, ::updateAdapter, ::initFetch)
                false
            }
            R.id.action_filter_mode -> {
                viewModel.toggleFilterMode()
                mListener?.sendFirebaseEvent(FirebaseConstants.ToggleFilter, if (viewModel.filterActive) FirebaseConstants.FilterOn else FirebaseConstants.FilterOff )
                adapter.showFilter = viewModel.filterActive
                adapter.notifyDataSetChanged()
                //TransitionManager.beginDelayedTransition(rootLayout)
                if (viewModel.filterActive) bottomToolbar.visibility = View.VISIBLE else bottomToolbar.visibility = View.GONE
                false
            }
            android.R.id.home -> {
                activity?.supportFragmentManager?.popBackStack()
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

    private fun setupUi(view: View) {
        swipeRefresh = view.findViewById(R.id.simpleSwipeRefreshLayout)
        swipeRefresh.setOnRefreshListener {
            progress.visibility = View.VISIBLE
            viewModel.getDepartures(stopId, ::updateAdapter, ::initFetch)
            swipeRefresh.isRefreshing = false
        }
        rootLayout = view.findViewById(R.id.rootLayout)
        bottomToolbar = view.findViewById(R.id.bottomToolbarLayout)
        bottomToolbar.visibility = View.GONE

        setupButtons(view)
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
