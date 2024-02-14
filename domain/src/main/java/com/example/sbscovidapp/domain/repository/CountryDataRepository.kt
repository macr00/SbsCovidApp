package com.example.sbscovidapp.domain.repository

import com.example.sbscovidapp.domain.model.Country
import com.example.sbscovidapp.domain.model.CovidStats

interface CountryDataRepository {
    suspend fun getCountryList(): List<Country>
    suspend fun getCountryStats(isoCode: String): CovidStats
}