package com.example.sbscovidapp.extensions

import com.example.sbscovidapp.domain.model.CovidStats

// TODO rename function
fun CovidStats.readableData(): List<Pair<String, String>> =
    listOf(
        Pair("Confirmed cases", confirmed.toString()),
        Pair("Deaths", deaths.toString()),
        Pair("Fatality Rate", fatalityRate.toPercentage())
    )

// TODO limit to two decimal places
fun Double.toPercentage() = (this * 100).toString().plus("%")
