package com.imploded.minaturer


import com.imploded.minaturer.utils.timeDifference
import junit.framework.Assert.assertTrue
import org.junit.Test

class GeneralTests {

    @Test
    fun givenTwoDates_whenItDiffersOneHour_thenResultShallBe3600seconds() {
        val sourceDate = "2018-01-06"
        val sourceTime = "16:05"
        val destDate = "2018-01-06"
        val destTime = "17:05"
        var diff = timeDifference(sourceDate, sourceTime, destDate, destTime)
        assertTrue(diff == 3600)
    }

    @Test
    fun givenTwoDates_whenItDiffersOneTwoHoursAndDateSpansOverNewDate_thenResultShallBe7200seconds() {
        val sourceDate = "2018-01-06"
        val sourceTime = "23:00"
        val destDate = "2018-01-07"
        val destTime = "01:00"
        var diff = timeDifference(sourceDate, sourceTime, destDate, destTime)
        assertTrue(diff == 7200)
    }
}