package com.example.sbscovidapp.domain.repository

import com.example.sbscovidapp.domain.model.Region
import com.example.sbscovidapp.domain.model.CovidStats

interface CovidDataRepository {
    suspend fun getCovidStats(iso: String? = null): CovidStats
    suspend fun getRegionList(): List<Region>
}