package com.indivar.cricketapp.di

import com.indivar.core.Navigator
import com.indivar.cricketapp.navigation.Base64Encoder
import com.indivar.cricketapp.navigation.Base64EncoderImpl
import com.indivar.cricketapp.navigation.NavigatorImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@InstallIn(ActivityRetainedComponent::class)
@Module
class Dependencies {

    @Provides
    fun provideEncoder(encoder: Base64EncoderImpl): Base64EncoderImpl = encoder
    @Provides
    fun provideNavigator(navigatorImpl: NavigatorImpl): Navigator = navigatorImpl
}