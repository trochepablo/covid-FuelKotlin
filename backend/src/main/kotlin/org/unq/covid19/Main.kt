package org.unq.covid19

import com.github.kittinunf.fuel.core.FuelManager
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder
import io.javalin.apibuilder.ApiBuilder.path
import io.javalin.core.util.RouteOverviewPlugin
import org.unq.covid19.controller.CountryController


class Covid19API(private val port: Int) {

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
        app.start(port)

        // routes
        setRoutes(app)

        // set exception
        setException(app)

        return app
    }

    private fun setRoutes(app: Javalin) {
        val countryController = CountryController()

        app.routes {
            path("getLastData") {
                ApiBuilder.post(countryController::getLastData)
            }
            path("getChartLinesData") {
                ApiBuilder.post(countryController::getChartLinesData)
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