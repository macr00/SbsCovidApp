package com.example.sbscovidapp.extensions

import com.example.sbscovidapp.domain.model.CovidStats
import java.math.RoundingMode

fun CovidStats.uiFormatted(): List<Pair<String, String>> =
    listOf(
        Pair("Confirmed cases", confirmed.toString()),
        Pair("Deaths", deaths.toString()),
        Pair("Fatality Rate", fatalityRate.toPercentage())
    )

fun Double.toPercentage() = this.toBigDecimal()
    .setScale(4, RoundingMode.HALF_UP)
    .let {
        (it.toDouble() * 100).toString().plus("%")
    }
