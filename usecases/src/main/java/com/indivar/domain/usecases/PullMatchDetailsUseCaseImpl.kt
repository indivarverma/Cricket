package com.indivar.domain.usecases

import com.indivar.core.data.Response
import com.indivar.core.match.detail.domain.usecase.PullMatchDetailsUseCase
import com.indivar.domain.repo.Repository
import com.indivar.models.match.Match
import javax.inject.Inject

class PullMatchDetailsUseCaseImpl @Inject constructor(
    private val networkRepository: Repository
) : PullMatchDetailsUseCase {
    override suspend fun trigger(
        id: Int
    ): Response<Match> = networkRepository.pullMatchDetails(id)


}