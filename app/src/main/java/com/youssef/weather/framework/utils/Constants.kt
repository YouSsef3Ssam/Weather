package com.youssef.weather.framework.utils

import com.youssef.weather.BuildConfig
import com.youssef.weather.framework.utils.Constants.Network.Versions.VERSION

interface Constants {
    object Network {
        const val BASE_URL: String = BuildConfig.HOST

        object Versions {
            const val VERSION = "2.5"
        }

        object EndPoints {
            const val WEATHER = "$VERSION/weather"
        }

        object Query {
            const val LATITUDE = "lat"
            const val LONGITUDE = "lon"
            const val UNITS = "units"
        }

        const val API_KEY = "appid"
        const val API_KEY_VALUE = "b967b6ee8812e0f6ad9aeaa04c6466ae"
    }

    object LocalDatabase {
        const val DATABASE_NAME = "weather_database"

        object Tables {
            const val WEATHER = "weather"
        }
    }

    object PrefKeys {
        const val PREF_KEY = "PREF_KEY"
        const val LOCATION = "LOCATION"
        const val TEMPERATURE_UNIT = "TEMPERATURE_UNIT"
    }

}