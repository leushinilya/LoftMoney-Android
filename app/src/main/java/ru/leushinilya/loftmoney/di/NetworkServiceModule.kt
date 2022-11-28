package ru.leushinilya.loftmoney.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import ru.leushinilya.loftmoney.data.remote.service.AuthService
import ru.leushinilya.loftmoney.data.remote.service.ItemsService

@Module
@InstallIn(SingletonComponent::class)
object NetworkServiceModule {

    @Provides
    fun provideItemsService(retrofit: Retrofit): ItemsService =
        retrofit.create(ItemsService::class.java)

    @Provides
    fun provideAuthService(retrofit: Retrofit): AuthService =
        retrofit.create(AuthService::class.java)

}