package com.indivar.domain.di

import com.indivar.core.match.detail.domain.usecase.PullMatchDetailsUseCase
import com.indivar.core.series.detail.domain.usecase.GetSeriesGroupUseCase
import com.indivar.core.series.list.domain.usecase.PullAllSeriesUseCase
import com.indivar.domain.usecases.GetSeriesGroupUseCaseImpl
import com.indivar.domain.usecases.PullAllSeriesUseCaseImpl
import com.indivar.domain.usecases.PullMatchDetailsUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
interface UseCaseProvider {
    @Binds
    fun createPullMatchDetailsUseCase(pullMatchDetailsUseCaseImpl: PullMatchDetailsUseCaseImpl): PullMatchDetailsUseCase

    @Binds
    fun createPullPlayerDetailsUseCase(useCaseImpl: PullAllSeriesUseCaseImpl): PullAllSeriesUseCase

    @Binds
    fun createGetSeriesGroupUseCase(useCaseImpl: GetSeriesGroupUseCaseImpl): GetSeriesGroupUseCase
}
