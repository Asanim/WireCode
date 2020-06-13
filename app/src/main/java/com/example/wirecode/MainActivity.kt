package com.example.wirecode

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.wirecode.database.Mapping
import com.example.wirecode.database.MappingDatabase
import com.example.wirecode.ui.list.ListViewModel
import com.example.wirecode.ui.list.MappingListAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.InternalCoroutinesApi

private const val newMappingActivityRequestCode = 1
private const val TAG = "Main Activity"

class MainActivity : AppCompatActivity() {
    @InternalCoroutinesApi
    private lateinit var listViewModel: ListViewModel
    private val newMappingActivityRequestCode = 1

    @InternalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        Thread.setDefaultUncaughtExceptionHandler(TopExceptionHandler(this))

        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_list)
        //setup database
        val db = Room.databaseBuilder(
            this,
            MappingDatabase::class.java, "database-name"
        ).build()

        listViewModel = ViewModelProvider(this).get(ListViewModel::class.java)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = this?.let { MappingListAdapter(it) }

        recyclerView.adapter = adapter

        adapter.setOnItemClickListener(object : MappingListAdapter.ClickListener {
            override fun onItemClick(position: Int, v: View?) {
                Log.d(TAG, "onItemClick position: $position")

                //todo: make sure retrieved and clicked mapping are the same
                val mapping = listViewModel.allMappings.value?.get(position)

                val intent = Intent(this@MainActivity, SerialMonitor::class.java)
                intent.putExtra(EXTRA_SERIAL_PART, mapping?.partNumber)
                intent.putExtra(EXTRA_SERIAL_MAPPING, mapping?.pinMapping)
                startActivity(intent)
            }
        })

        recyclerView.layoutManager = LinearLayoutManager(this)

        listViewModel.allMappings.observe(this, Observer { mappings ->
            // Update the cached copy of the words in the adapter.
            mappings?.let { adapter?.setWords(it) }
        })
        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this@MainActivity, NewMappingActivity::class.java)
            startActivityForResult(intent, newMappingActivityRequestCode)
        }
    }


    @InternalCoroutinesApi
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == newMappingActivityRequestCode && resultCode == Activity.RESULT_OK) {

            val title =  data!!.getStringExtra(NewMappingActivity.EXTRA_REPLY_TITLE)
            val pinList : ArrayList<String> =  data.getStringArrayListExtra(NewMappingActivity.EXTRA_REPLY_MAPPINGS)

            val mapping = Mapping(title, pinList)
            listViewModel.insert(mapping)
        } else {
            Toast.makeText(
                applicationContext,
                R.string.empty_not_saved,
                Toast.LENGTH_LONG).show()
        }
    }
    companion object {
        const val EXTRA_SERIAL_PART = "com.example.wirecode.SERIAL_PART"
        const val EXTRA_SERIAL_MAPPING = "com.example.wirecode.SERIAL_MAPPING"

    }


}
