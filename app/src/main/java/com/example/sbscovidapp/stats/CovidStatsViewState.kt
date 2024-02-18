package com.example.sbscovidapp.stats

import com.example.sbscovidapp.domain.interactor.GetRegionList.Companion.DefaultRegionList
import com.example.sbscovidapp.domain.model.Region
import com.example.sbscovidapp.domain.model.CovidStats

data class CovidStatsViewState(
    val covidStatsResult: Result<CovidStats>? = null,
    val region: Region,
    val regionListResult: Result<List<Region>>? = null,
    val isStatsLoading: Boolean = false,
    val isRegionListLoading: Boolean = false,
) {

    val covidStats: CovidStats = covidStatsResult?.getOrNull() ?: CovidStats.Empty

    val regionList: List<Region> = regionListResult?.getOrNull() ?: DefaultRegionList

    val showCovidStatsError: Boolean
        get() = !isStatsLoading && covidStatsResult?.isFailure == true

    val showRegionListError: Boolean
        get() = !isRegionListLoading && regionListResult?.isFailure == true

    companion object {
        val Initial = CovidStatsViewState(
            covidStatsResult = null,
            region = Region.Global,
        )
    }
}
