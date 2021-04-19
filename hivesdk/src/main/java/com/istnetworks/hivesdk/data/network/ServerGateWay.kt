package com.istnetworks.hivesdk.data.network

import com.istnetworks.hivesdk.data.utils.Preferences
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLSession


private const val SERVER_BASE_URL = "http://13.69.99.146:7050/api/"

fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
    val interceptor = HttpLoggingInterceptor()
    interceptor.level = HttpLoggingInterceptor.Level.BODY
    return interceptor
}

val provideOkHttpClientBuilder: OkHttpClient by lazy {
    OkHttpClient.Builder()
        .hostnameVerifier(HostnameVerifier { _: String?, _: SSLSession? -> true })
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS).addInterceptor(provideHttpLoggingInterceptor())
        .addInterceptor(HeaderInterceptor())
        .build()
}
private val retrofit: Retrofit by lazy {
    Retrofit.Builder()
        .baseUrl(SERVER_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .client(provideOkHttpClientBuilder)
        .build()
}

val apiService: ApiService by lazy {
    retrofit.create(ApiService::class.java)
}

class HeaderInterceptor: Interceptor {

    /**
     * Interceptor class for setting of the dynamic headers for every request
     */
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
            // dynamic token you get should be use instead of #YOUR_DYNAMIC_TOKEN.
            request = request?.newBuilder()
                ?.addHeader("Authorization", "Bearer ${Preferences.getTokenResponse().accessToken!!}")
                ?.header("Content-Type", "application/json")
                ?.header("Accept", "application/json")
        ?.build()
        return chain.proceed(request)
    }
}