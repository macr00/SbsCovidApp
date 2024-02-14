package com.example.sbscovidapp.domain.interactor

import com.example.sbscovidapp.domain.model.CovidStats
import com.example.sbscovidapp.domain.repository.CountryDataRepository
import javax.inject.Inject

class GetCountryStats
@Inject constructor(
    private val repository: CountryDataRepository
) : ResultInteractor<GetCountryStats.Params, CovidStats>() {

    override suspend fun doWork(params: Params): CovidStats {
        // TODO review this - perhaps use a when statement
        if (params.isoCode.isBlank())
            throw Exception("Invalid ISO code")
        return repository.getCountryStats(params.isoCode)
    }

    data class Params(val isoCode: String)
}