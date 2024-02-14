package com.example.sbscovidapp.data.network.model

import com.squareup.moshi.Json


data class CovidStatsResponse(
    val data: CovidStatsEntity
)

data class CovidStatsEntity(
    @field:Json(name = "date") val date: String,
    @field:Json(name = "last_update") val lastUpdate: String,
    @field:Json(name = "confirmed") val confirmed: Long = 0,
    @field:Json(name = "confirmed_diff") val confirmedDiff: Long = 0,
    @field:Json(name = "deaths") val deaths: Long = 0,
    @field:Json(name = "deaths_diff") val deathsDiff: Long = 0,
    @field:Json(name = "recovered") val recovered: Long = 0,
    @field:Json(name = "recovered_diff") val recoveredDiff: Long = 0,
    @field:Json(name = "active") val active: Long = 0,
    @field:Json(name = "active_diff") val activeDiff: Long = 0,
    @field:Json(name = "fatality_rate") val fatalityRate: Double = 0.0,
)
