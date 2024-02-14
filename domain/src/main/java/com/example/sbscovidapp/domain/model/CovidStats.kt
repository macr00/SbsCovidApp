package com.example.sbscovidapp.domain.model

data class CovidStats(
    val date: String,
    val lastUpdate: String,
    val confirmed: Long = 0,
    val confirmedDiff: Long = 0,
    val deaths: Long = 0,
    val deathsDiff: Long = 0,
    val recovered: Long = 0,
    val recoveredDiff: Long = 0,
    val active: Long = 0,
    val activeDiff: Long = 0,
    val fatalityRate: Double = 0.0,
) {

    companion object {
        val Empty = CovidStats("", "")
    }
}
