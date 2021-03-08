package com.gameonanil.currencyconverterapp.main

import com.gameonanil.currencyconverterapp.data.CurrencyApi
import com.gameonanil.currencyconverterapp.data.models.CurrencyResponse
import com.gameonanil.currencyconverterapp.utils.Resource
import javax.inject.Inject

class DefaultMainRepository @Inject constructor(
    private val api: CurrencyApi
): MainRepository {
    override suspend fun getRates(base: String): Resource<CurrencyResponse> {
        return try {
            val response = api.getRates(base)
            val result = response.body()
            if(response.isSuccessful && result !=null){
                Resource.Success(result)
            }else{
                Resource.Error(response.message())
            }
        }catch (e: Exception){
            Resource.Error(e.message ?: "An error occured")
        }
    }


}