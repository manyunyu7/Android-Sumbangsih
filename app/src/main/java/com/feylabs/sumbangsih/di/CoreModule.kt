package com.feylabs.sumbangsih.di

import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.feylabs.sumbangsih.data.source.remote.web.AuthApiClient
import com.feylabs.sumbangsih.data.source.remote.web.NewsApiClient
import com.feylabs.sumbangsih.di.ServiceLocator.BASE_URL
import com.feylabs.sumbangsih.presentation._otp.ReceiveOTPViewModel
import com.feylabs.sumbangsih.presentation.detailtutorial.DetailTutorialViewModel
import com.feylabs.sumbangsih.presentation.pin.AuthViewModel
import com.feylabs.sumbangsih.presentation.ui.home.HomeViewModel
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val networkModule = module {
    single {
        OkHttpClient.Builder()
//            .authenticator(TokenAuthenticator(get(), get()))
            .addInterceptor(
                okhttp3.logging.HttpLoggingInterceptor()
                    .setLevel(okhttp3.logging.HttpLoggingInterceptor.Level.BODY)
            )
            .addInterceptor(
                ChuckerInterceptor.Builder(androidContext())
                    .collector(ChuckerCollector(androidContext()))
                    .maxContentLength(250000L)
                    .redactHeaders(emptySet())
                    .alwaysReadResponseBody(true)
                    .build()
            )
//            .addInterceptor(HttpCustomInterceptor(get(), get()))
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .build()
    }

    single {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
        retrofit.create(AuthApiClient::class.java)
    }

    single {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
        retrofit.create(NewsApiClient::class.java)
    }
}


val usecaseModule = module {
//    factory<AuthUseCase> { AuthInteractor(get()) }
}

val viewModelModule = module {
//    single { SharedViewModel(get(), get()) }
    single { ReceiveOTPViewModel(get()) }
    single { AuthViewModel(get()) }
    single { DetailTutorialViewModel() }
    single { HomeViewModel(get()) }
}

val repositoryModule = module {
//    single<IAuthRepository> { AuthRepository(get()) }
}