package com.example.sbscovidapp.extensions

import com.example.sbscovidapp.domain.model.CovidStats
import java.text.DecimalFormat

fun CovidStats.uiFormatted(): List<Pair<String, String>> =
    listOf(
        Pair("Confirmed cases", confirmed.toString()),
        Pair("Deaths", deaths.toString()),
        Pair("Fatality Rate", fatalityRate.toPercentage())
    )

fun Double.toPercentage(): String {
    return DecimalFormat("##.##%").format(this)
}
