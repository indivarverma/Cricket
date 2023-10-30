package com.indivar.domain.usecases

import com.indivar.core.data.Response
import com.indivar.core.match.detail.domain.usecase.PullMatchDetailsUseCase
import com.indivar.domain.repo.Repository
import com.indivar.models.match.Match
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class PullMatchDetailsUseCaseImpl @Inject constructor(
    private val networkRepository: Repository
) : PullMatchDetailsUseCase {
    override suspend fun trigger(
        id: Int
    ): Response<Match> = coroutineScope {
        try {
            networkRepository.pullMatchDetails(id)?.let {
                Response.Success(it)
            } ?: Response.Error(404, "Empty response")
        } catch (e: Throwable) {
            if (e is DetailedServerError) {
                Response.Error(
                    errorCode = e.errorCode,
                    errorMessage = e.errorMessage,
                )
            } else {
                Response.Error(
                    errorCode = 400,
                    errorMessage = e.message ?: "",
                )
            }
        }

    }


}