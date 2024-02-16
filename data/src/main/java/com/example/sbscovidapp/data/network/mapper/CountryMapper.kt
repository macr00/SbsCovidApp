package com.example.sbscovidapp.data.network.mapper

import com.example.sbscovidapp.data.network.model.RegionEntity
import com.example.sbscovidapp.domain.model.Region
import javax.inject.Inject

class CountryMapper
@Inject constructor(): EntityMapper<RegionEntity, Region> {
    override fun map(entity: RegionEntity): Region =
        Region(
            iso = entity.iso,
            name = entity.name,
        )
}