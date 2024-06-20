package com.example.mytodoapp

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


class CustomArrayAdapter(context: Context, items: List<String>) : ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent)
        val textView = view.findViewById<TextView>(android.R.id.text1)
        textView.setTextColor(Color.WHITE)
        return view
    }
}

class MainActivity : AppCompatActivity() {
    private val PREF_NAME = "MyPref"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val todo: MutableList<String> = loadList()
        if (todo.isEmpty()) {
            todo.add("Твой список еще пуст")
        }

        val adapter = CustomArrayAdapter(this, todo)
        val listView = findViewById<ListView>(R.id.listView)
        listView.adapter = adapter

        val userData: EditText = findViewById(R.id.editTextText)
        val addBtn = findViewById<Button>(R.id.button1)
        addBtn.setOnClickListener {
            val text = userData.text.toString().trim()
            if (text.isNotEmpty()) {
                if (todo.contains("Твой список еще пуст")) {
                    todo.remove("Твой список еще пуст")
                }
                adapter.insert(text, 0)
                saveList(adapter)
            }
        }

        listView.setOnItemClickListener { adapterView, view, i, l ->
            val text = adapter.getItem(i).toString()
            adapter.remove(text)
            if(adapter.count == 0) {
                todo.add("Твой список еще пуст")
            }
            Toast.makeText(this, "Задача: $text удалена", Toast.LENGTH_LONG).show()
            saveList(adapter)
        }

        val toSecBtn = findViewById<Button>(R.id.button2)
        toSecBtn.setOnClickListener {
            startActivity(Intent(this, SecondActivity::class.java))
        }
    }


    private fun saveList(adapter: CustomArrayAdapter) {
        val sharedPref = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE) ?: return
        val listItems = List(adapter.count) { i -> adapter.getItem(i).toString() }
        with(sharedPref.edit()) {
            putStringSet("todoList", listItems.toSet())
            apply()
        }
    }

    private fun loadList(): MutableList<String> {
        val sharedPref = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPref.getStringSet("todoList", setOf<String>())?.toMutableList() ?: mutableListOf()
    }
}