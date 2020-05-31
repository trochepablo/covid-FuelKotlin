package org.unq.covid19

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelManager
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder
import io.javalin.apibuilder.ApiBuilder.path
import io.javalin.core.util.RouteOverviewPlugin
import org.unq.covid19.controller.CountryController
import org.unq.covid19.model.Country
import java.time.*
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
        app.start(port)

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
//        val userController = UserController(unqflix, tokenJWT)
//        val bannerController = BannerController(unqflix)
//        val searchController = SearchController(unqflix)

        app.routes {
            path("getLastData") {
                ApiBuilder.get(countryController::getLastData)
            }
            path("getChartLinesData") {
                ApiBuilder.get(countryController::getChartLinesData)
            }
//            path("register") {
//                ApiBuilder.post(userController::createUser, mutableSetOf<Role>(Roles.ANYONE))
//            }
//            path("login") {
//                ApiBuilder.post(userController::loginUser, mutableSetOf<Role>(Roles.ANYONE))
//            }
//            path("user") {
//                path(":id") {
//                    ApiBuilder.get(userController::getUser, mutableSetOf<Role>(Roles.USER))
//                }
//                path("lastSeen") {
//                    ApiBuilder.post(userController::lastSeen, mutableSetOf<Role>(Roles.USER))
//                }
//                path("fav") {
//                    path(":contentId") {
//                        ApiBuilder.post(userController::addDelFavorite, mutableSetOf<Role>(Roles.USER))
//                    }
//                }
//            }
//            path("content") {
//                ApiBuilder.get(contentController::getContent, mutableSetOf<Role>(Roles.USER))
//                path(":contentId") {
//                    ApiBuilder.get(contentController::getContentById, mutableSetOf<Role>(Roles.USER))
//                }
//            }
//            path("banners") {
//                ApiBuilder.get(bannerController::getBanners, mutableSetOf<Role>(Roles.USER))
//            }
//            path("search") {
//                ApiBuilder.get(searchController::searchByText, mutableSetOf<Role>(Roles.USER))
//            }
        }
    }

    private fun setException(app: Javalin) {
        app.error(500, "html") { ctx ->
            ctx.html("Internal error")
        }
//        app.exception(UserNotFoundException::class.java) { e, ctx ->
//            ctx.status(404)
//            ctx.json(
//                mapOf(
//                    "message" to e.toString()
//                )
//            )
//        }
//        app.exception(ContentNotFoundException::class.java) { e, ctx ->
//            ctx.status(404)
//            ctx.json(mapOf("message" to e.toString()))
//        }
//        app.exception(UsernameExistException::class.java) { e, ctx ->
//            ctx.status(400)
//            ctx.json(
//                mapOf(
//                    "message" to e.toString()
//                )
//            )
//        }
//        app.exception(NotNullOrEmptyTextParamException::class.java) { e, ctx ->
//            ctx.status(400)
//            ctx.json(
//                mapOf(
//                    "message" to e.toString()
//                )
//            )
//        }
//        app.exception(Exception::class.java) { e, ctx ->
//            ctx.status(500)
//            ctx.json(
//                mapOf(
//                    "message" to e.toString()
//                )
//            )
//        }
    }
}

fun main() {
    Covid19API(7004).init()
}
