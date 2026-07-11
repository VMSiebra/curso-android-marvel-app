package com.example.marvelapp.framework.di


import com.example.core.data.network.interceptor.AuthorizationInterception
import com.example.marvelapp.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Calendar
import java.util.TimeZone

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            setLevel(
                if(BuildConfig.DEBUG){
                    HttpLoggingInterceptor.Level.BODY
                } else{
                    HttpLoggingInterceptor.Level.NONE
                }
            )

        }
    }

    @Provides
    fun providerAuthorizationInterceptor(): AuthorizationInterception{
        return AuthorizationInterception(
            publicKey = BuildConfig.PUBLIC_KEY,
            privateKey = BuildConfig.PRIVATE_KEY,
            calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        )
    }

    @Provides
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
    ): OkHttpClient{
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .readTimeout(15, java.util.concurrent.TimeUnit.SECONDS)
            .connectTimeout(15, java.util.concurrent.TimeUnit.SECONDS)
            .build()
    }

    @Provides
    fun provideGsonConverterFactory(): GsonConverterFactory{
        return GsonConverterFactory.create()
    }

    @Provides
    fun provideARetrofit(
        okHttpClient: OkHttpClient,
        converterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_API)
            .client(okHttpClient)
            .addConverterFactory(converterFactory)
            .build()
    }
}