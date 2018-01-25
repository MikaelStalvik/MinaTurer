package com.imploded.minaturer.repository

import android.util.Log
import com.github.kittinunf.fuel.httpGet
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

    override fun getLocationsByNameTl(expr: String): LocationContainer {
        val endPoint = locationsByNameTlUrl(expr)
        val (_, _, result) = endPoint
                .httpGet()
                .responseString()
        return when(result)
        {
            is Result.Success -> {
                val data = Gson().fromJson<TlStopLocation>(result.value)
                val stopLocationList: ArrayList<StopLocation> = arrayListOf()
                data.stopList.mapTo(stopLocationList) { StopLocation(it.name, 0, it.lon.toString(), it.lat.toString(), it.id) }
                LocationContainer(LocationList("", "", stopLocationList))
            }
            is Result.Failure -> {
                LocationContainer(LocationList("", "", listOf()))
            }
        }
    }

    override fun getDeparturesTl(id: String, products: Int): DepartureContainer {
        val calender = Calendar.getInstance()

        val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val timeString = dateFormat.format(calender.time)
        val time = URLEncoder.encode(timeString, "UTF-8")
        val endPoint = departuresByIdTlUrl(id, date, time, products)
        val (_, _, result) = endPoint
                .httpGet()
                .responseString()
        return when(result)
        {
            is Result.Success -> {
                Log.d("JSON", result.value)
                val res =  Gson().fromJson<TlDepartureContainer>(result.value)
                val departures: ArrayList<Departure> = arrayListOf()
                for(item in res.departures) {
                    val stopList: ArrayList<Stop> = ArrayList()
                    item.stopContainer.stops.mapTo(stopList) {
                        Stop(
                                name = it.name,
                                id = it.id,
                                routeIdx = it.routeIdx.toString(),
                                depDate = it.depDate.ensureString(),
                                depTime = it.depTime.ensureString().fixTime(),
                                arrDate = it.arrDate.ensureString(),
                                arrTime = it.arrTime.ensureString().fixTime(),
                                track = item.actualTrack()
                        )
                    }

                    departures.add(Departure(
                            name = item.name,
                            sname = item.product.num,
                            stop = item.stop,
                            stopid = item.stopid,
                            date = item.date,
                            time = item.time.fixTime(),
                            rtDate = item.rtDate.ensureString(),
                            rtTime = item.rtTime.ensureString().fixTime(),
                            track = item.rtTrack.ensureString(),
                            direction = item.direction,
                            bgColor = item.bgColor(),
                            fgColor = item.fgColor(),
                            stops = stopList,
                            catOutCode = item.product.catCode
                    ))
                }
                DepartureContainer(DepartureBoard("", "", departures))
            }
            is Result.Failure -> {
                DepartureContainer(DepartureBoard())
            }
        }
    }

    companion object {
        private const val maxJournies = 50
        private fun locationsByNameTlUrl(arg: String) = "https://api.resrobot.se/v2/location.name?key=${WebApiKeys.reserobotKey}&input=$arg&format=json"
        private fun departuresByIdTlUrl(id: String, date: String, time: String, products: Int) = "https://api.resrobot.se/v2/departureBoard?key=${WebApiKeys.reserobotDepartureBoardKey}&id=$id&maxJourneys=$maxJournies&date=$date&time=$time&products=$products&format=json"

    }

}