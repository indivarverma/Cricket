package com.indivar.domain.usecases

import com.indivar.core.data.Response
import com.indivar.core.fixtures.list.domain.usecase.PullSeriesFixturesUseCase
import com.indivar.domain.repo.Repository
import com.indivar.models.series.SeriesFixtures
import javax.inject.Inject

class PullSeriesFixturesUseCaseImpl @Inject constructor(
    private val networkRepository: Repository,
) : PullSeriesFixturesUseCase {
    override suspend fun fetchSeriesFixtures(seriesId: Int): Response<SeriesFixtures> =

        when (val response = networkRepository.fetchSeriesFixtures(seriesId)) {
            is Response.Success -> response.copy(
                data = response.data.copy(
                    fixtures = response.data.fixtures.sortedWith { first, second ->
                        val firstDate = first.date
                        val secondDate = second.date
                        when {
                            firstDate != null && secondDate != null -> {
                                if (firstDate.isAfter(secondDate)) -1
                                else if (firstDate.isBefore(secondDate)) 1
                                else first.id - second.id
                            }

                            firstDate == null && secondDate == null -> first.id - second.id
                            else -> {
                                if (firstDate == null) -1 else 1;
                            }
                        }
                    }
                )
            )

            is Response.Error -> response
        }


}