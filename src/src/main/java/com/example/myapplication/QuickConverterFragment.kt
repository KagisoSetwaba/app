package com.example.myapplication

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment

class QuickConverterFragment : Fragment() {

    private lateinit var inputValue: EditText
    private lateinit var resultView: TextView
    private lateinit var convertButton: Button
    private lateinit var unitSpinner: Spinner
    private lateinit var helpManager: HelpManager

    // SharedPreferences to manage settings
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true) // Enable options menu for this fragment

        // Initialize SharedPreferences
        sharedPreferences = requireContext().getSharedPreferences("app_preferences", Context.MODE_PRIVATE)

        // Initialize HelpManager
        helpManager = HelpManager(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_converter, container, false)

        inputValue = view.findViewById(R.id.input_value)
        resultView = view.findViewById(R.id.result_view)
        convertButton = view.findViewById(R.id.convert_button)
        unitSpinner = view.findViewById(R.id.unit_spinner)

        // Populate the Spinner with conversion options
        val units = arrayOf("Meters to Kilometers", "Kilograms to Grams", "Celsius to Fahrenheit")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, units)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        unitSpinner.adapter = adapter

        convertButton.setOnClickListener {
            convertValue()
        }

        // Load and apply settings if necessary
        loadSettings()

        return view
    }

    private fun convertValue() {
        val inputText = inputValue.text.toString().trim()

        if (inputText.isEmpty()) {
            resultView.text = "Please enter a value"
            return
        }

        val value = inputText.toDoubleOrNull()
        if (value == null) {
            resultView.text = "Invalid input. Enter a valid number."
            return
        }

        // Show confirmation dialog before proceeding with conversion
        AlertDialog.Builder(requireContext())
            .setTitle("Confirm Conversion")
            .setMessage("Are you sure you want to convert $value ${unitSpinner.selectedItem}?")
            .setPositiveButton("Yes") { _, _ ->
                // Proceed with conversion
                try {
                    val selectedUnit = unitSpinner.selectedItem.toString()
                    val convertedValue = convertLogic(value, selectedUnit)
                    resultView.text = String.format("%.2f", convertedValue)
                } catch (e: Exception) {
                    resultView.text = "Error occurred during conversion."
                }
            }
            .setNegativeButton("No") { dialog, _ ->
                // User canceled the conversion
                dialog.dismiss()
            }
            .show()
    }

    private fun convertLogic(value: Double, selectedUnit: String): Double {
        return when (selectedUnit) {
            "Meters to Kilometers" -> value / 1000
            "Kilograms to Grams" -> value * 1000
            "Celsius to Fahrenheit" -> (value * 9 / 5) + 32
            else -> throw IllegalArgumentException("Unsupported conversion type")
        }
    }

    private fun loadSettings() {
        // Load any settings from SharedPreferences if needed
        // For example, you can check for a specific setting and apply it
        val language = sharedPreferences.getString("language", "default_language")
        // Apply settings to the UI if necessary
    }

    @Suppress("DEPRECATION")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu) // Inflate the single menu
        super.onCreateOptionsMenu(menu, inflater)
    }

    @Suppress("DEPRECATION")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                // Handle settings action
                val intent = Intent(requireContext(), SettingsActivity::class .java)
                startActivity(intent)
                true
            }
            R.id.action_help -> {
                // Handle help action
                showHelpDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showHelpDialog() {
        helpManager.showHelpDialog { which ->
            when (which) {
                0 -> helpManager.showFAQs() // Show FAQs
                1 -> helpManager.contactSupport() // Contact support
                2 -> helpManager.showVersionInfo() // Show version info
                3 -> helpManager.giveFeedback() // Give feedback
            }
        }
    }
}