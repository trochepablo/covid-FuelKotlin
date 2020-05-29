package org.unq.covid19

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelError
import org.junit.jupiter.api.*
import com.github.kittinunf.fuel.core.FuelManager
import org.junit.Assert.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class TestApiCOVID19 {

    @BeforeAll
    fun setUp() {

        FuelManager.instance.basePath = "https://covid-193.p.rapidapi.com"
        FuelManager.instance.baseHeaders = mapOf("x-rapidapi-host" to "covid-193.p.rapidapi.com",
            "x-rapidapi-key" to "aqui va tu token", "useQueryString" to "true")
    }

    @Test
    @Order(1)
    fun `get statistics`() {
        try {
            val (_, response, result) =
                Fuel.get("/statistics").response()
            println(response.data.toString(Charsets.UTF_8))
            assertEquals(200, response.statusCode)
        } catch (e: FuelError){
            println(e.message)
        }

    }

    @AfterAll
    fun tearDown() {
    }
}
