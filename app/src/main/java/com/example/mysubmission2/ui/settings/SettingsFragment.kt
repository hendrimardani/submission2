package com.example.mysubmission2.ui.settings

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.example.mysubmission2.databinding.FragmentSettingsBinding
import com.example.mysubmission2.datastore.theme.SettingPreferences
import com.example.mysubmission2.datastore.theme.ThemeViewModel
import com.example.mysubmission2.datastore.theme.ViewModelFactory
import com.example.mysubmission2.datastore.theme.dataStore
import com.example.mysubmission2.workmanager.MyWorker
import java.util.concurrent.TimeUnit

class SettingsFragment : Fragment() {
    private var _binding : FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private lateinit var periodicWorkRequest: PeriodicWorkRequest
    private lateinit var workManager: WorkManager

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(requireActivity(), "Notifications permission granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireActivity(), "Notifications permission rejected", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (Build.VERSION.SDK_INT >= 33)
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)

        workManager = WorkManager.getInstance(requireActivity())


        val pref = SettingPreferences.getInstance(requireActivity().dataStore)
        val themeViewModel = ViewModelProvider(requireActivity(), ViewModelFactory(pref)).get(
            ThemeViewModel::class.java
        )

        getSetDarkMode(themeViewModel)
        getSetNotifications()
    }

    private fun getSetNotifications() {
        binding.itemSwitchNotifications.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                startPeriodicTask()
                Toast.makeText(requireActivity(), "Notifikasi Aktif", Toast.LENGTH_SHORT).show()
            } else {
                cancelPeriodicTask()
                Toast.makeText(requireActivity(), "Notifikasi Tidak Aktif", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun startPeriodicTask() {
        val data = Data.Builder()
            .build()
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        periodicWorkRequest = PeriodicWorkRequest.Builder(MyWorker::class.java, 60, TimeUnit.MINUTES)
            .setInputData(data)
            .setConstraints(constraints)
            .build()
        workManager.enqueue(periodicWorkRequest)
    }

    private fun cancelPeriodicTask() = workManager.cancelWorkById(periodicWorkRequest.id)

    private fun getSetDarkMode(themeViewModel: ThemeViewModel) {
        themeViewModel.getThemeSettings().observe(requireActivity()) { isDarkModeActive ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding.itemSwitchDarkMode.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding.itemSwitchDarkMode.isChecked = false
            }
        }
        binding.itemSwitchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                themeViewModel.saveThemeSetting(isChecked)
                Toast.makeText(requireActivity(), "Dark Mode Aktif", Toast.LENGTH_SHORT).show()
            } else {
                themeViewModel.saveThemeSetting(isChecked)
                Toast.makeText(requireActivity(), "Dark Mode Tidak Aktif", Toast.LENGTH_SHORT).show()
            }
        }
    }
}