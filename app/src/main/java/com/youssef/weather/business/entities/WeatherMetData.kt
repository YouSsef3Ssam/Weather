package com.youssef.weather.business.entities

import com.google.gson.annotations.SerializedName

class WeatherMetData(
    @SerializedName("id") val id: Long,
    @SerializedName("main") val main: String,
    @SerializedName("description") val description: String
)