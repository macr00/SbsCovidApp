package com.example.sbscovidapp.domain.interactor

import com.example.sbscovidapp.domain.model.CovidStats
import com.example.sbscovidapp.domain.repository.CovidDataRepository
import javax.inject.Inject

class GetCovidStats
@Inject constructor(
    private val repository: CovidDataRepository,
) : ResultInteractor<GetCovidStats.Params, CovidStats>() {

    override suspend fun doWork(params: Params): CovidStats =
        repository.getGlobalStats(params.isoCode)

    data class Params(val isoCode: String? = null)
}