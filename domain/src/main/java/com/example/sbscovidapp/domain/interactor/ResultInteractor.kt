package com.example.sbscovidapp.domain.interactor

import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withTimeout
import java.util.concurrent.atomic.AtomicInteger
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

abstract class ResultInteractor<in P, R> {
    private val count = AtomicInteger(0)
    private val loadingState = MutableStateFlow(count.get())

    val loadingStateFlow: Flow<Boolean> = loadingState
        .map { it > 0 }
        .distinctUntilChanged()

    private fun addLoader() {
        loadingState.value = count.incrementAndGet()
    }

    private fun removeLoader() {
        loadingState.value = count.decrementAndGet()
    }

    suspend operator fun invoke(
        params: P,
        timeout: Duration = DefaultTimeout,
    ): Result<R> = try {
        addLoader()
        //delay(5.seconds)
        runCatching {
            withTimeout(timeout) {
                doWork(params)
            }
        }
    } finally {
        removeLoader()
    }

    protected abstract suspend fun doWork(params: P): R

    companion object {
        internal val DefaultTimeout = 5.minutes
    }
}

suspend operator fun <R> ResultInteractor<Unit, R>.invoke(
    timeout: Duration = ResultInteractor.DefaultTimeout,
) = invoke(Unit, timeout)


abstract class SuspendingWorkInteractor<P, T> {

    operator fun invoke(params: P): Flow<Result<T>> = flow {
        try {
            withTimeout(DefaultTimeout) {
                emit(doWork(params))
            }
        } catch (t: TimeoutCancellationException) {
            emit(Result.failure(t))
        }
    }

    abstract suspend fun doWork(params: P): Result<T>

    companion object {
        internal val DefaultTimeout = 5.minutes
    }
}