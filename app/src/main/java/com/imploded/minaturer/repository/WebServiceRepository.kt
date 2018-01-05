package com.imploded.minaturer.repository

import android.util.Log
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.result.Result
import com.google.gson.Gson
import com.imploded.minaturer.interfaces.WebServiceInterface
import com.imploded.minaturer.model.*
import com.imploded.minaturer.utils.WebApiKeys
import com.imploded.minaturer.utils.fromJson
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.*

class WebServiceRepository : WebServiceInterface{

    private var accessToken: Token = Token()

    override fun getToken(): Token {
        val endpoint = tokenUrl
        val(_, _, result) = endpoint.httpPost(listOf(
                "grant_type" to "client_credentials",
                "client_id" to WebApiKeys.clientId,
                "client_secret" to WebApiKeys.clientSecret))
                .responseString()

        return when(result) {
            is Result.Success -> {
                Log.d("WS", "GOT TOKEN")
                accessToken = Gson().fromJson<Token>(result.value)
                accessToken
            }
            is Result.Failure -> {
                Token()
            }
        }
    }
    override fun getLocationsByName(expr: String): LocationContainer {
        Log.d("WS", "TOKEN: " + accessToken.accessToken + " " + expr)
        val endPoint = locationsByNameUrl(expr)
        val (_, _, result) = endPoint
                .httpGet()
                .header(Pair("Authorization", "$tokenType ${accessToken.accessToken}"))
                .responseString()
        return when(result)
        {
            is Result.Success -> {
                Log.d("HEJ", result.value)
                Gson().fromJson<LocationContainer>(result.value)
            }
            is Result.Failure -> {
                LocationContainer(LocationList("", "", listOf()))
            }
        }
    }

    override fun getDepartures(id: String): DepartureContainer {
        //Log.d("WS", "TOKEN: " + accessToken.accessToken + " " + id)
        val calender = Calendar.getInstance()

        val date = SimpleDateFormat("yyyyMMdd").format(Date())

        val dateFormat = SimpleDateFormat("HH:mm")
        val timeString = dateFormat.format(calender.time)
        val time = URLEncoder.encode(timeString, "UTF-8")
        val endPoint = departuresById(id, date, time)
        //Log.d("WS", "Departures: " + endPoint)
        val (_, _, result) = endPoint
                .httpGet()
                .header(Pair("Authorization", "$tokenType ${accessToken.accessToken}"))
                .responseString()
        return when(result)
        {
            is Result.Success -> {
                Log.d("HEJ", result.value)
                val res =  Gson().fromJson<DepartureContainer>(result.value)
                res
            }
            is Result.Failure -> {
                DepartureContainer(DepartureBoard())
            }
        }
    }

    override fun getJourneyDetails(ref: String) : JourneyDetailsContainer {
        var endPoint = ref; //journeyDetailsByRef(ref)
        val (_, _, result) = endPoint
                .httpGet()
                .header(Pair("Authorization", "$tokenType ${accessToken.accessToken}"))
                .responseString()
        return when(result)
        {
            is Result.Success -> {
                Log.d("JDET", result.value)
                val res =  Gson().fromJson<JourneyDetailsContainer>(result.value)
                res
            }
            is Result.Failure -> {
                Log.d("JDET", "FAIL!!!")
                JourneyDetailsContainer(JourneyDetail())
            }
        }
    }

    companion object {
        private val tokenUrl = "https://api.vasttrafik.se:443/token"
        private fun locationsByNameUrl(arg: String) = "https://api.vasttrafik.se/bin/rest.exe/v2/location.name?input=$arg&format=json"
        private fun departuresById(id: String, date: String, time: String) = "https://api.vasttrafik.se/bin/rest.exe/v2/departureBoard?id=$id&date=$date&time=$time&format=json"
        private fun journeyDetailsByRef(ref: String) = "https://api.vasttrafik.se/bin/rest.exe/v2/journeyDetail?ref=$ref"

        private val tokenType = "Bearer"
    }

}