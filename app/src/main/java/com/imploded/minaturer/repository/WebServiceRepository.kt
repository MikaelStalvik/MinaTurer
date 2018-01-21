package com.imploded.minaturer.repository

import android.util.Log
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.result.Result
import com.google.gson.Gson
import com.imploded.minaturer.interfaces.WebServiceInterface
import com.imploded.minaturer.model.*
import com.imploded.minaturer.utils.*
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

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
                accessToken = Gson().fromJson<Token>(result.value)
                accessToken
            }
            is Result.Failure -> {
                Token()
            }
        }
    }
    override fun getLocationsByName(expr: String): LocationContainer {
        val endPoint = locationsByNameUrl(expr)
        val (_, _, result) = endPoint
                .httpGet()
                .header(Pair("Authorization", "$tokenType ${accessToken.accessToken}"))
                .responseString()
        return when(result)
        {
            is Result.Success -> {
                Log.d("JSON", result.value)
                Gson().fromJson<LocationContainer>(result.value)
            }
            is Result.Failure -> {
                LocationContainer(LocationList("", "", listOf()))
            }
        }
    }

    override fun getDepartures(id: String): DepartureContainer {
        val calender = Calendar.getInstance()

        val date = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())

        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val timeString = dateFormat.format(calender.time)
        val time = URLEncoder.encode(timeString, "UTF-8")
        val endPoint = departuresById(id, date, time)
        val (_, _, result) = endPoint
                .httpGet()
                .header(Pair("Authorization", "$tokenType ${accessToken.accessToken}"))
                .responseString()
        return when(result)
        {
            is Result.Success -> {
                val res =  Gson().fromJson<DepartureContainer>(result.value)
                res
            }
            is Result.Failure -> {
                DepartureContainer(DepartureBoard())
            }
        }
    }

    override fun getJourneyDetails(endPoint: String) : JourneyDetailsContainer {
        val (_, _, result) = endPoint
                .httpGet()
                .header(Pair("Authorization", "$tokenType ${accessToken.accessToken}"))
                .responseString()
        return when(result)
        {
            is Result.Success -> {
                val res =  Gson().fromJson<JourneyDetailsContainer>(result.value)
                res
            }
            is Result.Failure -> {
                JourneyDetailsContainer(JourneyDetail())
            }
        }
    }

    override fun getLocationsByNameTl(expr: String): LocationContainer {
        val endPoint = locationsByNameTlUrl(expr)
        val (_, _, result) = endPoint
                .httpGet()
                .responseString()
        return when(result)
        {
            is Result.Success -> {
                //Log.d("JSON", result.value)
                val data = Gson().fromJson<TlStopLocation>(result.value)
                var stopLocationList: ArrayList<StopLocation> = arrayListOf()
                data.stopList.mapTo(stopLocationList) { StopLocation(it.name, 0, it.lon.toString(), it.lat.toString(), it.id) }
                LocationContainer(LocationList("", "", stopLocationList))
            }
            is Result.Failure -> {
                LocationContainer(LocationList("", "", listOf()))
            }
        }
    }

    override fun getDeparturesTl(id: String): DepartureContainer {
        val calender = Calendar.getInstance()

        val date = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())

        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val timeString = dateFormat.format(calender.time)
        val time = URLEncoder.encode(timeString, "UTF-8")
        val endPoint = departuresByIdTlUrl(id, date, time)
        val (_, _, result) = endPoint
                .httpGet()
                .header(Pair("Authorization", "$tokenType ${accessToken.accessToken}"))
                .responseString()
        return when(result)
        {
            is Result.Success -> {
                Log.d("JSON", result.value)
                val res =  Gson().fromJson<TlDepartureContainer>(result.value)
                var departures: ArrayList<Departure> = arrayListOf()
                for(item in res.departures) {
                    try {
                        departures.add(Departure(
                                name = item.name,
                                sname = item.product.num,
                                stop = item.stop,
                                stopid = item.stopid,
                                time = item.time.fixTime(),
                                date = item.date,
                                rtTime = if (item.rtTime.isNullOrBlank()) "" else item.rtTime.fixTime(),
                                rtDate = if (item.rtDate.isNullOrBlank()) "" else item.rtDate,
                                track = if (item.rtTrack.isNullOrBlank()) "" else item.rtTrack,
                                direction = item.direction,
                                bgColor = item.bgColor(),
                                fgColor = item.fgColor(),
                                journeyRefIds = JourneyRef()
                        ))
                    }
                    catch(ex: Exception) {
                        val kk = 123
                    }
                }
                val dc = DepartureContainer(DepartureBoard("", "", departures))
                dc
            }
            is Result.Failure -> {
                DepartureContainer(DepartureBoard())
            }
        }
    }


    companion object {
        private val tokenUrl = "https://api.vasttrafik.se:443/token"
        private fun locationsByNameUrl(arg: String) = "https://api.vasttrafik.se/bin/rest.exe/v2/location.name?input=$arg&format=json"
        private fun departuresById(id: String, date: String, time: String) = "https://api.vasttrafik.se/bin/rest.exe/v2/departureBoard?id=$id&date=$date&time=$time&format=json"

        private fun locationsByNameTlUrl(arg: String) = "https://api.resrobot.se/v2/location.name?key=d6fd4f44-113c-45d1-b542-caad22888095&input=$arg&format=json"
        private fun departuresByIdTlUrl(id: String, date: String, time: String) = "https://api.resrobot.se/v2/departureBoard?key=27da7f92-ed37-4e87-8ecc-2ae07032acdf&id=$id&maxJourneys=15&format=json"

        private val tokenType = "Bearer"
    }

}