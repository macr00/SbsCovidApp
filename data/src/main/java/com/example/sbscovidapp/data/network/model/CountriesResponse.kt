package com.example.sbscovidapp.data.network.model

import com.squareup.moshi.Json

data class CountriesResponse(
    val data: List<CountryEntity>
)

data class CountryEntity(
    @field:Json(name = "iso") val iso: String,
    @field:Json(name = "name") val name: String,
)