package com.imploded.minaturer.ui

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import com.google.firebase.analytics.FirebaseAnalytics
import com.imploded.minaturer.R
import com.imploded.minaturer.fragments.DeparturesFragment
import com.imploded.minaturer.fragments.FindStopFragment
import com.imploded.minaturer.fragments.JourneyDetailsFragment
import com.imploded.minaturer.fragments.LandingPageFragment
import com.imploded.minaturer.interfaces.OnFragmentInteractionListener
import com.imploded.minaturer.model.UiDeparture
import com.imploded.minaturer.model.UiStop
import com.imploded.minaturer.utils.FirebaseConstants
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), OnFragmentInteractionListener {

    private val firebase: FirebaseAnalytics by lazy {
        FirebaseAnalytics.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sendFirebaseEvent(FirebaseConstants.ApplicationStarted)

        if (savedInstanceState == null) {
            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.root_layout, LandingPageFragment.newInstance(), "landingPageFragment")
                    .commit()
        }
    }

    override fun onJourneyDetailsSelected(ref: String, stopId: String, departure: UiDeparture) {
        sendFirebaseEvent(FirebaseConstants.JourneyDetailsSelected, departure.name)
        val journeyDetailsFragment = JourneyDetailsFragment.newInstance(ref, stopId, departure)
        supportFragmentManager.beginTransaction()
                .replace(R.id.root_layout, journeyDetailsFragment, "journeyDetailsFragment")
                .addToBackStack(null)
                .commit()
    }

    override fun onStopAdded(name: String) {
        sendFirebaseEvent(FirebaseConstants.AddedStop, name)
        val snack = Snackbar.make(root_layout, getString(R.string.stop_added) + name, Snackbar.LENGTH_SHORT)
        snack.show()
    }

    override fun onFindStopsSelected(data: Int) {
        sendFirebaseEvent(FirebaseConstants.ClickedFindStop)
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

    override fun onStopSelected(data: UiStop) {
        sendFirebaseEvent(FirebaseConstants.DepartureSelected, data.name)
        val departureFragment = DeparturesFragment.newInstance(data)
        supportFragmentManager.beginTransaction()
                .replace(R.id.root_layout, departureFragment, "departureFragment")
                .addToBackStack(null)
                .commit()
    }

    override fun sendFirebaseEvent(id: String, itemName: String) {
        val bundle = Bundle()
        with(bundle) {
            putString(FirebaseAnalytics.Param.ITEM_ID, id)
            putString(FirebaseAnalytics.Param.ITEM_NAME, itemName)
            putString(FirebaseAnalytics.Param.CONTENT_TYPE, "text")
        }
        firebase.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
    }

}
