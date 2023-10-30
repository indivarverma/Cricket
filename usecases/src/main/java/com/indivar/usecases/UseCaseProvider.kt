package com.indivar.usecases

import com.indivar.core.viewmodels.PullMatchDetailsUseCase
import com.indivar.core.viewmodels.PullAllSeriesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class UseCaseProvider {
    @Provides
    fun createPullMatchDetailsUseCase(pullMatchDetailsUseCaseImpl: PullMatchDetailsUseCaseImpl): PullMatchDetailsUseCase {
        return pullMatchDetailsUseCaseImpl
    }

    @Provides
    fun createPullPlayerDetailsUseCase(useCaseImpl: PullAllSeriesUseCaseImpl): PullAllSeriesUseCase {
        return useCaseImpl
    }
}