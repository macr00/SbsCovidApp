package com.example.sbscovidapp.domain.interactor

import com.example.sbscovidapp.domain.model.Region
import com.example.sbscovidapp.domain.repository.CovidDataRepository
import javax.inject.Inject

class GetRegionList
@Inject constructor(
    private val repository: CovidDataRepository
) : Interactor<Unit, List<Region>>() {

    override suspend fun doWork(params: Unit): Result<List<Region>> =
        runCatching {
            DefaultRegionList + repository.getRegionList()
        }

    companion object {
        val DefaultRegionList = listOf(Region.Global)
    }
}