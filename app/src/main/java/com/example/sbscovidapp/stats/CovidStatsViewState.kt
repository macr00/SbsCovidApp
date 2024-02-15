package com.example.sbscovidapp.stats

import com.example.sbscovidapp.domain.model.CovidStats

data class CovidStatsViewState(
    val globalStats: CovidStats = CovidStats.Empty,
    val isLoading: Boolean = false,
)
