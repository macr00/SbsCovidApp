package com.example.sbscovidapp.data.repositories

import com.example.sbscovidapp.data.network.CovidApiService
import com.example.sbscovidapp.data.network.mapper.CountryMapper
import com.example.sbscovidapp.data.network.mapper.CovidStatsMapper
import com.example.sbscovidapp.domain.model.Country
import com.example.sbscovidapp.domain.model.CovidStats
import com.example.sbscovidapp.domain.repository.CovidDataRepository

class CovidDataSource(
    private val covidApiService: CovidApiService,
    private val covidStatsMapper: CovidStatsMapper,
    private val countryMapper: CountryMapper
) : CovidDataRepository {

    override suspend fun getGlobalStats(iso: String?): CovidStats =
     covidApiService.getGlobalStats(iso)
            .let { covidStatsMapper.map(it.data) }

    override suspend fun getCountryList(): List<Country> =
        covidApiService.getCountries().data
            .map { countryMapper.map(it) }
}