package com.example.sbscovidapp.data.repositories

import com.example.sbscovidapp.data.network.CovidApiService
import com.example.sbscovidapp.data.network.mapper.CountryMapper
import com.example.sbscovidapp.data.network.mapper.CovidStatsMapper
import com.example.sbscovidapp.domain.repository.CovidDataRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoriesModule {
    @Singleton
    @Provides
    fun provideCovidDataRepository(covidApiService: CovidApiService,
                                   covidStatsMapper: CovidStatsMapper,
                                   countryMapper: CountryMapper): CovidDataRepository =
        CovidDataSource(covidApiService, covidStatsMapper, countryMapper)
}