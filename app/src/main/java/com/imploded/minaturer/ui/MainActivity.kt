package com.imploded.minaturer.ui

import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import com.google.firebase.analytics.FirebaseAnalytics
import com.imploded.minaturer.R
import com.imploded.minaturer.application.MinaTurerApp
import com.imploded.minaturer.fragments.DeparturesFragment
import com.imploded.minaturer.fragments.FindStopFragment
import com.imploded.minaturer.fragments.JourneyDetailsFragment
import com.imploded.minaturer.fragments.LandingPageFragment
import com.imploded.minaturer.interfaces.OnFragmentInteractionListener
import com.imploded.minaturer.model.UiDeparture
import com.imploded.minaturer.model.UiStop
import com.imploded.minaturer.utils.FirebaseConstants
import com.imploded.minaturer.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), OnFragmentInteractionListener {

    private val firebase: FirebaseAnalytics by lazy {
        FirebaseAnalytics.getInstance(this)
    }
    private val viewModel: MainViewModel by lazy {
        MainViewModel(MinaTurerApp.prefs)
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

        viewModel.increaseStartCount()
        if (viewModel.shouldAskForRating()) askForRating()
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
        val snack = Snackbar.make(root_layout, getString(R.string.stop_added) + " " + name, Snackbar.LENGTH_SHORT)
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

    private fun askForRating() {
        val alert = AlertDialog.Builder(this)
                .setTitle(getString(R.string.information))
                .setMessage(getString(R.string.rate_text))
                .setPositiveButton(getString(R.string.yes), {_,_ ->

                    val packageName = applicationContext.packageName
                    val activeSettings = MinaTurerApp.prefs.loadSettings()
                    activeSettings.IsRated = true
                    MinaTurerApp.prefs.saveSettings(activeSettings)

                    try {
                        val rateIntent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName))
                        rateIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        applicationContext.startActivity(rateIntent)
                    }
                    catch(e: ActivityNotFoundException) {
                        val rateIntent = Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + packageName))
                        rateIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        applicationContext.startActivity(rateIntent)
                    }

                })
                .setNegativeButton(getString(R.string.no), {_,_ -> })
        alert.create().show()
    }

}
