package org.unq.covid19.model

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.Gson
import java.util.*

data class Country(
    val Country: String,
    val CountryCode: String,
    val Province: String,
    val City: String,
    val CityCode: String,
    val Lat: String,
    val Lon: String,
    val Confirmed: Int,
    val Deaths: Int,
    val Recovered: Int,
    val Active: Int,
    // api returns date format unix
    val Date: Date
) {
    class Deserializer : ResponseDeserializable<Array<Country>> {
        override fun deserialize(content: String): Array<Country>
                = Gson().fromJson(content, Array<Country>::class.java)
    }
}
