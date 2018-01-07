package com.imploded.minaturer

import com.imploded.minaturer.interfaces.SettingsInterface
import com.imploded.minaturer.interfaces.WebServiceInterface
import com.imploded.minaturer.model.*
import com.imploded.minaturer.viewmodel.DeparturesViewModel
import com.imploded.minaturer.viewmodel.LandingViewModel
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.experimental.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.anyString
import java.util.logging.Filter

class DepartureViewModelTests {

    private lateinit var viewModel: DeparturesViewModel
    private lateinit var mockSettings: SettingsInterface
    private lateinit var webservice: WebServiceInterface
    private lateinit var departures: DepartureContainer
    private val stopId = "12345"

    @Before
    fun InitTests() {
        departures = DepartureContainer(DepartureBoard(
                "",
                "",
                listOf(
                        Departure("Rosa express", "ROSA", "bus", "Nordstan", "12345", "17:00", "2018-01-07", "23456", "Gerrebacka", "D", "", "", "#e89dc0", "#ffffff", "solid", "wheelChair", JourneyRef("https://api.vasttrafik.se/bin/rest.exe")),
                        Departure("Rosa express", "ROSA", "bus", "Nordstan", "12345", "17:05", "2018-01-07", "23456", "Billdal", "B", "", "", "#e89dc0", "#ffffff", "solid", "wheelChair", JourneyRef("https://api.vasttrafik.se/bin/rest.exe")),
                        Departure("Buss 58", "58", "bus", "Nordstan", "12345", "17:32", "2018-01-07", "23456", "Eriksberg", "C", "", "", "#00A5DC", "#ffffff", "solid", "", JourneyRef("https://api.vasttrafik.se/bin/rest.exe"))
                )
        ))
        webservice = Mockito.mock(WebServiceInterface::class.java)
        `when`(webservice.getToken()).thenReturn(Token("scope", "bearer", "3600", "abcdef1234"))
        `when`(webservice.getDepartures(ArgumentMatchers.anyString())).thenReturn(departures)

        mockSettings = Mockito.mock(SettingsInterface::class.java)
        viewModel = DeparturesViewModel(stopId, mockSettings, webservice)
    }

    @Test
    fun givenFetchingDepartures_whenNoFiltersAreApplied_thenThreeDeparturesShallBeReturned(){
        viewModel.generateFilteredDepartures(departures)
        assertTrue(viewModel.uiDepartures.size == 3)
    }

    @Test
    fun givenFetchingDepartures_whenFiltersAreApplied_thenOneDepartureShallBeReturned(){
        // Cannot mock a final class properly therefore use test method
        var filterData: HashMap<String, ArrayList<FilteredDeparture>> = hashMapOf(
                "12345" to arrayListOf(
                FilteredDeparture("ROSA", "Gerrebacka"),
                FilteredDeparture("ROSA", "Billdal")
        ))

        FilteredDepartures.setTestData(filterData)

        viewModel.generateFilteredDepartures(departures)
        assertTrue(viewModel.uiDepartures.size == 1)
    }
}