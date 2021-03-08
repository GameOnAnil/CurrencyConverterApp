package com.gameonanil.currencyconverterapp.di

import androidx.viewbinding.BuildConfig
import com.gameonanil.currencyconverterapp.BuildConfig.API_KEY
import com.gameonanil.currencyconverterapp.data.CurrencyApi
import com.gameonanil.currencyconverterapp.di.AppModule_ProvideCurrencyApiFactory.create
import com.gameonanil.currencyconverterapp.main.DefaultMainRepository
import com.gameonanil.currencyconverterapp.main.MainRepository
import com.gameonanil.currencyconverterapp.utils.DispatcherProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import okhttp3.Dispatcher
import retrofit2.CallAdapter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton




private const val BASE_URL = "https://v6.exchangerate-api.com/v6/b995ef67b53c9cbb008bbf97/"

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideCurrencyApi(): CurrencyApi = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(CurrencyApi::class.java)


    @Singleton
    @Provides
    fun provideMainRepository(api: CurrencyApi): MainRepository = DefaultMainRepository(api)



    @Singleton
    @Provides
    fun provideDispatcher(): DispatcherProvider = object : DispatcherProvider{
        override val main: CoroutineDispatcher
            get() = Dispatchers.Main
        override val io: CoroutineDispatcher
            get() = Dispatchers.IO
        override val default: CoroutineDispatcher
            get() =  Dispatchers.Default
        override val unconfined: CoroutineDispatcher
            get() =  Dispatchers.Unconfined

    }

}