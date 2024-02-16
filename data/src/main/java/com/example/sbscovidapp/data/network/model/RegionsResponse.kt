package com.example.sbscovidapp.data.network.model

import com.squareup.moshi.Json

data class RegionsResponse(
    val data: List<RegionEntity>
)

data class RegionEntity(
    @field:Json(name = "iso") val iso: String,
    @field:Json(name = "name") val name: String,
)