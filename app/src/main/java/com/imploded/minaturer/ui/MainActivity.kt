package com.imploded.minaturer.ui

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import com.imploded.minaturer.R
import com.imploded.minaturer.fragments.DeparturesFragment
import com.imploded.minaturer.fragments.FindStopFragment
import com.imploded.minaturer.fragments.LandingPageFragment
import com.imploded.minaturer.interfaces.OnFragmentInteractionListener
import com.imploded.minaturer.model.UiStop
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), OnFragmentInteractionListener {

    override fun onStopAdded(name: String) {
        val snack = Snackbar.make(root_layout, getString(R.string.stop_added) + name, Snackbar.LENGTH_SHORT)
        snack.show()
    }

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
