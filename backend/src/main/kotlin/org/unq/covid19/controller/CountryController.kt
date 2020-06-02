package org.unq.covid19.controller

import com.github.kittinunf.fuel.Fuel
import io.javalin.http.Context
import org.unq.covid19.mapper.CountryMapper
import org.unq.covid19.model.Country

class CountryController() {
    fun getLastData(ctx: Context) {
        val listDataCountry = getDataCountry(ctx)
        val lastCountry = listDataCountry[listDataCountry.lastIndex]
        ctx.json(lastCountry)
    }

    fun getChartLinesData(ctx: Context) {
        val listDataCountry = getDataCountry(ctx)
        val lineOfDeaths = listDataCountry.map { arrayOf(it.Date, it.Deaths) }
        val lineOfConfirm = listDataCountry.map { arrayOf(it.Date, it.Confirmed) }
        val lineOfActive = listDataCountry.map { arrayOf(it.Date, it.Active) }
        val lineOfRecovered = listDataCountry.map { arrayOf(it.Date, it.Recovered) }
        ctx.json(
            mapOf(
                "lineOfDeaths" to lineOfDeaths,
                "lineOfConfirm" to lineOfConfirm,
                "lineOfActive" to lineOfActive,
                "lineOfRecovered" to lineOfRecovered
            )
        )
    }

    private fun getDataCountry(ctx: Context): Array<Country> {
        val country = ctx.bodyValidator<CountryMapper>().check({
            it.country!!.isNotEmpty()
        }, "Invalid body: Country should not be null").get()

        // request to covid-19-fastest
        val (_, response, result) = Fuel.get("/dayone/country/${country.country}")
            // convert to country
            .responseObject(Country.Deserializer())
        // list of countrys
        return result.component1()!!
    }
}
