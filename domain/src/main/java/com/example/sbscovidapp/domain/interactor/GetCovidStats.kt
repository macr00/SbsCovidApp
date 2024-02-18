package com.example.sbscovidapp.domain.interactor

import com.example.sbscovidapp.domain.model.CovidStats
import com.example.sbscovidapp.domain.repository.CovidDataRepository
import javax.inject.Inject

class GetCovidStats
@Inject constructor(
    private val repository: CovidDataRepository,
) : Interactor<GetCovidStats.Params, CovidStats>() {

    override suspend fun doWork(params: Params): Result<CovidStats> =
        runCatching {
            repository.getCovidStats(params.iso.ifBlank { null })
        }

    data class Params(val iso: String)
}