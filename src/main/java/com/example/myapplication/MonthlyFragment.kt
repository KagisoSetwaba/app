package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CalendarView
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import java.text.SimpleDateFormat
import java.util.*

class MonthlyFragment : Fragment() {

    private lateinit var eventListView: ListView
    private lateinit var selectedMonthTextView: TextView // TextView to display the selected month
    private lateinit var calendarView: CalendarView // CalendarView for date selection
    private val events = mutableListOf<String>()
    private lateinit var adapter: ArrayAdapter<String> // Adapter for the ListView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_monthly, container, false)

        eventListView = view.findViewById(R.id.event_list)
        selectedMonthTextView = view.findViewById(R.id.selected_month_text) // Initialize TextView
        calendarView = view.findViewById(R.id.calendar_view) // Initialize CalendarView

        // Initialize the adapter with the events list
        adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, events)
        eventListView.adapter = adapter

        // Set a listener for the CalendarView to update events based on selected date
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val selectedDate = Calendar.getInstance().apply {
                set(year, month, dayOfMonth)
            }
            updateEventList(selectedDate, getEventsMap()) // Pass the eventsMap
        }

        // Optionally, you can set the current month to display when the fragment is created
        val currentMonth = Calendar.getInstance()
        updateEventList(currentMonth, getEventsMap()) // Pass the eventsMap

        return view
    }

    // Update method to accept a Calendar object for the selected month and eventsMap
    fun updateEventList(selectedMonth: Calendar, eventsMap: Map<String, List<String>>) {
        val dateFormatter = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
        val monthString = dateFormatter.format(selectedMonth.time)

        // Update the displayed month
        selectedMonthTextView.text = "Events for: $monthString"

        // Retrieve events for the selected month
        val monthlyEvents = getEventsForMonth(selectedMonth, eventsMap) // Pass the eventsMap
        this.events.clear() // Clear existing events
        this.events.addAll(monthlyEvents) // Add new monthly events

        // Notify the adapter that the data has changed
        adapter.notifyDataSetChanged() // Refresh the ListView
    }

    // Method to retrieve events for a specific month
    private fun getEventsForMonth(month: Calendar, eventsMap: Map<String, List<String>>): List<String> {
        val monthlyEvents = mutableListOf<String>()

        val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        // Get the start and end dates for the selected month
        val startOfMonth = month.clone() as Calendar
        startOfMonth.set(Calendar.DAY_OF_MONTH, 1)

        val endOfMonth = month.clone() as Calendar
        endOfMonth.set(Calendar.DAY_OF_MONTH, endOfMonth.getActualMaximum(Calendar.DAY_OF_MONTH))

        // Loop through the month and collect events
        val currentDate = startOfMonth.clone() as Calendar
        while (currentDate.before(endOfMonth) || currentDate == endOfMonth) {
            val dateString = dateFormatter.format(currentDate.time)
            val eventsForDate = eventsMap[dateString] ?: emptyList()
            monthlyEvents.addAll(eventsForDate)
            currentDate.add(Calendar.DAY_OF_YEAR, 1) // Move to the next day
        }

        return monthlyEvents
    }

    // Method to retrieve the events map from the CalendarFragment
    private fun getEventsMap(): Map<String, List<String>> {
        // This method is no longer needed since we are passing the eventsMap directly
        return mapOf() // Return an empty map for now
    }
}