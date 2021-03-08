package com.gameonanil.currencyconverterapp.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gameonanil.currencyconverterapp.data.models.ConversionRates

import com.gameonanil.currencyconverterapp.utils.DispatcherProvider
import com.gameonanil.currencyconverterapp.utils.Resource

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
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
                    if (rate == null) {
                        _conversion.value = CurrencyEvent.Failure("Unexpected error")
                    } else {
                        Log.d(TAG, "convert: rate>>>>>>: $rate")
                        val convertedCurrency = round(fromAmount * rate * 1000) / 1000
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
            "USD"->rates.USD
            "AED"->rates.AED
            "AFN"->rates.AFN
            "ALL"->rates.ALL
            "AMD"->rates.AMD
            "ANG"->rates.ANG
            "AOA"->rates.AOA
            "ARS"->rates.ARS
            "AUD"->rates.AUD
            "AWG"->rates.AWG
            "AZN"->rates.AZN
            "BAM"->rates.BAM
            "BBD"->rates.BBD
            "BDT"->rates.BDT
            "BGN"->rates.BGN
            "BHD"->rates.BHD
            "BIF"->rates.BIF
            "BMD"->rates.BMD
            "BND"->rates.BND
            "BOB"->rates.BOB
            "BRL"->rates.BRL
            "BSD"->rates.BSD
            "BTN"->rates.BTN
            "BWP"->rates.BWP
            "BYN"->rates.BYN
            "BZD"->rates.BZD
            "CAD"->rates.CAD
            "CDF"->rates.CDF
            "CHF"->rates.CHF
            "CLP"->rates.CLP
            "CNY"->rates.CNY
            "COP"->rates.COP
            "CRC"->rates.CRC
            "CUC"->rates.CUC
            "CUP"->rates.CUP
            "CVE"->rates.CVE
            "CZK"->rates.CZK
            "DJF"->rates.DJF
            "DKK"->rates.DKK
            "DOP"->rates.DOP
            "DZD"->rates.DZD
            "EGP"->rates.EGP
            "ERN"->rates.ERN
            "ETB"->rates.ETB
            "EUR"->rates.EUR
            "FJD"->rates.FJD
            "FKP"->rates.FKP
            "FOK"->rates.FOK
            "GBP"->rates.GBP
            "GEL"->rates.GEL
            "GGP"->rates.GGP
            "GHS"->rates.GHS
            "GIP"->rates.GIP
            "GMD"->rates.GMD
            "GNF"->rates.GNF
            "GTQ"->rates.GTQ
            "GYD"->rates.GYD
            "HKD"->rates.HKD
            "HNL"->rates.HNL
            "HRK"->rates.HRK
            "HTG"->rates.HTG
            "HUF"->rates.HUF
            "IDR"->rates.IDR
            "ILS"->rates.ILS
            "IMP"->rates.IMP
            "INR"->rates.INR
            "IQD"->rates.IQD
            "IRR"->rates.IRR
            "ISK"->rates.ISK
            "JMD"->rates.JMD
            "JOD"->rates.JOD
            "JPY"->rates.JPY
            "KES"->rates.KES
            "KGS"->rates.KGS
            "KHR"->rates.KHR
            "KID"->rates.KID
            "KMF"->rates.KMF
            "KRW"->rates.KRW
            "KWD"->rates.KWD
            "KYD"->rates.KYD
            "KZT"->rates.KZT
            "LAK"->rates.LAK
            "LBP"->rates.LBP
            "LKR"->rates.LKR
            "LRD"->rates.LRD
            "LSL"->rates.LSL
            "LYD"->rates.LYD
            "MAD"->rates.MAD
            "MDL"->rates.MDL
            "MGA"->rates.MGA
            "MKD"->rates.MKD
            "MMK"->rates.MMK
            "MNT"->rates.MNT
            "MOP"->rates.MOP
            "MRU"->rates.MRU
            "MUR"->rates.MUR
            "MVR"->rates.MVR
            "MWK"->rates.MWK
            "MXN"->rates.MXN
            "MYR"->rates.MYR
            "MZN"->rates.MZN
            "NAD"->rates.NAD
            "NGN"->rates.NGN
            "NIO"->rates.NIO
            "NOK"->rates.NOK
            "NPR"->rates.NPR
            "NZD"->rates.NZD
            "OMR"->rates.OMR
            "PAB"->rates.PAB
            "PEN"->rates.PEN
            "PGK"->rates.PGK
            "PHP"->rates.PHP
            "PKR"->rates.PKR
            "PLN"->rates.PLN
            "PYG"->rates.PYG
            "QAR"->rates.QAR
            "RON"->rates.RON
            "RSD"->rates.RSD
            "RUB"->rates.RUB
            "RWF"->rates.RWF
            "SAR"->rates.SAR
            "SBD"->rates.SBD
            "SCR"->rates.SCR
            "SDG"->rates.SDG
            "SEK"->rates.SEK
            "SGD"->rates.SGD
            "SHP"->rates.SHP
            "SLL"->rates.SLL
            "SOS"->rates.SOS
            "SRD"->rates.SRD
            "SSP"->rates.SSP
            "STN"->rates.STN
            "SYP"->rates.SYP
            "SZL"->rates.SZL
            "THB"->rates.THB
            "TJS"->rates.TJS
            "TMT"->rates.TMT
            "TND"->rates.TND
            "TOP"->rates.TOP
            "TRY"->rates.TRY
            "TTD"->rates.TTD
            "TVD"->rates.TVD
            "TWD"->rates.TWD
            "TZS"->rates.TZS
            "UAH"->rates.UAH
            "UGX"->rates.UGX
            "UYU"->rates.UYU
            "UZS"->rates.UZS
            "VES"->rates.VES
            "VND"->rates.VND
            "VUV"->rates.VUV
            "WST"->rates.WST
            "XAF"->rates.XAF
            "XCD"->rates.XCD
            "XDR"->rates.XDR
            "XOF"->rates.XOF
            "XPF"->rates.XPF
            "YER"->rates.YER
            "ZAR"->rates.ZAR
            "ZMW"->rates.ZMW
            else-> null
        }
    }


}




