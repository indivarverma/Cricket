package com.indivar.domain.di

import com.indivar.core.fixtures.list.domain.usecase.PullSeriesFixturesUseCase
import com.indivar.core.match.detail.domain.usecase.PullMatchDetailsUseCase
import com.indivar.core.series.detail.domain.usecase.GetSeriesGroupUseCase
import com.indivar.core.series.groups.domain.usecase.PullSeriesGroupsUseCase
import com.indivar.domain.usecases.GetSeriesGroupUseCaseImpl
import com.indivar.domain.usecases.PullSeriesGroupsUseCaseImpl
import com.indivar.domain.usecases.PullMatchDetailsUseCaseImpl
import com.indivar.domain.usecases.PullSeriesFixturesUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
interface UseCaseProvider {
    @Binds
    fun createPullMatchDetailsUseCase(pullMatchDetailsUseCaseImpl: PullMatchDetailsUseCaseImpl): PullMatchDetailsUseCase

    @Binds
    fun createPullPlayerDetailsUseCase(useCaseImpl: PullSeriesGroupsUseCaseImpl): PullSeriesGroupsUseCase

    @Binds
    fun createGetSeriesGroupUseCase(useCaseImpl: GetSeriesGroupUseCaseImpl): GetSeriesGroupUseCase

    @Binds
    fun createPullSeriesFixturesUseCase(useCaseImpl : PullSeriesFixturesUseCaseImpl): PullSeriesFixturesUseCase
}
