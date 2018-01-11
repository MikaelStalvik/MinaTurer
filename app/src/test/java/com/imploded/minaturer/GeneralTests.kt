package com.imploded.minaturer


import com.imploded.minaturer.utils.timeDifference
import junit.framework.Assert.assertTrue
import org.junit.Test

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
}