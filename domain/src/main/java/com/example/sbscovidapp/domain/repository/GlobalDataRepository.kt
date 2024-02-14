package com.example.sbscovidapp.domain.repository

import com.example.sbscovidapp.domain.model.CovidStats

interface GlobalDataRepository {
    suspend fun getGlobalStats(): CovidStats
}