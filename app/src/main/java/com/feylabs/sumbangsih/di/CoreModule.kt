package com.feylabs.sumbangsih.di

import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.feylabs.sumbangsih.data.source.remote.web.AuthApiClient
import com.feylabs.sumbangsih.data.source.remote.web.CommonApiClient
import com.feylabs.sumbangsih.data.source.remote.web.NewsApiClient
import com.feylabs.sumbangsih.di.ServiceLocator.BASE_URL
import com.feylabs.sumbangsih.presentation.CommonViewModel
import com.feylabs.sumbangsih.presentation._otp.ReceiveOTPViewModel
import com.feylabs.sumbangsih.presentation.chat.ChatCsViewModel
import com.feylabs.sumbangsih.presentation.data_diri.DataDiriViewModel
import com.feylabs.sumbangsih.presentation.detailtutorial.DetailTutorialViewModel
import com.feylabs.sumbangsih.presentation.komplain.KomplainViewModel
import com.feylabs.sumbangsih.presentation.ktp_verif.KTPVerifViewModel
import com.feylabs.sumbangsih.presentation.notification.NotificationViewModel
import com.feylabs.sumbangsih.presentation.pengajuan.PengajuanViewModel
import com.feylabs.sumbangsih.presentation.pin.AuthViewModel
import com.feylabs.sumbangsih.presentation.ui.home.HomeViewModel
import com.feylabs.sumbangsih.presentation.ui.home.news.ListAllNewsViewModel
import com.google.android.gms.common.internal.service.Common
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import okhttp3.Protocol
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
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
                    .maxContentLength(25000000L)
                    .redactHeaders(emptySet())
                    .alwaysReadResponseBody(true)
                    .build()
            )
//            .addInterceptor(HttpCustomInterceptor(get(), get()))
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
//            .connectionPool(ConnectionPool(0, 1, TimeUnit.NANOSECONDS))
//            .protocols(listOf(Protocol.HTTP_1_1))
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
        val retrofitz = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
        retrofitz.create(NewsApiClient::class.java)
    }
    single {
        val retrofitz = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
        retrofitz.create(CommonApiClient::class.java)
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
    single { ListAllNewsViewModel() }
    single { KTPVerifViewModel(get()) }
    single { PengajuanViewModel(get()) }
    single { ChatCsViewModel(get()) }
    single { KomplainViewModel(get()) }
    single { DataDiriViewModel(get()) }
    single { NotificationViewModel(get()) }
    viewModel { CommonViewModel(get(), get()) }
}

val repositoryModule = module {
//    single<IAuthRepository> { AuthRepository(get()) }
}