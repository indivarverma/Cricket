package com.indivar.repository

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class RepositoryImplTest {
    private lateinit var networkApi: NetworkApi
    private lateinit var matchDetail: MatchDetail

    @Before
    fun init() {
        matchDetail = FakeData.fakeMatchDetail1
        networkApi = mockk(relaxed = true) {
            coEvery { getMatchDetails(any()) }.returns(matchDetail)
        }
    }

    @Test
    fun `verify Match object is pulled correctly`() {
        runTest {
            val repository = RepositoryImpl(
                networkApi,
                StandardTestDispatcher(testScheduler)
            )

            val match = repository.pullMatchDetails(1002)
            Assert.assertNotNull(match)
            Assert.assertEquals(match?.id, matchDetail.results?.fixture?.id)
            Assert.assertEquals(match?.seriesId, matchDetail.results?.fixture?.series_id)
            coVerify { networkApi.getMatchDetails(1002) }


        }
    }
}