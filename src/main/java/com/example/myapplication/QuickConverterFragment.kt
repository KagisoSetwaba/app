package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
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

        return view
    }

    private fun convertValue() {
        val value = inputValue.text.toString().toDoubleOrNull()
        if (value != null) {
            val selectedUnit = unitSpinner.selectedItem.toString()
            val convertedValue = when (selectedUnit) {
                "Meters to Kilometers" -> value / 1000
                "Kilograms to Grams" -> value * 1000
                "Celsius to Fahrenheit" -> (value * 9/5) + 32
                else -> value // Default case
            }
            resultView.text = convertedValue.toString()
        } else {
            resultView.text = "Invalid input"
        }
    }
}