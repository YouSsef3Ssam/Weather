package com.youssef.weather.framework.utils.permission

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION

sealed class Permission(vararg val permissions: String) {
    object Location : Permission(ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION)
    companion object {
        fun from(permission: String) = when (permission) {
            ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION -> Location
            else -> throw IllegalArgumentException("Unknown permission: $permission")
        }
    }
}