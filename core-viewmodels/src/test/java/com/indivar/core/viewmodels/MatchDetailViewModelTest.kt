package com.indivar.core.viewmodels

import com.indivar.core.MainCoroutineRule
import com.indivar.core.Response
import com.indivar.models.Match
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class MatchDetailViewModelTest {
    private lateinit var useCase: PullMatchDetailsUseCase
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule(/*dispatcher = UnconfinedTestDispatcher()*/)
    @Before
    fun init() {
        val match: Response<Match> = Response.Success<Match>(
            data = Match(
                id = 100,
                seriesId = 1001,
                matchDates = null,
                title = "A good match",
                homeTeam = null,
                awayTeams = emptyList(),
                result = false,
                tossResult = null,
                homeTeamScores = null,
                awayTeamScores = null,
                matchOfficials = null,
                scoreCard = null,
            )
        )

        useCase = mockk(relaxed = true) {
            coEvery { this@mockk.trigger(any()) }.returns(match) // this will not work
        }

    }

    @Test
    fun addition_isCorrect() {
        runTest {
            val viewModel = MatchDetailViewModel(
                useCase
            )
            lateinit var state: MatchViewState
            viewModel.effects.take(1).collectLatest {
                it.forEach { effect ->
                    if (effect is MatchDetailsEffect.Ready) {
                        effect.triggerFetch.invoke(1002)
                    }
                }
            }
            viewModel.viewState.take(1).collectLatest {
                state = it
            }
            assertTrue(!state.showLoading)
        }
    }
}