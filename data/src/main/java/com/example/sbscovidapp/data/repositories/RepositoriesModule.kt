package com.example.sbscovidapp.data.repositories

import com.example.sbscovidapp.domain.repository.CountryDataRepository
import com.example.sbscovidapp.domain.repository.GlobalDataRepository
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
    fun provideCountryDataRepository(): CountryDataRepository =
        CountryDataSource()

    @Singleton
    @Provides
    fun provideGlobalDataRepository(): GlobalDataRepository =
        GlobalDataSource()
}