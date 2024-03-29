package com.example.sbscovidapp.domain.model

data class Region(
    val iso: String,
    val name: String
) {

    companion object {
        val Global = Region("", "Global")
    }
}