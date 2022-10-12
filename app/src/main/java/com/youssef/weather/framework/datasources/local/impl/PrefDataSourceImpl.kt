package com.youssef.weather.framework.datasources.local.impl

import com.iamhabib.easy_preference.EasyPreference
import com.youssef.weather.business.entities.LatitudeLongitude
import com.youssef.weather.business.entities.TempUnit
import com.youssef.weather.framework.datasources.local.abstraction.PrefDataSource
import com.youssef.weather.framework.utils.Constants.PrefKeys.LOCATION
import com.youssef.weather.framework.utils.Constants.PrefKeys.TEMPERATURE_UNIT
import javax.inject.Inject

class PrefDataSourceImpl @Inject constructor(private val preference: EasyPreference.Builder) :
    PrefDataSource {
    override fun saveLocation(location: LatitudeLongitude) {
        preference.addObject(LOCATION, location).save()
    }

    override fun getLocation(): LatitudeLongitude? =
        preference.getObject(LOCATION, LatitudeLongitude::class.java)

    override fun saveTemperatureUnit(tempUnit: TempUnit) {
        preference.addObject(TEMPERATURE_UNIT, tempUnit).save()
    }

    override fun getTemperatureUnit(): TempUnit =
        preference.getObject(TEMPERATURE_UNIT, TempUnit::class.java) ?: TempUnit.CELSIUS
}