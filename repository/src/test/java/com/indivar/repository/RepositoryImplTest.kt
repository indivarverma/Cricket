package com.indivar.repository

import com.indivar.common.api.NetworkApi
import com.indivar.domain.repo.RepositoryImpl
import com.indivar.domain.repo.match.details.MatchDetail
import com.indivar.models.match.Match
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class RepositoryImplTest {
    private lateinit var networkApi: NetworkApi
    private lateinit var matchDetail: MatchDetail
    /*@ExperimentalCoroutinesApi*/
    /*@get:Rule
    var mainCoroutineRule = MainCoroutineRule()*/

    @Before
    fun init() {
        matchDetail = FakeData.fakeMatchDetail1
        networkApi = mockk(relaxed = true) {
            coEvery { getMatchDetails(any()) }.coAnswers {
                delay(2000)
                matchDetail
            }
        }

    }

    @Test
    fun `verify Match object is pulled correctly`() {
        runTest {
            val testDispatcher = StandardTestDispatcher(testScheduler)
            val repository = RepositoryImpl(
                networkApi,
                testDispatcher
            )

            val match = repository.pullMatchDetails(1002)

            Assert.assertNotNull(match)
            Assert.assertEquals(match?.id, matchDetail.results?.fixture?.id)
            Assert.assertEquals(match?.seriesId, matchDetail.results?.fixture?.series_id)
            coVerify { networkApi.getMatchDetails(1002) }


        }
    }

    @Test
    fun `verify Match object is not pulled on delay`() {
        var match: Match? = null
        val (scheduler, job) = runEagerly {dispatcher ->
            coEvery { networkApi.getMatchDetails(any()) }.coAnswers {
                delay(6000)
                matchDetail
            }
            val repository = RepositoryImpl(
                networkApi,
                dispatcher
            )
            match = repository.pullMatchDetails(1002)
        }

        Assert.assertNull(match)
        coVerify { networkApi.getMatchDetails(1002) }
    }
}

fun runEagerly(code: suspend (CoroutineDispatcher) -> Unit): Pair<TestCoroutineScheduler, Job> {
    val testDispatcher = UnconfinedTestDispatcher()
    val job = TestScope(testDispatcher).launch { code(testDispatcher) }
    return testDispatcher.scheduler to job
}

@ExperimentalCoroutinesApi
class MainCoroutineRule(val dispatcher: TestDispatcher = StandardTestDispatcher()) :
    TestWatcher() {

    override fun starting(description: Description?) {
        super.starting(description)
        Dispatchers.setMain(dispatcher)
    }

    override fun finished(description: Description?) {
        super.finished(description)
        Dispatchers.resetMain()
    }
}