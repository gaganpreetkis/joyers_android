package com.joyersapp.di

import com.joyersapp.feature.profile.data.remote.ProfileApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object ProfileNetworkModule {

    @Provides
    @Singleton
    fun provideProfileApi(
        retrofit: Retrofit
    ): ProfileApi {
        return retrofit.create(ProfileApi::class.java)
    }
}