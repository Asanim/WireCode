package com.example.wirecode.ui.add_mapping

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.wirecode.R

val NUM_PINS : Int = 32
private lateinit var editWordView: EditText


class AddMappingFragment : Fragment() {

    private lateinit var addMappingViewModel: AddMappingViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        addMappingViewModel =
            ViewModelProviders.of(this).get(AddMappingViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_add_mapping, container, false)
        val editTitleView = root.findViewById<EditText>(R.id.edtxt_part_number)

        val button = root.findViewById<Button>(R.id.button_save)
        button.setOnClickListener {
            val replyIntent = Intent()
            if (TextUtils.isEmpty(editTitleView.text)) {
                activity?.setResult(Activity.RESULT_CANCELED, replyIntent)
            } else {
                val word = editTitleView.text.toString()
                replyIntent.putExtra(EXTRA_REPLY, word)
                activity?.setResult(Activity.RESULT_OK, replyIntent)
            }
            activity?.finish()
        }


        return root
    }

    companion object {
        const val EXTRA_REPLY = "com.example.android.wordlistsql.REPLY"
    }
}