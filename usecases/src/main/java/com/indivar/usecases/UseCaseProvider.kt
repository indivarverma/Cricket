package com.indivar.usecases

import com.indivar.core.viewmodels.PullMatchDetailsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class UseCaseProvider {
    @Provides
    fun createPullMatchDetailsUseCase( pullMatchDetailsUseCaseImpl: PullMatchDetailsUseCaseImpl): PullMatchDetailsUseCase {
        return pullMatchDetailsUseCaseImpl
    }
}