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

class WeeklyFragment : Fragment() {

    private lateinit var eventListView: ListView
    private lateinit var selectedWeekTextView: TextView // TextView to display selected week
    private lateinit var calendarView: CalendarView // CalendarView for date selection
    private val events = mutableListOf<String>() // List to hold event strings
    private lateinit var adapter: ArrayAdapter<String> // Adapter for the ListView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_weekly, container, false)

        eventListView = view.findViewById(R.id.event_list)
        selectedWeekTextView = view.findViewById(R.id.selected_date_text) // Initialize TextView
        calendarView = view.findViewById(R.id.calendar_view) // Initialize CalendarView

        // Initialize the adapter with the events list
        adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, events)
        eventListView.adapter = adapter

        // Set a listener for the CalendarView to update events based on selected date
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val selectedDate = Calendar.getInstance().apply {
                set(year, month, dayOfMonth)
            }
            // Calculate the start and end of the week
            val startOfWeek = getStartOfWeek(selectedDate)
            val endOfWeek = getEndOfWeek(selectedDate)
            // Update the event list for the week
            updateEventList(startOfWeek, endOfWeek) // Change visibility to public
        }

        // Optional: Handle clicks on the event list
        eventListView.setOnItemClickListener { _, _, position, _ ->
            val event = events[position]
            // Handle event click (e.g., show event details)
        }

        return view
    }

    // Change method visibility from private to public
    fun updateEventList(startDate: Calendar, endDate: Calendar) {
        val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val startDateString = dateFormatter.format(startDate.time)
        val endDateString = dateFormatter.format(endDate.time)

        // Update the displayed week range
        selectedWeekTextView.text = "Events from: $startDateString to $endDateString"

        // Retrieve events for the selected week
        val weeklyEvents = getEventsForWeek(startDate, endDate)
        this.events.clear() // Clear existing events
        this.events.addAll(weeklyEvents) // Add new weekly events

        // Notify the adapter that the data has changed
        adapter.notifyDataSetChanged() // Refresh the ListView
    }

    // Method to retrieve events for a specific week
    private fun getEventsForWeek(startDate: Calendar, endDate: Calendar): List<String> {
        // Placeholder for actual event retrieval logic
        // This should interact with the main CalendarFragment's eventsMap
        val eventsMap: Map<String, List<String>> = getEventsMap() // Retrieve the eventsMap from the CalendarFragment
        val weeklyEvents = mutableListOf<String>()

        val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        // Loop through the week and collect events
        val currentDate = startDate.clone() as Calendar
        while (currentDate.before(endDate) || currentDate == endDate) {
            val dateString = dateFormatter.format(currentDate.time)
            val eventsForDate = eventsMap[dateString] ?: emptyList()
            weeklyEvents.addAll(eventsForDate)
            currentDate.add(Calendar.DAY_OF_YEAR, 1) // Move to the next day
        }

        return weeklyEvents
    }

    // Helper method to get the start of the week
    public fun getStartOfWeek(date: Calendar): Calendar {
        val calendar = date.clone() as Calendar
        calendar.set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek)
        return calendar
    }

    // Helper method to get the end of the week
    public fun getEndOfWeek(date: Calendar): Calendar {
        val calendar = date.clone() as Calendar
        calendar.set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek)
        calendar.add(Calendar.DAY_OF_WEEK, 6) // 6 days later for the end of the week
        return calendar
    }

    // Method to retrieve the events map from the CalendarFragment
    private fun getEventsMap(): Map<String, List<String>> {
        // This method should be implemented to retrieve the eventsMap from the CalendarFragment
        // For example, you might use a shared ViewModel or a callback interface to get the data
        return mapOf() // Return an empty map for now
    }
}