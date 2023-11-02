package com.indivar.repository

import com.indivar.domain.repo.match.details.ResultFixture
import com.indivar.domain.repo.match.details.LiveDetails
import com.indivar.domain.repo.match.details.MatchDetail
import com.indivar.domain.repo.match.details.MatchSummary
import com.indivar.domain.repo.match.details.Officials
import com.indivar.domain.repo.match.details.Results
import com.indivar.domain.repo.match.details.Team

object FakeData {

    val fakeMatchDetail1 = MatchDetail(
        results = Results(
            fixture = ResultFixture(
                id = 1002,
                series_id = 21223,
                match_title = "Australia vs New Zealand",
                start_date = null,
                end_date = null,
                home = Team(id = 100, "Australia", "AUS"),
                away = Team(id = 120, "New Zealand", "NZL"),
            ),
            liveDetails = LiveDetails(
                match_summary = MatchSummary(
                    in_play = "No",
                    result = "Yes",
                    status = "Australia won by 7 wkts (117b rem)",
                    toss = "Australia, elected to field first",
                    home_scores = "192/3 (30.3/50 ov, target 192)",
                    away_scores = "191"
                ),
                officials = Officials(
                    umpire_1 = "Marais Erasmus",
                    umpire_2 = "Richard Illingworth",
                    umpire_tv = "Richard Kettleborough",
                    referee = "Andy Pycroft",
                    umpire_reserve = "Chris Gaffaney"
                ),
                scorecard = null,
            ),
        )
    )
}
