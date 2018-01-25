package com.imploded.minaturer.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.imploded.minaturer.R
import com.imploded.minaturer.interfaces.OnFragmentInteractionListener
import com.imploded.minaturer.interfaces.SettingsViewModelInterface
import com.imploded.minaturer.utils.*
import kotlinx.android.synthetic.main.fragment_settings.*
import javax.inject.Inject

class SettingsPageFragment : Fragment() {

    @Inject
    lateinit var viewModel: SettingsViewModelInterface

    private var mListener: OnFragmentInteractionListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        this.title(getString(R.string.settings))
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        this.inject()
        this.displayBackNavigation()
        setHasOptionsMenu(true)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activeFilters = viewModel.activeFilters()
        switchExpressBus.isChecked = activeFilters.isExpressTrain()
        switchRegionalTrain.isChecked = activeFilters.isRegionalTrain()
        switchExpressBus.isChecked = activeFilters.isExpressBus()
        switchLocalTrain.isChecked = activeFilters.isLocalTrain()
        switchSubway.isChecked = activeFilters.isSubway()
        switchTram.isChecked = activeFilters.isTram()
        switchBus.isChecked = activeFilters.isBus()
        switchFerry.isChecked = activeFilters.isFerry()
    }

    override fun onPause() {
        super.onPause()
        viewModel.saveFilters(
                switchExpressTrain.isChecked,
                switchRegionalTrain.isChecked,
                switchExpressBus.isChecked,
                switchLocalTrain.isChecked,
                switchSubway.isChecked,
                switchTram.isChecked,
                switchBus.isChecked,
                switchFerry.isChecked)
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

    companion object {
        fun newInstance(): SettingsPageFragment {
            return SettingsPageFragment()
        }
    }
}
