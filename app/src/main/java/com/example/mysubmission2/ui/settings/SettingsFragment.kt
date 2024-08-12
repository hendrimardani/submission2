package com.example.mysubmission2.ui.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.example.mysubmission2.databinding.FragmentSettingsBinding
import com.example.mysubmission2.datastore.theme.SettingPreferences
import com.example.mysubmission2.datastore.theme.ThemeViewModel
import com.example.mysubmission2.datastore.theme.ViewModelFactory
import com.example.mysubmission2.datastore.theme.dataStore

class SettingsFragment : Fragment() {
    private var _binding : FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pref = SettingPreferences.getInstance(requireActivity().dataStore)
        val themeViewModel = ViewModelProvider(requireActivity(), ViewModelFactory(pref)).get(
            ThemeViewModel::class.java
        )

        themeViewModel.getThemeSettings().observe(requireActivity()) { isDarkModeActive ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding.itemSwitchDarkMode.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding.itemSwitchDarkMode.isChecked = false
            }
        }

        binding.itemSwitchDarkMode.setOnCheckedChangeListener { buttonView, isChecked ->
            themeViewModel.saveThemeSetting(isChecked)
        }
    }

}