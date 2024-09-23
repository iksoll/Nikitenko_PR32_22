package com.example.prakt22

import WeatherResponse
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var editTextCity: EditText
    private lateinit var buttonGetWeather: Button
    private lateinit var textViewWeather: TextView

    private val apiKey = "29ac29a33495b3c7e9913fbc87b91c21"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editTextCity = findViewById(R.id.editTextCity)
        buttonGetWeather = findViewById(R.id.buttonGetWeather)
        textViewWeather = findViewById(R.id.textViewWeather)

        buttonGetWeather.setOnClickListener { getWeather() }
    }

    private fun getWeather() {
        val cityName = editTextCity.text.toString().trim()

        if (cityName.isEmpty()) {
            showSnackbar("Введите название города")
            return
        }

        RetrofitInstance.api.getWeatherByCityName(cityName, apiKey).enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val weather = response.body()!!
                    textViewWeather.text = "Город: ${weather.name}\n" +
                            "Температура: ${weather.main.temp} °C\n" +
                            "Скорость ветра: ${weather.wind.speed} м/с\n" +
                            "Давление: ${weather.main.pressure} гПа"
                } else {
                    showSnackbar("Город не найден")
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                showSnackbar("Ошибка загрузки данных")
            }
        })
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show()
    }
}