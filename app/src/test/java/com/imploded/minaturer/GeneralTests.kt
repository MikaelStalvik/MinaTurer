package com.imploded.minaturer


import com.imploded.minaturer.model.TlDeparture
import com.imploded.minaturer.model.TlProduct
import com.imploded.minaturer.utils.*
import junit.framework.Assert.*
import org.junit.Test

@Suppress("IllegalIdentifier")
class GeneralTests {

    @Test
    fun `When having two dates with one hour differance check that timeDifferance results in 3600 seconds`() {
        val sourceDate = "2018-01-06"
        val sourceTime = "16:05"
        val destDate = "2018-01-06"
        val destTime = "17:05"
        val diff = timeDifference(sourceDate, sourceTime, destDate, destTime)
        assertTrue(diff == 3600)
    }

    @Test
    fun `When having two dates which differes in two hours and spans over a new day, timeDifferance should return 7200 seconds`() {
        val sourceDate = "2018-01-06"
        val sourceTime = "23:00"
        val destDate = "2018-01-07"
        val destTime = "01:00"
        val diff = timeDifference(sourceDate, sourceTime, destDate, destTime)
        assertTrue(diff == 7200)
    }

    @Test
    fun `When a productName contains Spårväg isTram should be true`() {
        val productName = "Länstrafik - Spårväg 1"
        assertTrue(productName.isTram())
    }

    @Test
    fun `When a product is a tram and from Västtrafik, ensure that bgColor is the expected color`() {
        val departure = TlDeparture(
                product = TlProduct("Länstrafik - Spårväg 1", "1", OPERATOR_VASTTRAFIK)
        )
        assertEquals("#ffffff", departure.bgColor())
    }
    @Test
    fun `When a product is a tram and not from Västtrafik, ensure that bgColor is the expected color`() {
        val departure = TlDeparture(
                product = TlProduct("Länstrafik - Spårväg 1", "1", OPERATOR_SL)
        )
        assertEquals("#0089ca", departure.bgColor())
    }
    @Test
    fun `When a product is a bus from Västtrafik, ensure that bgColor is the expected color`() {
        val departure = TlDeparture(
                product = TlProduct("Länstrafik - Buss 1", "1", OPERATOR_VASTTRAFIK)
        )
        assertEquals(DEFAULT_BG_COLOR, departure.bgColor())
    }

    @Test
    fun `When filter is subway, tram and bus then result shall be true`() {
        val test = AppConstants.Subway + AppConstants.Tram + AppConstants.Bus
        val isSubway = test.isSubway()
        val isTram = test.isTram()
        val isBus = test.isBus()
        assertTrue(isSubway && isTram && isBus)
    }

    @Test
    fun `When filter is subway, tram and bus check that regional train is not included`() {
        val test = AppConstants.Subway + AppConstants.Tram + AppConstants.Bus
        val isRegionalTrain = test.isRegionalTrain()
        assertFalse(isRegionalTrain)
    }

}