package com.imploded.minaturer

import com.google.gson.Gson
import com.imploded.minaturer.interfaces.SettingsInterface
import com.imploded.minaturer.model.SettingsModel
import com.imploded.minaturer.model.UiStop
import com.imploded.minaturer.utils.AppConstants
import com.imploded.minaturer.viewmodel.LandingViewModel
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`

@Suppress("IllegalIdentifier")
class LandingViewModelTests {

    private lateinit var viewModel: LandingViewModel
    private lateinit var mockSettings: SettingsInterface

    @Before
    fun init() {
        val stops = listOf(
                UiStop("Nordstan", "1", "", ""),
                UiStop("Heden", "2", "", ""),
                UiStop("Kärra", "3", "", ""),
                UiStop("Torslanda", "4", "", ""),
                UiStop("Frölunda", "5", "", ""))
        val json = Gson().toJson(stops)
        mockSettings = Mockito.mock(SettingsInterface::class.java)
        `when`(mockSettings.loadSettings()).thenReturn(
                SettingsModel(json, "", "", false, false, false, false, 0, AppConstants.Bus)
        )
        viewModel = LandingViewModel(mockSettings)
    }

    @Test
    fun `When having five stops in list and deleting item three the resulting list shall have four items with the correct order`() {
        viewModel.getStops()
        viewModel.removeStop(2)
        assertEquals(viewModel.selectedStops.count(), 4)
        assertTrue(viewModel.selectedStops[0].id == "1")
        assertTrue(viewModel.selectedStops[1].id == "2")
        assertTrue(viewModel.selectedStops[2].id == "4")
        assertTrue(viewModel.selectedStops[3].id == "5")
    }
}