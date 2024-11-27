package com.example.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.widget.Toolbar

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // Ensure this is the correct layout

        // Set up the toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Set the title of the Action Bar
        supportActionBar?.title = "QuickUtilityApp"

        // Initialize the BottomNavigationView
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        // Check if the Fragment is already added to avoid overlapping
        if (savedInstanceState == null) {
            // Load the default fragment (MonthlyFragment or WeeklyFragment)
            loadFragment(MonthlyFragment()) // Change to WeeklyFragment() if needed
        }


        // Set listener for BottomNavigationView
        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_calculator -> {
                    loadFragment(CalculatorFragment())
                    true
                }
                R.id.nav_converter -> {
                    loadFragment(QuickConverterFragment())
                    true
                }
                R.id.nav_calendar -> {
                    loadFragment(CalendarFragment()) // Load your CalendarFragment
                    true
                }
                R.id.nav_flash_events -> {
                    loadFragment(FlashEventsFragment())
                    true
                }
                else -> false
            }
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment) // Ensure this ID exists in the XML
            .commit()
    }
}