package com.example.sbscovidapp

import com.example.sbscovidapp.domain.model.CovidStats

data class GlobalViewState(
    val globalStats: CovidStats = CovidStats.Empty,
    val isLoading: Boolean = false,
)
