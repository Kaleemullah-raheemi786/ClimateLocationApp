package com.example.climatelocationapp.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.climatelocationapp.databinding.FragmentClimateLocationBinding
import com.example.climatelocationapp.model.WeatherModel
import com.example.climatelocationapp.viewmodel.WeatherViewModel
import com.squareup.picasso.Picasso

class ClimateLocationFragment : Fragment() {

    private lateinit var binding: FragmentClimateLocationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentClimateLocationBinding.inflate(layoutInflater, container, false)

        val weatherModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[WeatherViewModel::class.java]
        weatherModel.isLoading.observe(viewLifecycleOwner) { showLoading(it) }


        val lat = requireArguments().getString("lat")
        val lon = requireArguments().getString("lon")

        if (lat != null && lon !=null){
            binding.constraint.visibility = View.VISIBLE
            weatherModel.getListWeatherDetails(lat.toDouble(), lon.toDouble(), WeatherViewModel.APPID)
            weatherModel.showWeatherModel.observe(viewLifecycleOwner) { detailWeather ->
                setWeatherDetail(
                    detailWeather
                )
            }
        }

        return binding.root
    }

    private fun setWeatherDetail(detailWeather: WeatherModel?) {
        binding.apply {
            try {
                Picasso.get()
                    .load("https://openweathermap.org/img/wn/" + detailWeather?.weather?.get(0)?.icon.toString() + "@2x.png")
                    .into(imageView)
                tvDescription.text = detailWeather?.weather?.get(0)?.description
                tvCountryName.text = detailWeather?.sys?.country
                tvCityName.text = detailWeather?.name
                tvHummidity.text = detailWeather?.main?.humidity.toString()
                tvSpeed.text = detailWeather?.wind?.speed.toString()
                tvPressure.text = detailWeather?.main?.pressure.toString()
                tvTemp.text = detailWeather?.main?.temp.toString()
                tvTemMAx.text = detailWeather?.main?.tempMax.toString()
                tvTempMin.text = detailWeather?.main?.tempMin.toString()
            } catch (e: Exception) {
                println("Error found $e")
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

}