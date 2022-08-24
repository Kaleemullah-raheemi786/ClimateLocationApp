package com.example.climatelocationapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.climatelocationapp.Networking.ApiConfig
import com.example.climatelocationapp.model.WeatherModel
import kotlinx.coroutines.Job
import retrofit2.Call
import retrofit2.Response

class WeatherViewModel : ViewModel() {
    private val _weatherModel = MutableLiveData<WeatherModel>()
    val showWeatherModel : LiveData<WeatherModel> = _weatherModel

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    private var handleJob: Job? = null

    override fun onCleared() {
        super.onCleared()
        handleJob?.cancel()
    }

    fun getListWeatherDetails (lat: Double, lon: Double, APPID: String){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getCurrentWeatherData(lat.toString(), lon.toString(),APPID)
        client?.enqueue(object: retrofit2.Callback<WeatherModel> {
            override fun onResponse(
                call: Call<WeatherModel>,
                response: Response<WeatherModel>
            ){
                _isLoading.value = false
                Log.d("tes", response.toString())
                if (response.isSuccessful){
                    _weatherModel.value = response.body()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<WeatherModel>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    companion object{
        private const val TAG = "PokemonViewModel"
        const val APPID = "c0c7057b8c23ba410a4846527cadd6c8"
    }
}
