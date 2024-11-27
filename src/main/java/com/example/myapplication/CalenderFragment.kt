package com.example.myapplication

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.*

class CalendarFragment : Fragment() {

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager
    private lateinit var fabAddEvent: FloatingActionButton
    private val eventsMap = mutableMapOf<String, MutableList<String>>() // To hold events for each date
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private lateinit var eventTextView: TextView // TextView to display events

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_calender, container, false)
        tabLayout = view.findViewById(R.id.tab_layout)
        viewPager = view.findViewById(R.id.view_pager)
        fabAddEvent = view.findViewById(R.id.fab_add_event)
        eventTextView = view.findViewById(R.id.text_view_events) // Initialize TextView

        // Set up ViewPager with an adapter for daily, weekly, and monthly views
        val adapter = CalendarPagerAdapter(childFragmentManager)
        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)

        fabAddEvent.setOnClickListener {
            showDatePicker()
        }

        // Add a listener to update fragments when the tab is changed
        viewPager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                // Get the currently selected date (you might want to store this)
                val selectedDate = dateFormat.format(Date())
                updateEventList(selectedDate)
            }
        })

        return view
    }

    private fun showDatePicker() {
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select a date")
            .build()

        datePicker.addOnPositiveButtonClickListener { selection ->
            val selectedDate = dateFormat.format(Date(selection))
            addEvent(selectedDate)
        }

        datePicker.show(parentFragmentManager, "datePicker")
    }

    private fun addEvent(date: String) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Add Event")

        val input = EditText(requireContext())
        builder.setView(input)

        builder.setPositiveButton("OK") { dialog, _ ->
            val eventTitle = input.text.toString()
            if (eventTitle.isNotEmpty()) {
                val event = "$eventTitle on $date"
                eventsMap.getOrPut(date) { mutableListOf() }.add(event)
                Log.d("CalendarFragment", "Event added: $event") // Log added event
                Log.d("CalendarFragment", "Current eventsMap: $eventsMap") // Log current eventsMap
                updateEventList(date) // Update the current fragment with the new event
            }
            dialog.dismiss()
        }
        builder.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }

        builder.show()
    }

    private fun updateEventList(date: String) {
        // Get the current fragment
        val currentFragment = (viewPager.adapter as CalendarPagerAdapter).getCurrentFragment(viewPager.currentItem)

        // Update Monthly Fragment
        if (currentFragment is MonthlyFragment) {
            val monthlyEvents = getMonthlyEvents(date)
            currentFragment.updateEventList(Calendar.getInstance(), eventsMap) // Pass the eventsMap
        }

        // Display events for the selected date in the TextView
        val eventsForDate = eventsMap[date] ?: emptyList()
        eventTextView.text = if (eventsForDate.isNotEmpty()) {
            "Events for $date:\n" + eventsForDate.joinToString("\n ")
        } else {
            "No events for this date."
        }
    }

    private fun getDailyEvents(date: String): List<String> {
        return eventsMap[date] ?: emptyList()
    }

    private fun getWeeklyEvents(date: String): List<String> {
        val calendar = Calendar.getInstance()
        calendar.time = dateFormat.parse(date) ?: Date()
        val weekEvents = mutableListOf<String>()

        // Get events for the current week
        for (i in 0..6) { // 0 to 6 for the week
            val day = dateFormat.format(calendar.time)
            weekEvents.addAll(eventsMap[day] ?: emptyList())
            calendar.add(Calendar.DAY_OF_MONTH, 1) // Move to the next day
        }
        return weekEvents
    }

    private fun getMonthlyEvents(date: String): List<String> {
        val calendar = Calendar.getInstance()
        calendar.time = dateFormat.parse(date) ?: Date()
        val monthEvents = mutableListOf<String>()

        // Get events for the current month
        val month = calendar.get(Calendar.MONTH)
        val year = calendar.get(Calendar.YEAR)

        for (day in 1..calendar.getActualMaximum(Calendar.DAY_OF_MONTH)) {
            calendar.set(Calendar.DAY_OF_MONTH, day)
            val dayString = dateFormat.format(calendar.time)
            monthEvents.addAll(eventsMap[dayString] ?: emptyList())
        }
        return monthEvents
    }
}

