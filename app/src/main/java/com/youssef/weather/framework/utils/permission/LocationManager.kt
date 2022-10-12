package com.youssef.weather.framework.utils.permission

import android.app.AlertDialog
import android.content.Context.LOCATION_SERVICE
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager.GPS_PROVIDER
import android.location.LocationManager.NETWORK_PROVIDER
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.LocationServices
import com.youssef.weather.R
import com.youssef.weather.business.entities.LatitudeLongitude
import com.youssef.weather.framework.utils.ext.openAppPermissionsSettings
import com.youssef.weather.framework.utils.states.LocationState
import java.lang.ref.WeakReference
import android.location.LocationManager as LocationService

class LocationManager constructor(private val fragment: WeakReference<Fragment>) {

    private val requiredPermissions = mutableListOf<Permission>()
    private var permissionsGrantedCallback: (LocationState) -> Unit = {}

    private val permissionCheck =
        fragment.get()
            ?.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result ->
                sendResult(result)
            }


    fun request(vararg permission: Permission): LocationManager {
        requiredPermissions.addAll(permission)
        return this
    }


    fun onPermissionsGranted(callback: (LocationState) -> Unit) {
        this.permissionsGrantedCallback = callback
        handlePermissionRequest()
    }

    private fun handlePermissionRequest() {
        fragment.get()?.let { fragment ->
            when {
                areAllPermissionsGranted(fragment) -> sendPositiveResult()
                shouldShowPermissionRationale(fragment) ->
                    displayPermissionDialog(
                        fragment,
                        R.string.dialog_permission_button_positive
                    ) { requestPermissions() }
                else -> requestPermissions()
            }
        }
    }

    private fun sendResult(grantResults: Map<String, Boolean>) {
        fragment.get()?.let { fragment ->
            when {
                grantResults.all { it.value } -> sendPositiveResult()
                shouldShowPermissionRationale(fragment) -> displayPermissionDialog(
                    fragment,
                    R.string.dialog_permission_button_positive
                ) { requestPermissions() }
                !shouldShowPermissionRationale(fragment) -> displayPermissionDialog(
                    fragment,
                    R.string.dialog_permission_button_positive_setting
                ) { fragment.activity?.openAppPermissionsSettings() }
            }
        }
    }

    private fun sendPositiveResult() {
        getUserLocation()
    }

    private fun getUserLocation() {
        fragment.get()?.apply {
            val locationManager =
                requireContext().getSystemService(LOCATION_SERVICE) as LocationService?
            locationManager?.let {
                val isGPSLocationExist = getGPSLocation(locationManager)
                if (isGPSLocationExist) return
                val isNetworkLocationExist = getNetworkLocation(locationManager)
                if (isNetworkLocationExist) return
                getLastKnownLocation()
            } ?: run {
                permissionsGrantedCallback(LocationState.NotAvailable)
            }
        }
    }

    private fun getGPSLocation(locationManager: LocationService): Boolean {
        val isGPSEnabled = locationManager.isProviderEnabled(GPS_PROVIDER)
        if (!isGPSEnabled) return false
        val locationGPS: Location =
            locationManager.getLastKnownLocation(GPS_PROVIDER) ?: return false
        permissionsGrantedCallback(LocationState.Available(locationGPS.map()))
        cleanUp()
        return true
    }

    private fun getNetworkLocation(locationManager: LocationService): Boolean {
        val isNetworkEnabled = locationManager.isProviderEnabled(NETWORK_PROVIDER)
        if (!isNetworkEnabled) return false
        val locationNetwork: Location =
            locationManager.getLastKnownLocation(NETWORK_PROVIDER) ?: return false
        permissionsGrantedCallback(LocationState.Available(locationNetwork.map()))
        cleanUp()
        return true
    }


    private fun getLastKnownLocation() {
        fragment.get()?.run {
            val locationService = LocationServices.getFusedLocationProviderClient(requireActivity())
            locationService.lastLocation.addOnSuccessListener {
                it?.let { location ->
                    permissionsGrantedCallback(LocationState.Available(location.map()))
                } ?: run {
                    permissionsGrantedCallback(LocationState.NotAvailable)
                }
            }
        } ?: run {
            permissionsGrantedCallback(LocationState.NotAvailable)
        }
    }

    private fun displayPermissionDialog(
        fragment: Fragment,
        @StringRes actionName: Int,
        actionToTake: () -> Unit
    ) {
        AlertDialog.Builder(fragment.requireContext())
            .setTitle(fragment.getString(R.string.dialog_permission_title))
            .setMessage(fragment.getString(R.string.dialog_permission_message))
            .setCancelable(false)
            .setPositiveButton(fragment.getString(actionName)) { _, _ ->
                actionToTake.invoke()
            }
            .show()
    }

    private fun cleanUp() {
        requiredPermissions.clear()
        permissionsGrantedCallback = {}
        fragment.clear()
    }

    private fun requestPermissions() {
        permissionCheck?.launch(getPermissionList())
    }

    private fun areAllPermissionsGranted(fragment: Fragment) =
        requiredPermissions.all { it.isGranted(fragment) }

    private fun shouldShowPermissionRationale(fragment: Fragment) =
        requiredPermissions.any { it.requiresRationale(fragment) }

    private fun getPermissionList() =
        requiredPermissions.flatMap { it.permissions.toList() }.toTypedArray()

    private fun Permission.isGranted(fragment: Fragment) =
        permissions.all { hasPermission(fragment, it) }

    private fun Permission.requiresRationale(fragment: Fragment) =
        permissions.any { fragment.shouldShowRequestPermissionRationale(it) }

    private fun hasPermission(fragment: Fragment, permission: String) = with(fragment) {
        ContextCompat.checkSelfPermission(requireContext(), permission) ==
                PackageManager.PERMISSION_GRANTED
    }

    private fun Location.map(): LatitudeLongitude = LatitudeLongitude(latitude, longitude)
}