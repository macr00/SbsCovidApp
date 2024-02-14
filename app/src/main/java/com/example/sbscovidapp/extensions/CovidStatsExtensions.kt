package com.example.sbscovidapp.extensions

import com.example.sbscovidapp.domain.model.CovidStats

fun CovidStats.readableData(): List<Pair<String, String>> =
    listOf(
        Pair("Confirmed cases", confirmed.toString()),
        Pair("Deaths", deaths.toString()),
        Pair("Fatality Rate", fatalityRate.toPercentage())
    )

fun Double.toPercentage() = (this * 100).toString().plus("%")
