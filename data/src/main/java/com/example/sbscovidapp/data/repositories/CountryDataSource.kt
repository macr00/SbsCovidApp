package com.example.sbscovidapp.data.repositories

import com.example.sbscovidapp.data.network.CountryDataService
import com.example.sbscovidapp.domain.model.Country
import com.example.sbscovidapp.domain.model.CovidStats
import com.example.sbscovidapp.domain.repository.CountryDataRepository

class CountryDataSource(
    //private val service: CountryDataService
) : CountryDataRepository {

    override suspend fun getCountryList(): List<Country> =
        //service.getCountries().data
        //Result.success(
            listOf(
                Country("AUS", "Australia"),
                Country("USA", "United States"),
                Country("IRN", "Iran")
            )
        //)

    override suspend fun getCountryStats(isoCode: String): CovidStats =
        //service.getCountryStats(isoCode).data
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
}