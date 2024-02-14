package com.example.sbscovidapp.data.network

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RetrofitModule {

    @Singleton
    @Provides
    fun provideClient(): OkHttpClient = OkHttpClient()

    @Singleton
    @Provides
    fun provideMoshiConverterFactory(): MoshiConverterFactory =
        MoshiConverterFactory.create()

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient,
                        moshiConverterFactory: MoshiConverterFactory): Retrofit =
        Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(moshiConverterFactory)
            .baseUrl(Base_Url)
            .build()

    @Singleton
    @Provides
    fun provideGlobalDataService(retrofit: Retrofit): CovidApiService =
        retrofit.create(CovidApiService::class.java)

    companion object {
        const val Base_Url = "https://covid-api.com/api/"
    }

}