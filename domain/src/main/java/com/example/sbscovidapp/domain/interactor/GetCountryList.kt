package com.example.sbscovidapp.domain.interactor

import com.example.sbscovidapp.domain.model.Country
import com.example.sbscovidapp.domain.repository.CountryDataRepository
import javax.inject.Inject

class GetCountryList
@Inject constructor(
    private val repository: CountryDataRepository
) : ResultInteractor<Unit, List<Country>>() {

    override suspend fun doWork(params: Unit): List<Country> =
        repository.getCountryList()
}