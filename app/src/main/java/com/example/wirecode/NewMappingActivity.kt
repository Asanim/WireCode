package com.example.wirecode

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

private lateinit var editPartNumberView : EditText
private lateinit var editContainer : LinearLayout

class NewMappingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_mapping)

        editPartNumberView  = findViewById(R.id.edit_word)
        editContainer  = findViewById(R.id.xmapping_container)

        for (i in 1..32) {
            val textview : EditText = EditText(this)
            textview.hint = "Pin $i"
            textview.layoutParams =  LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT)
            editContainer.addView(textview)
        }
        val button = findViewById<Button>(R.id.button_save)
        button.setOnClickListener {
            val replyIntent = Intent()
            if (TextUtils.isEmpty(editPartNumberView.text)) {
                setResult(Activity.RESULT_CANCELED, replyIntent)
            } else {
                val word = editPartNumberView.text.toString()

                val mappingArray : ArrayList<String> = ArrayList()

                //get pin mappings
                //todo: robust?
                for (i in 1..32) {
                    val editText : EditText = editContainer.getChildAt(i-1) as EditText
                    mappingArray.add(editText.text.toString())
                }
                Log.d("add mapping", mappingArray.toString())

                replyIntent.putExtra(EXTRA_REPLY_TITLE, word)
                replyIntent.putExtra(EXTRA_REPLY_MAPPINGS, mappingArray)

                setResult(Activity.RESULT_OK, replyIntent)
            }
            finish()
        }
    }

    companion object {
        const val EXTRA_REPLY_TITLE = "com.example.wirecode.TITLE"
        const val EXTRA_REPLY_MAPPINGS = "com.example.wirecode.MAPPING_ARRAY"

    }
}