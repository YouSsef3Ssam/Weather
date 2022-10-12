package com.youssef.weather.framework.presentation.features

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.databinding.DataBindingUtil
import com.youssef.weather.R
import com.youssef.weather.databinding.ActivityMainBinding
import com.youssef.weather.framework.notification.RemindersManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding get() = _binding!!

    @Inject
    lateinit var remindersManager: RemindersManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        remindersManager.startReminder()
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}