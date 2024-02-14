package com.example.sbscovidapp.data.network.mapper

import com.example.sbscovidapp.data.network.model.CovidStatsEntity
import com.example.sbscovidapp.domain.model.CovidStats
import javax.inject.Inject

class CovidStatsMapper
@Inject constructor(): EntityMapper<CovidStatsEntity, CovidStats> {
    override fun map(entity: CovidStatsEntity) = CovidStats(
        date = entity.date,
        lastUpdate = entity.lastUpdate,
        confirmed = entity.confirmed,
        confirmedDiff = entity.confirmedDiff,
        deaths = entity.deaths,
        deathsDiff = entity.deathsDiff,
        recovered = entity.recovered,
        recoveredDiff = entity.recoveredDiff,
        active = entity.active,
        activeDiff = entity.activeDiff,
        fatalityRate = entity.fatalityRate,
    )
}