package com.youssef.weather.framework.utils.ext

import com.youssef.weather.business.entities.errors.ErrorMessage
import com.youssef.weather.business.entities.errors.ErrorTypes
import com.youssef.weather.business.entities.errors.ErrorTypes.*
import java.net.ConnectException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

fun Throwable.getType(): ErrorTypes =
    when (this) {
        is ConnectException, is UnknownHostException -> NetworkError
        is TimeoutException -> TimeOut
        else -> UnKnown(this)
    }

fun ErrorTypes.getMessage(): ErrorMessage =
    when (this) {
        is NetworkError -> ErrorMessage(type = NetworkError)
        is TimeOut -> ErrorMessage(type = TimeOut)
        is UnKnown -> ErrorMessage(text = throwable.message, type = UnKnown(throwable))
    }

