package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class EventsAdapter(private var events: List<String>) : RecyclerView.Adapter<EventsAdapter.EventViewHolder>() {

    class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val eventTextView: TextView = itemView.findViewById(R.id.text_view_event)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_event, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.eventTextView.text = events[position]
    }

    override fun getItemCount(): Int {
        return events.size
    }

    fun updateEvents(newEvents: List<String>) {
        events = newEvents
        notifyDataSetChanged()
    }
}