package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_calculator, container, false)
        workingTextView = view.findViewById(R.id.workingTextView)
        resultTextView = view.findViewById(R.id.resultTextView)

        // Set up button click listeners
        setupButtonListeners(view)

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
    }
}