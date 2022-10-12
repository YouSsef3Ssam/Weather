package com.youssef.weather.framework.presentation.features.weather

import android.os.Bundle
import androidx.fragment.app.viewModels
import com.youssef.weather.R
import com.youssef.weather.business.entities.TempUnit
import com.youssef.weather.business.entities.Weather
import com.youssef.weather.databinding.FragmentWeatherBinding
import com.youssef.weather.framework.presentation.features.base.BaseFragment
import com.youssef.weather.framework.utils.NetworkUtils
import com.youssef.weather.framework.utils.permission.LocationManager
import com.youssef.weather.framework.utils.permission.Permission
import com.youssef.weather.framework.utils.states.DataState
import com.youssef.weather.framework.utils.states.LocationState
import dagger.hilt.android.AndroidEntryPoint
import java.lang.ref.WeakReference
import javax.inject.Inject


@AndroidEntryPoint
class WeatherFragment : BaseFragment<FragmentWeatherBinding>() {
    private val viewModel by viewModels<WeatherViewModel>()

    @Inject
    lateinit var networkUtils: NetworkUtils

    private lateinit var locationManager: LocationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        locationManager = LocationManager(WeakReference(this))
    }

    override fun onStart() {
        super.onStart()
        requestLocationPermission()
    }

    override fun bindViews() {
        initUI()
        subscribeOnViewObservers()
    }

    private fun initUI() {
        binding.tempUnit = viewModel.getTempUnit()
        binding.celsius.setOnClickListener {
            if (!updateTemperatureUnit(TempUnit.CELSIUS))
                binding.fahrenheit.isChecked = true
        }
        binding.fahrenheit.setOnClickListener {
            if (!updateTemperatureUnit(TempUnit.FAHRENHEIT))
                binding.celsius.isChecked = true
        }
    }

    private fun updateTemperatureUnit(tempUnit: TempUnit): Boolean {
        return if (networkUtils.isNetworkAvailable()) {
            viewModel.updateTempUnit(tempUnit)
            true
        } else {
            showMessage(R.string.please_check_your_internet_connection)
            false
        }
    }

    private fun requestLocationPermission() {
        locationManager
            .request(Permission.Location)
            .onPermissionsGranted {
                when (it) {
                    is LocationState.Available -> viewModel.locationAvailable(it.location)
                    LocationState.NotAvailable -> viewModel.locationNotAvailable()
                }
            }
    }

    private fun subscribeOnViewObservers() {
        viewModel.currentWeatherDataState.observe(viewLifecycleOwner) {
            when (it) {
                is DataState.Success -> presentSuccess(it.data)
                is DataState.Failure -> presentError(getErrorMessage(it.throwable))
                DataState.Loading -> binding.loading = true
            }
        }
        viewModel.canNotGetLocation.observe(viewLifecycleOwner) {
            presentError(getString(R.string.can_not_get_location_message))
        }
    }

    private fun presentSuccess(weather: Weather) {
        binding.loading = false
        binding.weather = weather
        binding.tempUnit = viewModel.getTempUnit()
    }

    private fun presentError(errorMessage: String) {
        binding.loading = false
        binding.error = errorMessage
    }

    override fun getLayoutResId() = R.layout.fragment_weather
}