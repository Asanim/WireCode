package com.example.wirecode.ui.dashboard

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.wirecode.R
import java.io.File
import java.io.FileWriter

class DashboardFragment : Fragment() {

    private lateinit var dashboardViewModel: DashboardViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
            ViewModelProviders.of(this).get(DashboardViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)
        val textView: TextView = root.findViewById(R.id.text_dashboard)
        dashboardViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        testData(textView)
        return root
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun testData(textView: TextView) {
        val filename = "config"
        val fileContents = "Hello world!"
        val textdash = StringBuilder()

        context?.openFileOutput(filename, Context.MODE_PRIVATE).use {
            it?.write(fileContents.toByteArray())
        }
        context?.openFileInput(filename).use { stream ->
            val text = stream?.bufferedReader().use {
                it?.readText()
            }
            Log.d("TAG", "LOADED: $text")
            textdash.append(text.toString())
        }

        Log.d("dashboard text", textdash.toString())
        textView.text = textdash.toString()
    }
}