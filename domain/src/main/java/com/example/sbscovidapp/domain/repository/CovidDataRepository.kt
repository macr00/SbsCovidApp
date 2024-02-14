package com.example.sbscovidapp.domain.repository

import com.example.sbscovidapp.domain.model.Country
import com.example.sbscovidapp.domain.model.CovidStats

interface CovidDataRepository {
    suspend fun getGlobalStats(iso: String? = null): CovidStats
    suspend fun getCountryList(): List<Country>
}