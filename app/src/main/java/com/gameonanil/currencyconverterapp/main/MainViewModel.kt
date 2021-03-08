package com.gameonanil.currencyconverterapp.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gameonanil.currencyconverterapp.data.models.ConversionRates
import com.gameonanil.currencyconverterapp.data.models.CurrencyResponse

import com.gameonanil.currencyconverterapp.utils.DispatcherProvider
import com.gameonanil.currencyconverterapp.utils.Resource

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.round

import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
        private val repository: DefaultMainRepository,
        private val dispatchers: DispatcherProvider
) : ViewModel(){

    private  val TAG = "MainViewModel"

    sealed class CurrencyEvent{
        class Success(val resultText:String): CurrencyEvent()
        class Failure(val errorText:String): CurrencyEvent()
        object Loading: CurrencyEvent()
        object Empty: CurrencyEvent()
    }

    private val _conversion = MutableStateFlow<CurrencyEvent>(CurrencyEvent.Empty)
    val conversion: StateFlow<CurrencyEvent> = _conversion


    private val _resultString = MutableLiveData<String>()
    val resultString: LiveData<String> = _resultString

    fun convert(
            amountStr: String,
            fromCurrency: String,
            toCurrency: String
    ) {
        val fromAmount = amountStr.toFloatOrNull()
        if(fromAmount == null) {
            _conversion.value = CurrencyEvent.Failure("Not a valid amount")
            return
        }

        viewModelScope.launch(dispatchers.io) {
            _conversion.value = CurrencyEvent.Loading
            when(val ratesResponse = repository.getRates(fromCurrency)) {
                is Resource.Error -> _conversion.value = CurrencyEvent.Failure(ratesResponse.message!!)
                is Resource.Success -> {
                    val rates = ratesResponse.data!!.conversion_rates
                    val rate = getRateForCurrency(toCurrency, rates)
                    if(rate == null) {
                        _conversion.value = CurrencyEvent.Failure("Unexpected error")
                    } else {
                        val convertedCurrency = round(fromAmount * rate * 100) / 100
                        _conversion.value = CurrencyEvent.Success(
                                "$fromAmount $fromCurrency = $convertedCurrency $toCurrency"
                        )
                    }
                }
            }
        }
    }

    private fun getRateForCurrency(currency: String, rates: ConversionRates): Double? {
        return when (currency){
            "NPR"->rates.NPR
            "USD"->rates.USD
            "AUD"->rates.AUD
            "AED"->rates.AED
            "AFN"->rates.AFN
            "ALL"->rates.ALL
            "AMD"->rates.AMD
            "ANG"->rates.ANG
            else-> null
        }
    }


}




