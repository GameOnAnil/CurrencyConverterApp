package com.gameonanil.currencyconverterapp.data

import com.gameonanil.currencyconverterapp.data.models.CurrencyResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CurrencyApi {

    @GET("latest/{base}")
    suspend fun getRates(
           @Path("base") base: String
    ): Response<CurrencyResponse>
}