package com.indivar.usecases

import com.indivar.core.viewmodels.PullMatchDetailsUseCase
import javax.inject.Inject

class PullMatchDetailsUseCaseImpl @Inject constructor(
    private val networkRepository: Repository
): PullMatchDetailsUseCase {

    override suspend fun invoke(
        id: Int
    ) =  networkRepository.pullMatchDetails(id)


}