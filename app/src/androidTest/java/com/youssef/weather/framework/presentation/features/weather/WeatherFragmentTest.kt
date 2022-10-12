package com.youssef.weather.framework.presentation.features.weather

import android.widget.RadioGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.youssef.weather.R
import com.youssef.weather.business.entities.TempUnit
import com.youssef.weather.framework.presentation.features.BaseTest
import org.hamcrest.CoreMatchers.containsString
import org.junit.Test

internal class WeatherFragmentTest : BaseTest() {

    override fun startTest() {
        onView(withId(R.id.weatherFragmentLayout)).check(matches(isDisplayed()))
        onView(withId(R.id.date)).check(matches(isDisplayed()))
        onView(withId(R.id.temperatureIV)).check(matches(isDisplayed()))
        onView(withId(R.id.temperatureTV)).check(matches(isDisplayed()))
        onView(withId(R.id.windIV)).check(matches(isDisplayed()))
        onView(withId(R.id.windTV)).check(matches(isDisplayed()))
        onView(withId(R.id.humidityIV)).check(matches(isDisplayed()))
        onView(withId(R.id.humidityTV)).check(matches(isDisplayed()))
        onView(withId(R.id.tempRG)).check(matches(isDisplayed()))
        onView(withId(R.id.celsius)).check(matches(isDisplayed()))
        onView(withId(R.id.fahrenheit)).check(matches(isDisplayed()))
    }

    @Test
    fun checkThatTemperatureUnitMatchSelectedTemperatureUnit() {
        when (activity.findViewById<RadioGroup>(R.id.tempRG).checkedRadioButtonId) {
            R.id.celsius -> onView(withId(R.id.temperatureTV)).check(
                matches(withText(containsString(TempUnit.CELSIUS.sign.toString())))
            )
            R.id.fahrenheit -> onView(withId(R.id.temperatureTV)).check(
                matches(withText(containsString(TempUnit.FAHRENHEIT.sign.toString())))
            )
        }
    }

    @Test
    fun changeTemperatureUnit() {
        when (activity.findViewById<RadioGroup>(R.id.tempRG).checkedRadioButtonId) {
            R.id.celsius -> {
                onView(withId(R.id.fahrenheit)).perform(click())
                onView(withId(R.id.temperatureTV)).check(
                    matches(withText(containsString(TempUnit.FAHRENHEIT.sign.toString())))
                )
            }
            R.id.fahrenheit -> {
                onView(withId(R.id.celsius)).perform(click())
                onView(withId(R.id.temperatureTV)).check(
                    matches(withText(containsString(TempUnit.CELSIUS.sign.toString())))
                )
            }
        }
    }
}