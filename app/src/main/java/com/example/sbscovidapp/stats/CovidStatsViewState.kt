package com.example.sbscovidapp.stats

import com.example.sbscovidapp.domain.model.Region
import com.example.sbscovidapp.domain.model.CovidStats

data class CovidStatsViewState(
    val covidStats: CovidStats? = null,
    val region: Region,
    val regionList: List<Region>,
    val isStatsLoading: Boolean = false,
    val isRegionListLoading: Boolean = false,
) {

    val showError: Boolean
        get() = !isStatsLoading && covidStats == null

    companion object {
        val Initial = CovidStatsViewState(
            covidStats = CovidStats.Empty,
            region = DefaultGlobal,
            regionList = listOf(DefaultGlobal),
        )
    }
}
