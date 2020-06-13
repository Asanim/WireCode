package com.example.wirecode.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wirecode.R
import kotlinx.coroutines.InternalCoroutinesApi

class ListFragment : Fragment() {

    @InternalCoroutinesApi
    private lateinit var listViewModel: ListViewModel
    private val newMappingActivityRequestCode = 1

    @InternalCoroutinesApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        listViewModel =
            ViewModelProviders.of(this).get(ListViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_list, container, false)

        val recyclerView = root.findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = activity?.let { MappingListAdapter(it) }

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(activity)

        listViewModel = ViewModelProvider(this).get(ListViewModel::class.java)
        listViewModel.allMappings.observe(viewLifecycleOwner, Observer { mappings ->
            // Update the cached copy of the words in the adapter.
            mappings?.let { adapter?.setWords(it) }
        })

        return root
    }


}