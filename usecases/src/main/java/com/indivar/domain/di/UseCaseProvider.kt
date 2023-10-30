package com.indivar.domain.di

import com.indivar.core.match.detail.domain.usecase.PullMatchDetailsUseCase
import com.indivar.core.series.list.domain.usecase.PullAllSeriesUseCase
import com.indivar.domain.usecases.PullAllSeriesUseCaseImpl
import com.indivar.domain.usecases.PullMatchDetailsUseCaseImpl
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