package com.example.sbscovidapp.data.network

import com.example.sbscovidapp.data.network.model.CountriesResponse
import com.example.sbscovidapp.data.network.model.CovidStatsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface CovidApiService {
    @GET("reports/total")
    suspend fun getGlobalStats(@Query("iso") iso: String? = null): CovidStatsResponse
    @GET("regions")
    suspend fun getCountries(): CountriesResponse
}