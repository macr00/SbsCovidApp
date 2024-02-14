package com.example.sbscovidapp.data.network.mapper

interface EntityMapper<in E, out T> {
    fun map(entity: E): T
}