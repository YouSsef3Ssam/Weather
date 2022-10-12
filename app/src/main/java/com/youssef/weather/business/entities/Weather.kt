package com.youssef.weather.business.entities

import com.google.gson.annotations.SerializedName

class Weather(
    @SerializedName("dt") val dateTime: Long,
    @SerializedName("main") val main: WeatherMain,
    @SerializedName("wind") val wind: WeatherWind,
    @SerializedName("weather") val weather: List<WeatherMetData>,
    @SerializedName("latitude") var latitude: Double?,
    @SerializedName("longitude") var longitude: Double?
)