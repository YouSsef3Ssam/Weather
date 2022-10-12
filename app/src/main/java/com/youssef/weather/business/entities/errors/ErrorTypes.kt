package com.youssef.weather.business.entities.errors

sealed class ErrorTypes {
    data class UnKnown(val throwable: Throwable) : ErrorTypes()
    object NetworkError : ErrorTypes()
    object TimeOut : ErrorTypes()
}
