package com.youssef.weather.business.entities

enum class TempUnit(val value: String, val sign: Char) {
    FAHRENHEIT("imperial", 'F'),
    CELSIUS("metric", 'C')
}