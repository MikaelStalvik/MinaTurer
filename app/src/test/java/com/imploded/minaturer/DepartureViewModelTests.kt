package com.imploded.minaturer

import com.imploded.minaturer.interfaces.SettingsInterface
import com.imploded.minaturer.interfaces.WebServiceInterface
import com.imploded.minaturer.model.*
import com.imploded.minaturer.utils.AppConstants
import com.imploded.minaturer.viewmodel.DeparturesViewModel
import junit.framework.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import org.mockito.Mockito.`when`

@Suppress("IllegalIdentifier")
class DepartureViewModelTests {

    private lateinit var viewModel: DeparturesViewModel
    private lateinit var mockSettings: SettingsInterface
    private lateinit var webservice: WebServiceInterface
    private lateinit var departures: DepartureContainer
    private val stopId = "12345"

    @Before
    fun initTests() {
        departures = DepartureContainer(DepartureBoard(
                "",
                "",
                listOf(
                        Departure(
                                "Rosa express",
                                "ROSA",
                                "bus",
                                "Nordstan",
                                "12345",
                                "17:00",
                                "2018-01-07",
                                "23456",
                                "Gerrebacka",
                                "D",
                                "",
                                "",
                                "#e89dc0",
                                "#ffffff",
                                "wheelChair",
                                catOutCode = AppConstants.Bus
                        ),
                        Departure(
                                "Rosa express",
                                "ROSA",
                                "bus",
                                "Nordstan",
                                "12345",
                                "17:05",
                                "2018-01-07",
                                "23456",
                                "Billdal",
                                "B",
                                "",
                                "",
                                "#e89dc0",
                                "#ffffff",
                                "wheelChair",
                                catOutCode = AppConstants.Bus
                        ),
                        Departure(
                                "Buss 58",
                                "58",
                                "bus",
                                "Nordstan",
                                "12345",
                                "17:32",
                                "2018-01-07",
                                "23456",
                                "Eriksberg",
                                "C",
                                "",
                                "",
                                "#00A5DC",
                                "#ffffff",
                                "wheelChair",
                                catOutCode = AppConstants.Bus
                        )
                )
        ))
        webservice = Mockito.mock(WebServiceInterface::class.java)
        `when`(webservice.getDeparturesTl(ArgumentMatchers.anyString(), ArgumentMatchers.anyInt())).thenReturn(departures)

        mockSettings = Mockito.mock(SettingsInterface::class.java)
        viewModel = DeparturesViewModel(webservice, mockSettings)
        viewModel.setStopId(stopId)
        FilteredDepartures.setTestData(hashMapOf())
    }

    @Test
    fun `When fetching departures with no active filters then three departures shall be returned`(){
        viewModel.generateFilteredDepartures(departures)
        assertTrue(viewModel.uiDepartures.size == 3)
    }

    @Test
    fun `When fetching departures and a filter is applies then only one departure shall be returned`(){
        // Cannot mock a final class properly therefore use test method
        val filterData: HashMap<String, ArrayList<FilteredDeparture>> = hashMapOf(
                "12345" to arrayListOf(
                FilteredDeparture("ROSA", "Gerrebacka"),
                FilteredDeparture("ROSA", "Billdal")
        ))

        FilteredDepartures.setTestData(filterData)

        viewModel.generateFilteredDepartures(departures)
        assertTrue(viewModel.uiDepartures.size == 1)
    }

    @Test
    fun `When selecting all items ensure that three items are selected`() {
        viewModel.generateFilteredDepartures(departures)
        viewModel.selectNone()
        viewModel.selectAll()
        val count = viewModel.uiDepartures.count { d -> d.checked }
        assertTrue(count == 3)
    }

    @Test
    fun `When having all items selected and selecting no items ensure that no items are selected`() {
        viewModel.generateFilteredDepartures(departures)
        viewModel.selectAll()
        viewModel.selectNone()
        val count = viewModel.uiDepartures.count { d -> d.checked }
        assertTrue(count == 0)
    }

    @Test
    fun `When applying filters to a list with three items ensure that only one is returned and filters are saved`() {
        viewModel.generateFilteredDepartures(departures)
        viewModel.uiDepartures[0].checked = true
        viewModel.uiDepartures[1].checked = false
        viewModel.uiDepartures[2].checked = false
        viewModel.applyFilters()
        val filterList = FilteredDepartures.filterListForStop(stopId)
        assertTrue(filterList.count() == 2)
        assertTrue((filterList[0].direction == "Billdal"))
        assertTrue((filterList[1].direction == "Eriksberg"))
    }
}