package com.indivar.cricketapp.di

import com.indivar.core.Base64Encoder
import com.indivar.core.Navigator
import com.indivar.cricketapp.navigation.Base64EncoderImpl
import com.indivar.cricketapp.navigation.NavigatorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@InstallIn(ActivityRetainedComponent::class)
@Module
interface Dependencies {

    @Binds
    fun provideEncoder(encoder: Base64EncoderImpl): Base64Encoder

    @Binds
    fun provideNavigator(navigatorImpl: NavigatorImpl): Navigator

}