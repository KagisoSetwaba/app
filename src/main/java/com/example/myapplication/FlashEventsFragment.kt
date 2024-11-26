package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import android.app.TimePickerDialog
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.widget.EditText
import java.util.Calendar
import android.content.Intent
import android.util.Log

class FlashEventsFragment : Fragment() {

    private lateinit var eventListView: ListView
    private lateinit var fabAddEvent: FloatingActionButton
    private val events = mutableListOf<String>()
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var helpManager: HelpManager // Add HelpManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true) // Enable options menu for this fragment
        helpManager = HelpManager(requireContext()) // Initialize HelpManager
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_flash_events, container, false)

        eventListView = view.findViewById(R.id.flash_event_list)
        fabAddEvent = view.findViewById(R.id.fab_add_event)

        // Initialize the adapter
        adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, events)
        eventListView.adapter = adapter

        // Set up FAB click listener to add a new event
        fabAddEvent.setOnClickListener {
            showAddEventDialog()
        }

        // Set up item click listener for editing and deleting events
        eventListView.setOnItemClickListener { _, _, position, _ ->
            showEditDeleteDialog(position)
        }

        return view
    }

    private fun showAddEventDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Add Event")

        // Create a layout for the dialog
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_event, null)
        val inputTitle = dialogView.findViewById<EditText>(R.id.event_title)
        val inputTime = dialogView.findViewById<EditText>(R.id.event_time)

        builder.setView(dialogView)

        // Set up a TimePickerDialog
        inputTime.setOnClickListener {
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            // Create a TimePickerDialog
            TimePickerDialog(requireContext(), { _, selectedHour, selectedMinute ->
                // Format the time and set it to the EditText
                val formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
                inputTime.setText(formattedTime)
            }, hour, minute, true).show()
        }

        // Change the Positive Button to show a confirmation dialog
        builder.setPositiveButton("Add") { _, _ ->
            // This will be handled in the confirmation dialog
        }
        builder.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }

        // Show the initial dialog
        val alertDialog = builder.create()
        alertDialog.show()

        // Set up the confirmation dialog on positive button click
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            val eventTitle = inputTitle.text.toString()
            val eventTime = inputTime.text.toString()

            if (eventTitle.isNotEmpty() && eventTime.isNotEmpty()) {
                // Show confirmation dialog
                AlertDialog.Builder(requireContext())
                    .setTitle("Confirm Addition")
                    .setMessage("Are you sure you want to add the event '$eventTitle at $eventTime'?")
                    .setPositiveButton("Yes") { _, _ ->
                        // User confirmed, add the event
                        events.add("$eventTitle at $eventTime")
                        adapter.notifyDataSetChanged()
                        alertDialog.dismiss() // Dismiss the initial dialog
                    }
                    .setNegativeButton("No") { dialog, _ ->
                    // User canceled, dismiss the confirmation dialog
                    dialog.dismiss()
                }
                    .show()
            } else {
                Toast.makeText(requireContext(), "Event title and time cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showEditDeleteDialog(position: Int) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Edit or Delete Event")

        builder.setItems(arrayOf("Edit", "Delete")) { _, which ->
            when (which) {
                0 -> showEditEventDialog(position) // Edit
                1 -> {
                    events.removeAt(position)
                    adapter.notifyDataSetChanged()
                }
            }
        }
        builder.show()
    }

    private fun showEditEventDialog(position: Int) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Edit Event")

        val input = EditText(requireContext())
        input.setText(events[position])
        builder.setView(input)

        builder.setPositiveButton("Update") { _, _ ->
            val updatedTitle = input.text.toString()
            if (updatedTitle.isNotEmpty()) {
                events[position] = updatedTitle
                adapter.notifyDataSetChanged()
            } else {
                Toast.makeText(requireContext(), "Event title cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }
        builder.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
        builder.show()
    }

    private fun openSettingsDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Open Settings")
            .setMessage("Are you sure you want to open the settings?")
            .setPositiveButton("Yes") { _, _ ->
                // User confirmed, open SettingsActivity
                openSettingsActivity()
            }
            .setNegativeButton("No") { dialog, _ ->
                // User canceled, dismiss the dialog
                dialog.dismiss()
            }
            .show()
    }

    private fun openSettingsActivity() {
        if (isAdded) {
            val intent = Intent(requireContext(), SettingsActivity::class.java)
            startActivity(intent)
        } else {
            Log.e("FlashEventsFragment", "Fragment is not attached to an activity.")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu) // Inflate the menu
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                openSettingsDialog() // Open settings confirmation dialog
                true
            }
            R.id.action_help -> {
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

    override fun onDestroyView() {
        super.onDestroyView()
        events.clear() // Clear events when the view is destroyed
    }
}
