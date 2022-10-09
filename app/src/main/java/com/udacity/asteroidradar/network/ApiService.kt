package com.udacity.asteroidradar.network

import com.udacity.asteroidradar.BuildConfig
import com.udacity.asteroidradar.Constants
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private val client = OkHttpClient.Builder()
    .addInterceptor(ApiKeyInterceptor())
    //.addInterceptor(ChuckerInterceptor(context))
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(Constants.BASE_URL)
    .client(client)
    .build()


interface ApiService {
    @GET("neo/rest/v1/feed")
    suspend fun getResponse(
        @Query("start_date") startDate: String = "",
        @Query("end_date") endDate: String = ""
    ): String

    @GET("planetary/apod")
    suspend fun getPictureOfTheDay(): String
}

object Api {
    val retrofitService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}


class ApiKeyInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var original = chain.request()
        val apiKey = BuildConfig.API_KEY
        val url = original.url().newBuilder().addQueryParameter("api_key", apiKey).build()
        original = original.newBuilder().url(url).build()
        return chain.proceed(original)
    }
}



