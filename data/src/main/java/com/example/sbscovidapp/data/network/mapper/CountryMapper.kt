package com.example.sbscovidapp.data.network.mapper

import com.example.sbscovidapp.data.network.model.CountryEntity
import com.example.sbscovidapp.domain.model.Country
import javax.inject.Inject

class CountryMapper
@Inject constructor(): EntityMapper<CountryEntity, Country> {
    override fun map(entity: CountryEntity): Country =
        Country(
            iso = entity.iso,
            name = entity.name,
        )
}