package com.example.sbscovidapp.stats

import com.example.sbscovidapp.domain.interactor.GetRegionList.Companion.DefaultRegionList
import com.example.sbscovidapp.domain.model.Region
import com.example.sbscovidapp.domain.model.CovidStats
import com.example.sbscovidapp.extensions.uiFormatted

data class CovidStatsViewState(
    private val covidStatsResult: Result<CovidStats>? = null,
    val regionListResult: Result<List<Region>>? = null,
    val region: Region,
    val isStatsLoading: Boolean = false,
    val isRegionListLoading: Boolean = false,
) {

    internal val covidStats: CovidStats =
        covidStatsResult?.getOrNull() ?: CovidStats.Empty

    internal val regionList: List<Region> =
        regionListResult?.getOrNull() ?: DefaultRegionList

    internal val showCovidStatsError: Boolean =
        !isStatsLoading && covidStatsResult?.isFailure == true

    internal val showRegionListError: Boolean =
        !isRegionListLoading && regionListResult?.isFailure == true

    companion object {
        val Initial = CovidStatsViewState(
            covidStatsResult = null,
            region = Region.Global,
        )
    }
}
