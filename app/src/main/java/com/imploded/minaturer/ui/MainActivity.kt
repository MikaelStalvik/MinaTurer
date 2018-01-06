package com.imploded.minaturer.ui

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import com.imploded.minaturer.R
import com.imploded.minaturer.fragments.DeparturesFragment
import com.imploded.minaturer.fragments.FindStopFragment
import com.imploded.minaturer.fragments.JourneyDetailsFragment
import com.imploded.minaturer.fragments.LandingPageFragment
import com.imploded.minaturer.interfaces.OnFragmentInteractionListener
import com.imploded.minaturer.model.UiStop
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), OnFragmentInteractionListener {

    override fun onJourneyDetailsSelected(ref: String, stopId: String) {
        val journeyDetailsFragment = JourneyDetailsFragment.newInstance(ref, stopId)
        supportFragmentManager.beginTransaction()
                .replace(R.id.root_layout, journeyDetailsFragment, "journeyDetailsFragment")
                .addToBackStack(null)
                .commit()
    }

    override fun onStopAdded(name: String) {
        val snack = Snackbar.make(root_layout, getString(R.string.stop_added) + name, Snackbar.LENGTH_SHORT)
        snack.show()
    }

    override fun onFindStopsSelected(data: Int) {
        when(data) {
            LandingPageFragment.ArgChangeToFindStopsView -> {
                val findStopFragment = FindStopFragment.newInstance()
                supportFragmentManager.beginTransaction()
                        .replace(R.id.root_layout, findStopFragment, "findStopFragment")
                        .addToBackStack(null)
                        .commit()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.root_layout, LandingPageFragment.newInstance(), "rageComicList")
                    .commit()
        }
    }

    override fun onStopSelected(data: UiStop) {
        val departureFragment = DeparturesFragment.newInstance(data)
        supportFragmentManager.beginTransaction()
                .replace(R.id.root_layout, departureFragment, "departureFragment")
                .addToBackStack(null)
                .commit()
    }

}
