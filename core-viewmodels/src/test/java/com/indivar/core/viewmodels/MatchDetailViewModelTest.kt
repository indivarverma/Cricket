package com.indivar.core.viewmodels

import androidx.lifecycle.SavedStateHandle
import com.indivar.core.MainCoroutineRule
import com.indivar.core.data.Response
import com.indivar.core.match.detail.domain.usecase.PullMatchDetailsUseCase
import com.indivar.core.match.detail.domain.viewmodel.MatchDetailViewModel
import com.indivar.core.match.detail.domain.viewmodel.MatchInformation
import com.indivar.core.match.detail.domain.viewmodel.MatchViewState
import com.indivar.models.Team
import com.indivar.models.match.Match
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
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
    private lateinit var savedStateHandle: SavedStateHandle

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun init() {
        val match: Response<Match> = Response.Success<Match>(
            data = Match(
                id = 100,
                seriesId = 1001,
                matchDates = null,
                title = "A good match",
                homeTeam = Team(id = 345, name = "Team A", code = "A", players = emptyList()),
                awayTeams = emptyList(),
                result = false,
                tossResult = null,
                homeTeamScores = null,
                awayTeamScores = null,
                matchOfficials = null,
                scoreCard = null,
            )
        )
        savedStateHandle = mockk(relaxed = true) {
            every { get<Int>("matchId") }.returns(1002)
        }
        useCase = mockk(relaxed = true) {
            coEvery { this@mockk.trigger(any()) }.returns(match) // this will not work
        }

    }

    @Test
    fun addition_isCorrect() {
        runTest {
            val viewModel = MatchDetailViewModel(
                savedStateHandle,
                useCase
            )
            lateinit var state: MatchViewState

            viewModel.viewState.take(1).collectLatest {
                state = it
            }
            assertTrue(!state.showLoading)
            assertEquals(
                MatchInformation(
                    startDate = "",
                    endDate = "",
                    title = "A good match",
                    homeTeam = "Team A",
                    awayTeam = "",
                    result = false,
                    tossResult = null,
                    homeTeamScores = null,
                    awayTeamScores = null,
                    scoreCard = null,
                    firstEmpire = null,
                    secondEmpire = null,
                    thirdEmpire = null,
                ),
                state.info
            )
        }
    }
}