package com.stustirling.ribotviewer.data.api

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Stu Stirling on 21/09/2017.
 */
@Module
class ApiModule {

    @Singleton
    @Provides
    fun providesGsonConverterFactory(gsonConverterFactoryProvider: GsonConverterFactoryProvider) : GsonConverterFactory {
        return gsonConverterFactoryProvider.build()
    }

    @Provides
    fun providesRetrofitProvider(gsonConverterFactoryProvider: GsonConverterFactoryProvider) : RetrofitProvider {
        return RetrofitProvider("https://api.ribot.io/",gsonConverterFactoryProvider.build())
    }

    @Singleton
    @Provides
    fun providesRetrofit( retrofitProvider: RetrofitProvider) : Retrofit{
        return retrofitProvider.build()
    }

    @Singleton
    @Provides
    fun providesRibotService(retrofit: Retrofit) : RibotService {
        return retrofit.create(RibotService::class.java)
    }

}

class GsonConverterFactoryProvider @Inject constructor() {
    fun build() : GsonConverterFactory {
        return GsonConverterFactory.create()
    }
}

class RetrofitProvider (val baseUrl: String,
                       val gsonConverterFactory: GsonConverterFactory) {
    fun build() : Retrofit{
        val client = OkHttpClient.Builder()
//                .addInterceptor(HttpLoggingInterceptor().apply {
//                    level = HttpLoggingInterceptor.Level.BODY })
                .build()

        return Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(gsonConverterFactory)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build()
    }
}