package com.example.simpletodo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import java.io.IOException
import java.nio.charset.Charset
import java.util.*
import org.apache.commons.io.FileUtils

class MainActivity : AppCompatActivity() {

    var listOfTask = mutableListOf<String>()
    lateinit var adapter : TaskItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val onLongClickListener = object : TaskItemAdapter.OnLongClickListener{
            override fun onItemLongClicked(position: Int) {
                // Remove the item from the list
                listOfTask.removeAt(position)
                // Notify the adapter that our data set changed
                adapter.notifyDataSetChanged()

                saveItems()
            }

        }


        loadItems()
        // Look up the recyclerView in layout
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

        // Create a adapter passing in the sample user
        adapter = TaskItemAdapter(listOfTask, onLongClickListener)
        // Attach the adapter to the recyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)


        // Setup the button and input field
        val inputTextField = findViewById<EditText>(R.id.addTaskField)

        // Set an onClickListener
        findViewById<Button>(R.id.button).setOnClickListener {
            val userInputTask = inputTextField.text.toString()

            // Add the string to our list
            listOfTask.add(userInputTask)

            // Notify the adapter that our data has benn updated
            adapter.notifyItemInserted(listOfTask.size - 1)

            // Reset Text field
            inputTextField.setText("")

            saveItems()
        }
    }

    // Save the data that the user has inputted
    // Save data by writing and reading a file

    // Get the file we need
    fun getDataFile() : File{
        return File(filesDir, "data.txt")
    }


    // Load the items by reading every line in the data file
    fun loadItems(){
        try {
            listOfTask = FileUtils.readLines(getDataFile(), Charset.defaultCharset())
        }catch (ioException:IOException){
            ioException.printStackTrace()
        }
    }


    //Save items by writing into our data file
    fun saveItems(){
        try {
            FileUtils.writeLines(getDataFile(), listOfTask)
        }catch (ioException:IOException){
            ioException.printStackTrace()
        }
    }

}