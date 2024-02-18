package com.example.sbscovidapp.domain.interactor

import android.util.Log
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.withTimeout
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

abstract class Interactor<P : Any, T> {

    private val loadingState = LoadingStateObservable()

    val isLoading = loadingState.isLoadingFlow

    protected open val sharedFlow: MutableSharedFlow<Result<T>?> =
        MutableSharedFlow(
            replay = 1,
            onBufferOverflow = BufferOverflow.DROP_OLDEST
        )

    val flow: Flow<Result<T>?> by lazy {
        sharedFlow
    }

    suspend operator fun invoke(
        params: P,
        timeout: Duration = DefaultTimeout
    ) {
        try {
           withTimeout(timeout) {
               loadingState.addLoader()
               sharedFlow.emit(doWork(params))
           }
        } catch (e: Exception) {
            sharedFlow.emit(Result.failure(e))
        } finally {
            loadingState.removeLoader()
        }
    }

    protected abstract suspend fun doWork(params: P): Result<T>

    companion object {
        internal val DefaultTimeout = 5.minutes
    }
}

suspend operator fun <R> Interactor<Unit, R>.invoke() = invoke(Unit)