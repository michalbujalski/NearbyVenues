package com.mb.data.network

import com.mb.data.BuildConfig
import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

class NetworkServiceProvider {
    companion object {
        private const val PARAM_CLIENT_ID = "client_id"
        private const val PARAM_CLIENT_SECRET = "client_secret"
        private const val BASE_URL_FOURSQUARE = "https://api.foursquare.com/v2/"
    }

    private fun client(): OkHttpClient {

        val logInterceptor = HttpLoggingInterceptor()
        logInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient().newBuilder()
                .addInterceptor(logInterceptor)
                .addInterceptor { chain ->
                    var request = chain.request()
                    val url = request
                            .url()
                            .newBuilder()
                            .addQueryParameter(PARAM_CLIENT_ID, BuildConfig.FOURSQUARE_CLIENT_ID)
                            .addQueryParameter(PARAM_CLIENT_SECRET, BuildConfig.FOURSQUARE_CLIENT_SECRET)
                            .addQueryParameter("v", "20170227")
                            .build()
                    request = request.newBuilder().url(url).build()
                    chain.proceed(request)
                }
                .build()
    }

    private fun moshiConverter(moshi: Moshi) = MoshiConverterFactory.create(moshi)

    private fun moshi(): Moshi {
        return Moshi
                .Builder()
                .add(KotlinJsonAdapterFactory())
                .build()
    }

    private fun retrofit(): Retrofit {
        return Retrofit.Builder()
                .baseUrl(BASE_URL_FOURSQUARE)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(
                        moshiConverter(moshi())
                )
                .client(client())
                .build()
    }

    fun venuesService(): VenuesService {
        return retrofit().create(VenuesService::class.java)
    }
}