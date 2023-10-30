package com.indivar.domain.repo

import com.indivar.common.api.NetworkApi
import com.indivar.domain.repo.match.details.MatchDetail
import com.indivar.domain.repo.match.details.MatchInning
import com.indivar.domain.repo.match.details.Officials
import com.indivar.domain.repo.match.details.Team
import com.indivar.models.series.AllSeries
import com.indivar.models.match.BattingMatchStat
import com.indivar.models.Boundary
import com.indivar.models.BoundaryType
import com.indivar.models.match.BowlingMatchStat
import com.indivar.models.match.Inning
import com.indivar.models.match.Match
import com.indivar.models.match.MatchDates
import com.indivar.models.match.MatchOfficials
import com.indivar.models.match.Overs
import com.indivar.models.Player
import com.indivar.models.match.ScoreCard
import com.indivar.models.series.Series
import com.indivar.models.series.SeriesGroup
import com.indivar.domain.repo.series.list.AllSeriesDetail
import com.indivar.domain.repo.series.list.SeriesItem
import com.indivar.domain.repo.series.list.SeriesSet
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import javax.inject.Inject
import com.indivar.models.Team as ModelsTeam


class RepositoryImpl @Inject constructor(
    private val networkApi: NetworkApi,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) : Repository {
    override suspend fun pullMatchDetails(matchId: Int): Match? {
        return withContext(defaultDispatcher) {
            withTimeoutOrNull(5000) {
                val v = networkApi.getMatchDetails(matchId)
                v.match
            }
        }

    }

    override suspend fun getAllSeries(): AllSeries? {
        return withContext(defaultDispatcher) {
            withTimeoutOrNull(20000) {
                val v = networkApi.getAllSeries()
                v.allSeries
            }
        }
    }
}


val AllSeriesDetail.allSeries: AllSeries
    get() = AllSeries(
        title = this.meta.title,
        description = this.meta.description,
        seriesGroup = this.results.map { it.seriesGroup }
    )

val SeriesSet.seriesGroup: SeriesGroup
    get() = SeriesGroup(
        type = this.type,
        series = this.items.map { it.series }
    )

val SeriesItem.series: Series
    get() = Series(
        id = this.id,
        name = this.name,
        status = this.status,
        season = this.season,
    )
val Team.team: ModelsTeam
    get() = ModelsTeam(
        id = this.id,
        name = this.name,
        code = this.code,
        players = emptyList(),
    )
val MatchDetail.match: Match?
    get() = this.results?.let {
        Match(
            id = this.results.fixture.id,
            seriesId = this.results.fixture.series_id,
            matchDates = this.results.fixture.start_date?.let { startDate ->
                this.results.fixture.end_date?.let { endDate ->
                    MatchDates(startDate, endDate)
                }
            },
            title = this.results.fixture.match_title,
            homeTeam = this.results.fixture.home.team,
            awayTeams = listOf(this.results.fixture.away.team),
            result = this.results.liveDetails?.match_summary?.result == "Yes",
            tossResult = this.results.liveDetails?.match_summary?.toss,
            homeTeamScores = this.results.liveDetails?.match_summary?.home_scores,
            awayTeamScores = this.results.liveDetails?.match_summary?.away_scores,
            matchOfficials = this.results.liveDetails?.officials?.matchOfficials,
            scoreCard = this.results.liveDetails?.scorecard?.let { ScoreCard(it.map { it.inning }) },


            )
    }

val Officials.matchOfficials: MatchOfficials
    get() = MatchOfficials(
        firstUmpire = this.umpire_1,
        secondUmpire = this.umpire_2,
        thirdUmpire = this.umpire_tv,
        referee = this.referee,
        reserveUmpire = this.umpire_reserve,
    )

val String.overValue: Overs
    get() {
        this.toDoubleOrNull() ?: return Overs.None
        return if (this.contains('.')) {
            val parts = this.split('.')
            if (parts.size == 2) {
                Overs(completeOvers = parts[0].toInt(), ballsInCurrentOver = parts[1].toInt())
            } else {
                Overs.None
            }
        } else {
            Overs(completeOvers = this.toInt(), ballsInCurrentOver = 0)
        }
    }
val MatchInning.inning: Inning
    get() = Inning(
        title = this.title,
        runs = this.runs,
        overs = this.overs.overValue,
        wickets = this.wickets.toIntOrNull() ?: 0,
        extras = this.extras,
        extraTitle = this.extras_detail,
        fow = this.fow,
        batting = this.batting.map { stat ->
            val player = Player(id = stat.player_id, name = stat.player_name)
            val battingMatchStat = BattingMatchStat(
                battingOrder = stat.bat_order,
                outStyle = stat.how_out,
                minutesOfPlay = stat.minutes.toIntOrNull(),
                runs = stat.runs,
                balls = stat.balls,
                boundaries = buildList {
                    if (stat.fours > 0) {
                        this.add(Boundary(BoundaryType.Four, stat.fours))
                    }
                    if (stat.sixes > 0) {
                        this.add(Boundary(BoundaryType.Six, stat.sixes))
                    }
                },
                strikeRate = stat.strike_rate.toFloatOrNull(),
            )
            player to battingMatchStat
        },
        bowling = this.bowling.map { stat ->
            val player = Player(id = stat.player_id, name = stat.player_name)
            val battingMatchStat = BowlingMatchStat(

                overs = stat.overs.toIntOrNull() ?: 0,
                runs = stat.runs_conceded,
                maidens = stat.maidens,
                dotBalls = stat.dot_balls,
                boundaries = buildList {
                    if (stat.fours > 0) {
                        this.add(Boundary(BoundaryType.Four, stat.fours))
                    }
                    if (stat.sixes > 0) {
                        this.add(Boundary(BoundaryType.Six, stat.sixes))
                    }
                },
                economy = stat.economy.toFloatOrNull() ?: 0.0f,
                extras = stat.extras.toIntOrNull() ?: 0,
                wickets = stat.wickets,
            )
            player to battingMatchStat
        }
    )



