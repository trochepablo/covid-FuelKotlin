package org.unq.covid19

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelManager
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder
import io.javalin.apibuilder.ApiBuilder.path
import io.javalin.core.util.RouteOverviewPlugin
import org.unq.covid19.controller.CountryController
import org.unq.covid19.model.Country
import java.time.LocalDate
import java.time.ZoneOffset
import java.util.*
import kotlin.concurrent.schedule


class Covid19API(private val port: Int) {
    // variable global para acceder al listado
    companion object {
        var argentinaHistory: Array<Country> = arrayOf()
    }

    val timer = Timer("schedule", true);

    fun init(): Javalin {

        // declaracion de instance para fuel (The easiest HTTP networking library for Kotlin/Android)
        FuelManager.instance.basePath = "https://covid-19-fastest-update.p.rapidapi.com"
        FuelManager.instance.baseHeaders = mapOf(
            "x-rapidapi-host" to "covid-19-fastest-update.p.rapidapi.com",
            "x-rapidapi-key" to "bc915823d8mshc08e34940d1ec72p191869jsn08922a187a7d",
            "useQueryString" to "true"
        )

        // config javalin API back end ApiRestFul
        val app = Javalin.create {
            it.defaultContentType = "application/json"
            it.registerPlugin(RouteOverviewPlugin("/routes"))
            it.enableCorsForAllOrigins()
        }

        // set port
        app.start(getHerokuAssignedPort())

        // routes
        setRoutes(app)

        // set exception
        setException(app)

        // ejecucion periodica para actualización de datos
        timer.schedule(0, 50000000) {

            // set date request
            val desde = "2020-03-03T00:00:00Z"
            val hasta = LocalDate.now(ZoneOffset.UTC)

            // request to covid-19-fastest
            val (_, response, result) = Fuel.get(
                "/country/argentina?from=$desde&to=$hasta"
            ).also { println(it.url) }
                // convert to country
                .responseObject(Country.Deserializer())
            // list of countrys
            argentinaHistory = result.component1()!!
            println(argentinaHistory.size)
        }
        return app
    }

    private fun setRoutes(app: Javalin) {
        val countryController = CountryController()

        app.routes {
            path("getLastData") {
                ApiBuilder.get(countryController::getLastData)
            }
            path("getChartLinesData") {
                ApiBuilder.get(countryController::getChartLinesData)
            }
        }
    }

    private fun setException(app: Javalin) {
        app.error(500, "html") { ctx ->
            ctx.html("Internal error")
        }
    }
}

fun main(args: Array<String>) {
    Covid19API(getHerokuAssignedPort()).init()
}

private fun getHerokuAssignedPort(): Int {
    val herokuPort = System.getenv("PORT")
    return herokuPort?.toInt() ?: 7000
}