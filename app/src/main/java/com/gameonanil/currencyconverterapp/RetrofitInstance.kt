package com.gameonanil.currencyconverterapp

import com.gameonanil.currencyconverterapp.data.CurrencyApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    val api: CurrencyApi by lazy {
        Retrofit.Builder()
                .baseUrl("https://v6.exchangerate-api.com/v6/${BuildConfig.API_KEY}/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(CurrencyApi::class.java)
    }
}