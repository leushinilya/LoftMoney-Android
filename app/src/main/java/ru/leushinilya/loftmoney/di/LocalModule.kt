package ru.leushinilya.loftmoney.di

import android.app.Application
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import ru.leushinilya.loftmoney.data.local.PreferencesDataSource

@Module
@InstallIn(ViewModelComponent::class)
object LocalModule {

    @Provides
    fun providePreferencesDataSource(
        application: Application
    ): PreferencesDataSource = PreferencesDataSource(application)

}