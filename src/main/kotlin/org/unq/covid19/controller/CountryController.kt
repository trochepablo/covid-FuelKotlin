package org.unq.covid19.controller

import io.javalin.http.Context
import org.unq.covid19.Covid19API.Companion.argentinaHistory

class CountryController() {
    fun getLastData(ctx: Context){
        val lastCountry = argentinaHistory[argentinaHistory.lastIndex]
        ctx.json(lastCountry)
    }
}
