package com.example.simpletodo.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.simpletodo.R
import com.example.simpletodo.adapters.TaskItemAdapter
import java.io.File
import java.io.IOException
import java.nio.charset.Charset
import org.apache.commons.io.FileUtils

class MainActivity : AppCompatActivity() {

    var listOfTask = mutableListOf<String>()
    lateinit var adapter : TaskItemAdapter
    var index: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val onLongClickListener = object : TaskItemAdapter.OnLongClickListener {
            override fun onItemLongClicked(position: Int) {
                // Remove the item from the list
                listOfTask.removeAt(position)
                // Notify the adapter that our data set changed
                adapter.notifyDataSetChanged()

                saveItems()
            }
        }

        // Set onClickListener
        val onClickListener = object : TaskItemAdapter.OnClickListener{
            override fun onItemClicked(position: Int) {
                index = position
                launchEditActivity(listOfTask[position])
            }
        }

        loadItems()
        // Look up the recyclerView in layout
        val recyclerView = findViewById<RecyclerView>(R.id.rvItems)

        // Create a adapter passing in the sample user
        adapter = TaskItemAdapter(listOfTask, onLongClickListener, onClickListener)
        // Attach the adapter to the recyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Setup the button and input field
        val inputTextField = findViewById<EditText>(R.id.addTaskField)

        // Set an onClickListener
        findViewById<Button>(R.id.buttonAdd).setOnClickListener {
            val userInputTask = inputTextField.text.toString()

            // Add the string to our list
            listOfTask.add(userInputTask)

            // Notify the adapter that our data has benn updated
            adapter.notifyItemInserted(listOfTask.size - 1)

            // Reset Text field
            inputTextField.setText("")

            saveItems()
        }

        // Returning Data Result to MainActivity
    }

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

    var editActivityResultLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        // If the user comes back to this activity from EditActivity
        // with no error or cancellation
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            // Get the data passed from EditActivity
            if (data != null) {
                val itemUpdate = data.extras?.getString("itemUpdate")
                if (!itemUpdate.isNullOrEmpty()) {
                    listOfTask.set(index, itemUpdate)
                    adapter.notifyDataSetChanged()

                    saveItems()
                }
            }
        }
    }

    // navigate to EditActivity
    fun launchEditActivity(item: String) {
        val intent = Intent(this, EditActivity::class.java)
        intent.putExtra("item", item)
        editActivityResultLauncher.launch(intent)
    }
}