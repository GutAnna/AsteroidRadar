package com.udacity.asteroidradar.domain

import com.udacity.asteroidradar.Constants
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object DateConverter {
    fun getDateFromToday(count: Long): String {
        val date = LocalDate.now().plusDays(count)
        return date.format(DateTimeFormatter.ofPattern(Constants.API_QUERY_DATE_FORMAT))
    }

    fun getToday() = getDateFromToday(0)
}