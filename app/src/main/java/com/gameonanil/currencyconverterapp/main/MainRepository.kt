package com.gameonanil.currencyconverterapp.main

import com.gameonanil.currencyconverterapp.data.models.CurrencyResponse
import com.gameonanil.currencyconverterapp.utils.Resource

interface MainRepository {

    suspend fun getRates(base: String): Resource<CurrencyResponse>
}