package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment

class MonthlyFragment : Fragment() {

    private lateinit var eventListView: ListView
    private val events = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_monthly, container, false)
        eventListView = view.findViewById(R.id.event_list)
        return view
    }

    fun updateEventList(events: List<String>) {
        this.events.clear()
        this.events.addAll(events)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, this.events)
        eventListView.adapter = adapter
    }
}