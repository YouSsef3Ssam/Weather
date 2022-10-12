package com.youssef.weather.framework.utils

import android.widget.TextView
import androidx.databinding.BindingAdapter
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter(value = ["app:date"], requireAll = true)
fun TextView.setDate(date: Long) {
    if (date != 0L) {
        val simpleDateFormat = SimpleDateFormat("dd MMM yyyy hh:mm:ss aa")
        text = simpleDateFormat.format(Date(date.times(1000)))
    }
}