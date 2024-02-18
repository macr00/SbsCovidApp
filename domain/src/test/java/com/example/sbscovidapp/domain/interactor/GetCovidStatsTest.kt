package com.example.sbscovidapp.domain.interactor

import com.example.sbscovidapp.domain.model.CovidStats
import com.example.sbscovidapp.domain.repository.CovidDataRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
class GetCovidStatsTest {

    private lateinit var getCovidStats: GetCovidStats

    private val testDispatcher = UnconfinedTestDispatcher()

    private val repository = mockk<CovidDataRepository>()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        getCovidStats = GetCovidStats(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test successful Result`() = runTest {
        coEvery { repository.getCovidStats(any()) } returns CovidStats.Empty

        val job = launch(UnconfinedTestDispatcher(testScheduler)) {
            getCovidStats.invoke(GetCovidStats.Params("AUS"))
            val last = getCovidStats.flow.last()
            coVerify(exactly = 1) { repository.getCovidStats("AUS") }
            assertEquals(Result.success(CovidStats.Empty), last)
        }

        job.cancel()
    }

    @Test
    fun `test failure Result`() = runTest {
        coEvery { repository.getCovidStats(any()) } throws Exception()

        val job = launch(UnconfinedTestDispatcher(testScheduler)) {
            getCovidStats.invoke(GetCovidStats.Params("AUS"))
            val last = getCovidStats.flow.last()
            coVerify(exactly = 1) { repository.getCovidStats("AUS") }
            assertEquals(Result.failure<CovidStats>(Exception()), last)
        }

        job.cancel()
    }

    @Test
    fun `test empty iso params are converted to null`() = runTest {
        coEvery { repository.getCovidStats(any()) } returns CovidStats.Empty

        val job = launch(UnconfinedTestDispatcher(testScheduler)) {
            getCovidStats.invoke(GetCovidStats.Params(""))
            coVerify(exactly = 1) { repository.getCovidStats(null) }
        }

        job.cancel()
    }
}