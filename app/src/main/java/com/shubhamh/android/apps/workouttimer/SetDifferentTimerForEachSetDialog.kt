package com.shubhamh.android.apps.workouttimer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton

class SetDifferentTimerForEachSetDialog() : DialogFragment(), SetsTimerAdapter.AdapterCallbacks {
    private var recyclerView: RecyclerView? = null
    private lateinit var countersViewModel: CountersViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(
            R.layout.set_different_timer_for_each_set,
            container, /* attachToRoot= */
            false
        )

        countersViewModel =
            ViewModelProviders.of(requireActivity()).get(CountersViewModel::class.java)

        countersViewModel.apply {
            setWithTimerMap.observe(requireActivity(), { recyclerView?.adapter?.notifyDataSetChanged() })
        }

        dialog?.setCanceledOnTouchOutside(false)
        view.findViewById<MaterialButton>(R.id.okButton).setOnClickListener { dialog?.dismiss() }
        setupRecyclerView(view)
        return view
    }

    private fun setupRecyclerView(view: View) {
        val data = countersViewModel.setWithTimerMap.value
        if (data != null) {
            val adapter = SetsTimerAdapter(data, this)
            view.findViewById<RecyclerView>(R.id.recyclerView).apply {
                recyclerView = this
                layoutManager = LinearLayoutManager(context)
                this.adapter = adapter
            }
        }
    }

    override fun increaseTimerForSet(set: Int) {
        countersViewModel.updateSetWithTimerMap(requireContext(), set, true)
    }

    override fun decreaseTimerForSet(set: Int) {
        countersViewModel.updateSetWithTimerMap(requireContext(), set, false)
    }
}