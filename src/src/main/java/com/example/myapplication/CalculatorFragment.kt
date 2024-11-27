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
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment

class CalculatorFragment : Fragment() {

    private lateinit var workingTextView: TextView
    private lateinit var resultTextView: TextView
    private var operator: String? = null
    private var firstValue: String? = null
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
        val view = inflater.inflate(R.layout.fragment_calculator, container, false)
        workingTextView = view.findViewById(R.id.workingTextView)
        resultTextView = view.findViewById(R.id.resultTextView)

        // Set up button click listeners
        setupButtonListeners(view)

        // Load and apply settings if necessary
        loadSettings()

        return view
    }

    private fun setupButtonListeners(view: View) {
        val buttonIds = listOf(
            R.id.button0, R.id.button1, R.id.button2, R.id.button3,
            R.id.button4, R.id.button5, R.id.button6, R.id.button7,
            R.id.button8, R.id.button9, R.id.buttonAdd, R.id.buttonSubtract,
            R.id.buttonMultiply, R.id.buttonDivide, R.id.buttonEqual, R.id.buttonClear
        )

        for (id in buttonIds) {
            view.findViewById<Button>(id).setOnClickListener { onButtonClick(it) }
        }
    }

    private fun onButtonClick(view: View) {
        when (val buttonText = (view as Button).text.toString()) {
            "C" -> clear()
            "=" -> calculateResult()
            else -> appendToWorkingText(buttonText)
        }
    }

    private fun clear() {
        workingTextView.text = ""
        resultTextView.text = ""
        operator = null
        firstValue = null
    }

    private fun appendToWorkingText(value: String) {
        // Check if the value is a number
        if (value in listOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9")) {
            workingTextView.append(value)
        } else {
            // If it's an operator and firstValue is not set, set it
            if (firstValue == null) {
                firstValue = workingTextView.text.toString()
                operator = value
                workingTextView.append(value)
            }
        }
    }

    private fun calculateResult() {
        // Create an AlertDialog to confirm the calculation
        val dialogBuilder = AlertDialog.Builder(requireContext())
        dialogBuilder.setTitle("Confirm Calculation")
        dialogBuilder.setMessage("Are you sure you want to calculate the result?")

        dialogBuilder.setPositiveButton("Yes") { dialog, _ ->
            // User confirmed, proceed with calculation
            val secondValue = workingTextView.text.toString().substringAfterLast(operator ?: "")
            if (firstValue != null && operator != null && secondValue.isNotEmpty()) {
                val result = when (operator) {
                    "+" -> firstValue!!.toDouble() + secondValue.toDouble()
                    "-" -> firstValue!!.toDouble() - secondValue.toDouble()
                    "*" -> firstValue!!.toDouble() * secondValue.toDouble()
                    "/" -> firstValue!!.toDouble() / secondValue.toDouble()
                    else -> 0.0
                }
                resultTextView.text = result.toString()
                // Reset for the next calculation
                firstValue = null
                operator = null
            }
            dialog.dismiss()
        }

        dialogBuilder.setNegativeButton("No") { dialog, _ ->
            // User canceled the calculation dialog.dismiss()
        }

        // Show the dialog
        val alertDialog = dialogBuilder.create()
        alertDialog.show()
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
                val intent = Intent(requireContext(), SettingsActivity::class.java)
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