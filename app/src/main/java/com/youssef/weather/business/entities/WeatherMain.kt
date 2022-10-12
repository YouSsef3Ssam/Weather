package com.youssef.weather.business.entities

import com.google.gson.annotations.SerializedName

class WeatherMain(
    @SerializedName("temp") val temp: Double,
    @SerializedName("feels_like") val feelsLike: Double,
    @SerializedName("pressure") val pressure: Long,
    @SerializedName("humidity") val humidity: Long,
    @SerializedName("temp_min") val tempMin: Double,
    @SerializedName("temp_max") val tempMax: Double
)