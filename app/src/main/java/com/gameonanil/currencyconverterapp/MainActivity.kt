package com.gameonanil.currencyconverterapp

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.gameonanil.currencyconverterapp.databinding.ActivityMainBinding
import com.gameonanil.currencyconverterapp.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainViewModel by viewModels()

    companion object{
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonConvert.setOnClickListener{
            viewModel.convert(
                binding.etFrom.text.toString(),
                binding.spFromCurrency.selectedItem.toString(),
                binding.spToCurerncy.selectedItem.toString()
            )
        }

        lifecycleScope.launchWhenStarted {
            viewModel.conversion.collect { event->
                when(event){
                    is MainViewModel.CurrencyEvent.Success->{
                        binding.progressBar.isVisible = false
                        binding.tvResult.setTextColor(Color.BLACK)
                        binding.tvResult.text = event.resultText
                    }
                    is MainViewModel.CurrencyEvent.Failure->{
                        binding.progressBar.isVisible = false
                        binding.tvResult.setTextColor(Color.RED)
                        binding.tvResult.text = event.errorText

                    }
                    is MainViewModel.CurrencyEvent.Loading->{
                        binding.progressBar.isVisible = true
                    }
                    else->Unit
                }
            }
        }


    }


    private fun testRetrofitInstance(){
        lifecycleScope.launchWhenStarted {
            val response = try {
                RetrofitInstance.api.getRates("USD")

            }catch (e: HttpException){
                Log.d(TAG, "testRetrofitInstance: HttpException")
                return@launchWhenStarted
            }catch (e: IOException){
                Log.d(TAG, "testRetrofitInstance: IOException")
                return@launchWhenStarted
            }

            if(response.isSuccessful && response.body()!=null){
                Log.d(TAG, "testRetrofitInstance: response: ${response.body()}")
            }else{
                Log.d(TAG, "testRetrofitInstance: error:: ${response.message()}")
            }
        }
    }
}