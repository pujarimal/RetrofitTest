package com.example.apicalling

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.apicalling.databinding.ActivityMainBinding
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "https://jsonplaceholder.typicode.com"
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TodoAPI::class.java)

        binding.apply {
            progressBar.visibility = View.VISIBLE
            lifecycleScope.launchWhenCreated {
                val toDoListResponse = retrofit.getTodos()
                if(toDoListResponse.isSuccessful && toDoListResponse.body() != null) {
                    val toDoList = toDoListResponse.body()

                    rvTodos.apply {
                        val toDoAdapter = toDoList?.let { ToDoAdapter(it) }
                        adapter = toDoAdapter
                    }
                }
            }
            progressBar.visibility = View.GONE
        }
    }
}