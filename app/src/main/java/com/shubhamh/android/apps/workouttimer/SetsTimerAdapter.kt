package com.shubhamh.android.apps.workouttimer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton

class SetsTimerAdapter(private val data: HashMap<Int, Int>, private val callbacks: AdapterCallbacks) :
    RecyclerView.Adapter<SetsTimerAdapter.ViewHolder>() {

    interface AdapterCallbacks {
        fun increaseTimerForSet(set: Int)

        fun decreaseTimerForSet(set: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.counter_template_with_title, parent, /* attachToRoot= */ false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = "Set " + (position + 1)
        holder.counterText.text = "" + data[position]
        holder.plusButton.setOnClickListener {
            callbacks.increaseTimerForSet(position)
        }
        holder.minusButton.setOnClickListener {
            callbacks.decreaseTimerForSet(position)
        }
    }

    override fun getItemCount()= data.size

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val title = itemView.findViewById<TextView>(R.id.title)
        val counterTemplate = itemView.findViewById<ViewGroup>(R.id.counterTemplate)
        val counterText = counterTemplate.findViewById<TextView>(R.id.countText)
        val plusButton = counterTemplate.findViewById<MaterialButton>(R.id.plusButton)
        val minusButton = counterTemplate.findViewById<MaterialButton>(R.id.minusButton)
    }
}