package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment

class DailyFragment : Fragment() {

    private lateinit var eventListView: ListView
    private lateinit var selectedDateTextView: TextView
    private val events = mutableListOf<String>() // List to hold event strings
    private lateinit var adapter: ArrayAdapter<String> // Declare adapter here

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_daily, container, false)

        eventListView = view.findViewById(R.id.event_list)
        selectedDateTextView = view.findViewById(R.id.selected_date_text)

        // Initialize the adapter with the events list
        adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, events)
        eventListView.adapter = adapter

        return view
    }

    // Update method to accept a date and a list of events
    fun updateEventList(date: String, events: List<String>) {
        this.events.clear() // Clear existing events
        this.events.addAll(events) // Add new events

        // Update the displayed date
        selectedDateTextView.text = "Events for: $date"

        // Notify the adapter that the data has changed
        adapter.notifyDataSetChanged() // Notify the adapter to refresh the ListView
    }
}