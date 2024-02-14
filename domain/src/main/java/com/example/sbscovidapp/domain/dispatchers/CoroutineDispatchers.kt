package com.example.sbscovidapp.domain.dispatchers

import kotlinx.coroutines.CoroutineDispatcher

data class CoroutineDispatchers(
    private val main: CoroutineDispatcher,
    private val io: CoroutineDispatcher,
    private val computation: CoroutineDispatcher,
)