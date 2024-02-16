package com.example.sbscovidapp.domain.interactor

import com.example.sbscovidapp.domain.model.Region
import com.example.sbscovidapp.domain.repository.CovidDataRepository
import javax.inject.Inject

class GetRegionList
@Inject constructor(
    private val repository: CovidDataRepository
) : ResultInteractor<Unit, List<Region>>() {

    override suspend fun doWork(params: Unit): List<Region> =
        repository.getRegionList()
}