package com.example.sbscovidapp.domain.interactor

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class InteractorTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test interactor result is success and loading state updates`() = runTest {
        val interactor = SuccessInteractor()
        val loadingStats = mutableListOf<Boolean>()
        val job = launch(UnconfinedTestDispatcher(testScheduler)) {
            interactor.isLoading.collectLatest {
                loadingStats.add(it)
            }
            val result = interactor.flow.last()
            assertEquals(Result.success(Any()), result)
        }

        interactor.invoke()

        job.cancel()

        assertEquals(false, loadingStats[0])
        assertEquals(true, loadingStats[1])
        assertEquals(false, loadingStats[2])
    }

    @Test
    fun `test interactor result is failure`() = runTest {
        val interactor = FailureInteractor()
        val loadingStats = mutableListOf<Boolean>()
        val job = launch(UnconfinedTestDispatcher(testScheduler)) {
            interactor.isLoading.collectLatest {
                loadingStats.add(it)
            }
            val result = interactor.flow.last()
            assertEquals(Result.failure<Any>(Throwable()), result)
        }

        interactor.invoke()

        job.cancel()

        assertEquals(false, loadingStats[0])
        assertEquals(true, loadingStats[1])
        assertEquals(false, loadingStats[2])
    }

    class SuccessInteractor : Interactor<Unit, Any>() {
        override suspend fun doWork(params: Unit): Result<Any> {
            return Result.success(Any())
        }
    }

    class FailureInteractor : Interactor<Unit, Any>() {
        override suspend fun doWork(params: Unit): Result<Any> {
            throw Exception()
        }
    }
}