package com.example.sbscovidapp.domain.interactor

import com.example.sbscovidapp.domain.model.CovidStats
import com.example.sbscovidapp.domain.repository.GlobalDataRepository
import javax.inject.Inject

class GetGlobalStats
@Inject constructor(
    private val repository: GlobalDataRepository,
) : ResultInteractor<Unit, CovidStats>() {

    override suspend fun doWork(params: Unit): CovidStats =
        repository.getGlobalStats()
}