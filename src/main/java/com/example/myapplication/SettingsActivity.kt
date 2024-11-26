package com.example.myapplication

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate

class SettingsActivity : AppCompatActivity() {

    private lateinit var settingsListView: ListView
    private val settingsOptions = arrayOf(
        "Option 1: Change Theme",
        "Option 2: Notifications",
        "Option 3: Privacy Policy",
        "Option 4: About"
    )

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("app_preferences", MODE_PRIVATE)

        // Load saved theme preference
        val themePreference = sharedPreferences.getString("theme", "light")
        setAppTheme(themePreference)

        setContentView(R.layout.activity_settings)

        settingsListView = findViewById(R.id.settings_list)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, settingsOptions)
        settingsListView.adapter = adapter

        // Handle clicks on settings options
        settingsListView.setOnItemClickListener { _, _, position, _ ->
            when (position) {
                0 -> changeTheme() // Handle Option 1: Change Theme
                1 -> toggleNotifications() // Handle Option 2: Notifications
                2 -> showPrivacyPolicy() // Handle Option 4: Privacy Policy
                3 -> showAbout() // Handle Option 5: About
            }
        }
    }

    private fun setAppTheme(theme: String?) {
        when (theme) {
            "dark" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            "light" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            else -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    private fun changeTheme() {
        // Show a dialog to select a theme
        val themes = arrayOf("Light", "Dark")
        AlertDialog.Builder(this)
            .setTitle("Select Theme")
            .setItems(themes) { _, which ->
                when (which) {
                    0 -> {
                        // Set Light Theme
                        sharedPreferences.edit().putString("theme", "light").apply()
                        setAppTheme("light") // Apply the light theme immediately
                        Toast.makeText(this, "Theme changed to Light", Toast.LENGTH_SHORT).show()
                    }
                    1 -> {
                        // Set Dark Theme
                        sharedPreferences.edit().putString("theme", "dark").apply()
                        setAppTheme("dark") // Apply the dark theme immediately
                        Toast.makeText(this, "Theme changed to Dark", Toast.LENGTH_SHORT).show()
                    }
                }
                // Recreate the activity to apply the theme
                recreate()
            }
            .show()
    }

    private fun toggleNotifications() {
        // Get current notification preference
        val isEnabled = sharedPreferences.getBoolean("notifications_enabled", true)

        // Show a dialog to toggle notifications
        AlertDialog.Builder(this)
            .setTitle("Notifications")
            .setMessage(if (isEnabled) "Disable notifications?" else "Enable notifications?")
            .setPositiveButton("Yes") { _, _ ->
                // Toggle the notification preference
                sharedPreferences.edit().putBoolean("notifications_enabled", !isEnabled).apply()
                Toast.makeText(this, "Notifications ${if (isEnabled) "disabled" else "enabled"}", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun showPrivacyPolicy() {
        // Show the privacy policy in a dialog
        val privacyPolicy = "This is the privacy policy of the application. Please read it carefully."
        AlertDialog.Builder(this)
            .setTitle("Privacy Policy")
            .setMessage(privacyPolicy)
            .setPositiveButton("OK", null)
            .show()
    }

    private fun showAbout() {
        // Show about information in a dialog
        val aboutInfo = "App Name: My Application\nVersion: 1.0\nDeveloped by: Your Name"
        AlertDialog.Builder(this)
            .setTitle("About")
            .setMessage(aboutInfo)
            .setPositiveButton("OK", null)
            .show()
    }
}