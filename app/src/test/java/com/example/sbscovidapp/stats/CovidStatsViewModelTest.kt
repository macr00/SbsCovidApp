package com.example.sbscovidapp.stats

import com.example.sbscovidapp.domain.interactor.GetCovidStats
import com.example.sbscovidapp.domain.interactor.GetRegionList
import com.example.sbscovidapp.domain.interactor.GetRegionList.Companion.DefaultRegionList
import com.example.sbscovidapp.domain.interactor.invoke
import com.example.sbscovidapp.domain.model.CovidStats
import com.example.sbscovidapp.domain.model.Region
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
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

@ExperimentalCoroutinesApi
class CovidStatsViewModelTest {

    private lateinit var viewModel: CovidStatsViewModel

    private val testDispatcher = UnconfinedTestDispatcher()

    private val getCovidStatsMock = mockk<GetCovidStats>(relaxed = true)
    private val getRegionListMock = mockk<GetRegionList>(relaxed = true)

    private val covidStatsStateFlow: MutableStateFlow<Result<CovidStats>?> =
        MutableStateFlow(null)
    private val covidListLoadingFlow: MutableStateFlow<Boolean> =
        MutableStateFlow(false)
    private val regionListStateFlow: MutableStateFlow<Result<List<Region>>> =
        MutableStateFlow(Result.success(DefaultRegionList))
    private val regionListLoadingFlow: MutableStateFlow<Boolean> =
        MutableStateFlow(false)

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        coEvery { getCovidStatsMock.flow } returns covidStatsStateFlow
        coEvery { getCovidStatsMock.isLoading } returns covidListLoadingFlow
        coEvery { getRegionListMock.flow } returns regionListStateFlow
        coEvery { getRegionListMock.isLoading } returns regionListLoadingFlow

        viewModel = CovidStatsViewModel(
            getCovidStats = getCovidStatsMock,
            getRegionList = getRegionListMock
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test initial default ViewState`() = runTest {
        val first = viewModel.uiStateFlow.first()
        assertEquals(DefaultRegionList, first.regionList)
        assertEquals(CovidStats.Empty, first.covidStats)
        assertEquals(Region.Global, first.region)
        assertEquals(false, first.isStatsLoading)
        assertEquals(false, first.isRegionListLoading)
    }

    @Test
    fun `test uiState when GetCovidStats result is successful`() = runTest {
        val covidStats = CovidStats(
            date = "2024-02-15",
            lastUpdate = "2024-02-13",
            confirmed = 11399460,
            deaths = 19574,
            fatalityRate = 0.00152
        )

        val job = launch(UnconfinedTestDispatcher(testScheduler)) {
            val last = viewModel.uiStateFlow.last()
            assertEquals(covidStats, last.covidStats)
            assertEquals(false, last.showCovidStatsError)
        }

        covidStatsStateFlow.tryEmit(Result.success(covidStats))

        job.cancel()
    }

    @Test
    fun `test uiState when GetCovidStats result is failure`() = runTest {
        val job = launch(UnconfinedTestDispatcher(testScheduler)) {
            val last = viewModel.uiStateFlow.last()
            assertEquals(true, last.showCovidStatsError)
        }

        covidStatsStateFlow.tryEmit(Result.failure(Throwable()))

        job.cancel()
    }

    @Test
    fun `test uiState when GetRegionList result is successful`() = runTest {
        val regionList = listOf(Australia, Brazil, Sweden)

        val job = launch(UnconfinedTestDispatcher(testScheduler)) {
            val last = viewModel.uiStateFlow.last()
            assertEquals(regionList, last.regionList)
            assertEquals(false, last.showRegionListError)
        }

        regionListStateFlow.tryEmit(Result.success(regionList))

        job.cancel()
    }

    @Test
    fun `test uiState when GetRegionList result is failure`() = runTest {
        val job = launch(UnconfinedTestDispatcher(testScheduler)) {
            val last = viewModel.uiStateFlow.last()
            assertEquals(true, last.showRegionListError)
        }

        regionListStateFlow.tryEmit(Result.failure(Throwable()))

        job.cancel()
    }

    @Test
    fun `test getCovidStatsForRegion invokes GetCovidStats`() = runTest {
        viewModel.getCovidStatsForRegion(Brazil)

        val job = launch(UnconfinedTestDispatcher(testScheduler)) {
            coVerify(exactly = 1) { getCovidStatsMock(GetCovidStats.Params(Brazil.iso)) }
        }

        job.cancel()
    }

    @Test
    fun `test getRegionList invokes GetRegionList`() = runTest {
        viewModel.getRegionsList()

        val job = launch(UnconfinedTestDispatcher(testScheduler)) {
            coVerify(exactly = 1) { getRegionListMock.invoke() }
        }

        job.cancel()
    }
    @Test
    fun `test onRegionSelected invokes getCovidStats`() = runTest {
        viewModel.onRegionSelected(Australia)
        // Simulate re-selecting the same region
        viewModel.onRegionSelected(Australia)

        viewModel.onRegionSelected(Sweden)

        val job = launch(UnconfinedTestDispatcher(testScheduler)) {
            coVerify(exactly = 1) { getCovidStatsMock(GetCovidStats.Params(Australia.iso)) }
            coVerify(exactly = 1) { getCovidStatsMock(GetCovidStats.Params(Sweden.iso)) }
        }

        job.cancel()
    }

    companion object {
        val Australia = Region("AUS", "Australia")
        val Brazil = Region("BRA", "Brazil")
        val Sweden = Region("SWE", "Sweden")
    }
}