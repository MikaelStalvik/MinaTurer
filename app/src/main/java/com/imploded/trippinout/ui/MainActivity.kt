package com.imploded.trippinout.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.result.Result
import com.google.gson.Gson
import com.imploded.trippinout.fragments.LandingPageFragment
import com.imploded.trippinout.R
import com.imploded.trippinout.fragments.DeparturesFragment
import com.imploded.trippinout.fragments.FindStopFragment
import com.imploded.trippinout.interfaces.OnFragmentInteractionListener
import com.imploded.trippinout.model.LocationContainer
import com.imploded.trippinout.model.LocationList
import com.imploded.trippinout.model.Token
import com.imploded.trippinout.model.UiStop
import com.imploded.trippinout.utils.fromJson
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg

class MainActivity : AppCompatActivity(), OnFragmentInteractionListener {

    /*
    var accessToken: Token = Token("" ,"", "", "")
    var tokenUrl = "https://api.vasttrafik.se:443/token"
    var encodedConsumerKey ="LwpVrKg1hZaijQ2eVPX_ueufz8Ia"
    var encodedConsumerKeySecret = "yLhi2ovU7z6kUlpvbm0sXtQxKHka"

    fun getToken(
            tokenEndpoint: String,
            clientId: String,
            clientSecret: String): Token {
        val(_, _, result) = tokenEndpoint.httpPost(listOf(
            "grant_type" to "client_credentials",
            "client_id" to clientId,
            "client_secret" to clientSecret))
            //"scope" to scopes.joinToString(" ")))
            .responseString()

        return when(result) {
            is Result.Success -> {
                var token = Gson().fromJson<Token>(result.value)
                Log.d("MAIN", "**** Expire: " + token.expiresIn)
                token
            }
            is Result.Failure -> {
                Token("", "", "", "")
            }
        }
    }*/

    /*
    fun getNames(tokenType: String): LocationContainer {
        Log.d("MAIN", "GET NAMES")
        val endPoint = "https://api.vasttrafik.se/bin/rest.exe/v2/location.name?input=gerrebacka&format=json"
        val (_, _, result) = endPoint
                .httpGet()
                .header(Pair("Authorization", "$tokenType ${accessToken.accessToken}"))
                .responseString()
        Log.d("MAIN", "GOT NAMES")
        return when(result)
        {
            is Result.Success -> {
                val lc = Gson().fromJson<LocationContainer>(result.value)
                lc
            }
            is Result.Failure -> {
                LocationContainer(LocationList("", "", listOf()))
            }
        }
    }*/

    /*
    fun getData() = async(UI) {
        val tokenTask = bg {
            getToken(
                    tokenUrl,
                    encodedConsumerKey,
                    encodedConsumerKeySecret
            )
        }
        accessToken = tokenTask.await()
        val locationTask = bg {getNames("Bearer")}
        val ll = locationTask.await()
        Log.d("MAIN", "GOT NAMES: " + ll.locationList.stopLocations.count())
    }*/

    override fun onFindStopsSelected(data: Int) {
        when(data) {
            LandingPageFragment.ArgChangeToFindStopsView -> {
                val findStopFragment = FindStopFragment.newInstance("", "")
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
                    .add(R.id.root_layout, LandingPageFragment.newInstance("", ""), "rageComicList")
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
