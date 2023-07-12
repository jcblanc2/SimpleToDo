package com.example.simpletodo.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.simpletodo.R
import com.example.simpletodo.adapters.TaskItemAdapter

class EditActivity  : AppCompatActivity() {

    lateinit var inputTextField : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        // Setup the button, input field and textView
        inputTextField = findViewById<EditText>(R.id.editItemField)
        val tvEditText = findViewById<TextView>(R.id.tvEditItemBelow)

        // Access data passed in
        inputTextField.setText(getIntent().getStringExtra("item"));

        findViewById<Button>(R.id.buttonSave).setOnClickListener {
            onSubmit()
        }

    }

    // closes the EditActivity and returns to Main screen
    fun onSubmit() {
        // Prepare data intent
        val data = Intent()
        // Pass relevant data back as a result
        data.putExtra("itemUpdate", inputTextField.text.toString())
        // Activity finished ok, return the data
        setResult(RESULT_OK, data) // set result code and bundle data for response
        finish() // closes the activity, pass data to parent
    }
}