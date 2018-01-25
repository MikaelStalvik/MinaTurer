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
import com.imploded.minaturer.R
import com.imploded.minaturer.adapters.StopsAdapter
import com.imploded.minaturer.interfaces.FindStopsViewModelInterface
import com.imploded.minaturer.interfaces.OnFragmentInteractionListener
import com.imploded.minaturer.interfaces.SettingsInterface
import com.imploded.minaturer.utils.*
import org.jetbrains.anko.support.v4.alert
import javax.inject.Inject
import kotlin.concurrent.fixedRateTimer
import kotlinx.android.synthetic.main.fragment_find_stop.*

class FindStopFragment : Fragment() {

    @Inject lateinit var viewModel: FindStopsViewModelInterface
    @Inject lateinit var appSettings: SettingsInterface

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: StopsAdapter
    private var mListener: OnFragmentInteractionListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        this.title(getString(R.string.find_stop))
        this.displayBackNavigation()
        val view = inflater.inflate(R.layout.fragment_find_stop, container, false)
        this.inject()
        showHint()
        setHasOptionsMenu(true)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi(view)
        adapter = createAdapter()
        recyclerView = view.findViewById(R.id.recyclerViewStops)
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        recyclerView.adapter = adapter
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

    private fun setupUi(view: View) {
        editTextSearch.afterTextChanged {
            fixedRateTimer("timer", false, 0, 750, {
                this.cancel()
                val filter = editTextSearch.text.toString()
                if (viewModel.updateFiltering(filter)) {
                    activity?.runOnUiThread{
                        if (!viewModel.isSearching) {
                            viewModel.isSearching = true
                            viewModel.getStops(::updateAdapter)
                        }
                    }
                }
            })
        }
        editTextSearch.setOnFocusChangeListener { _, hasFocus -> if (!hasFocus) editTextSearch.hideKeyboard(activity!!.inputMethodManager()) }
    }

    private fun updateAdapter() {
        adapter.updateItems { viewModel.locations.locationList.stopLocations }
        adapter.notifyDataSetChanged()
        recyclerView.scrollToPosition(0)
        viewModel.isSearching = false
    }


    private fun createAdapter(): StopsAdapter {
        return StopsAdapter({
            editTextSearch.hideKeyboard(activity!!.inputMethodManager())
            viewModel.addStop(it)
            mListener?.onStopAdded(it.name)
            activity?.supportFragmentManager?.popBackStack()
        })
    }

    private fun showHint() {
        val settings = appSettings.loadSettings()
        if (settings.FindStopHintShown) return
        alert(getString(R.string.find_stop_page_hint), getString(R.string.tip)) {
            positiveButton(getString(R.string.got_it)) {
                settings.FindStopHintShown = true
                appSettings.saveSettings(settings)
            }
        }.show()
    }

    companion object {
        fun newInstance(): FindStopFragment {
            return FindStopFragment()
        }
    }
}
