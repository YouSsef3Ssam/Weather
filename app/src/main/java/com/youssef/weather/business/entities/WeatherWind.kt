package com.youssef.weather.business.entities

import com.google.gson.annotations.SerializedName

class WeatherWind(
    @SerializedName("speed") val speed: Double,
    @SerializedName("deg") val deg: Long
)