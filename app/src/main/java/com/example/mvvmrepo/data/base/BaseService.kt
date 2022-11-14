package com.example.mvvmrepo.data.base

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object BaseService {
    private var retrofitBaseApi: Retrofit? = null

    fun getBaseApi(): PokemonAPIService? {
        return createRetrofitBase().create(PokemonAPIService::class.java)
    }

    fun createRetrofitBase(): Retrofit {
        if (retrofitBaseApi == null) {
            retrofitBaseApi = Retrofit.Builder()
                .baseUrl("https://pokeapi.co/api/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(getOkHttpClient())
                .build()
        }
        return retrofitBaseApi as Retrofit
    }

    private fun getOkHttpClient(): OkHttpClient? {
        val client = OkHttpClient.Builder().addInterceptor(
            HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC })
            .addInterceptor { chain ->
                val originalRequest: Request = chain.request()
                val requestBuilder = originalRequest.newBuilder()
                val request = requestBuilder.build()
                chain.proceed(request)
            }
            .build()
        return client
    }
}
