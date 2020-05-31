package org.unq.covid19.controller

import io.javalin.http.Context
import org.unq.covid19.Covid19API.Companion.argentinaHistory
import java.sql.Timestamp

class CountryController() {
    fun getLastData(ctx: Context) {
        val lastCountry = argentinaHistory[argentinaHistory.lastIndex]
        ctx.json(lastCountry)
    }

    fun getChartLinesData(ctx: Context) {
        val lineOfDeaths = argentinaHistory.map { arrayOf(it.Date, it.Deaths) }
        val lineOfConfirm = argentinaHistory.map { arrayOf(it.Date, it.Confirmed) }
        val lineOfActive = argentinaHistory.map { arrayOf(it.Date, it.Active) }
        val lineOfRecovered = argentinaHistory.map { arrayOf(it.Date, it.Recovered) }
        ctx.json(
            mapOf(
                "lineOfDeaths" to lineOfDeaths,
                "lineOfConfirm" to lineOfConfirm,
                "lineOfActive" to lineOfActive,
                "lineOfRecovered" to lineOfRecovered
            )
        )
    }
}
