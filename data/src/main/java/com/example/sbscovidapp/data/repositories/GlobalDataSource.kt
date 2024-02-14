package com.example.sbscovidapp.data.repositories

import com.example.sbscovidapp.data.network.GlobalDataService
import com.example.sbscovidapp.domain.model.CovidStats
import com.example.sbscovidapp.domain.repository.GlobalDataRepository

class GlobalDataSource(
    //private val service: GlobalDataService
) : GlobalDataRepository {

    override suspend fun getGlobalStats(): CovidStats =
        //Result.success(
            CovidStats(
                date = "2023-03-09",
                lastUpdate = "2023-03-10 04:21:03",
                confirmed = 676544789,
                confirmedDiff = 194101,
                deaths = 6881737,
                deathsDiff = 1854,
                recovered = 0,
                recoveredDiff = 0,
                active = 669663052,
                activeDiff = 192247,
                fatalityRate = 0.0102
            )
        //)
    //service.getGlobalStats().data
}