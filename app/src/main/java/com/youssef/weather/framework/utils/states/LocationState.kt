package com.youssef.weather.framework.utils.states

import com.youssef.weather.business.entities.LatitudeLongitude

sealed class LocationState {
    data class Available(val location: LatitudeLongitude) : LocationState()
    object NotAvailable : LocationState()
}