package com.example.hackathon

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.hackathon.databinding.FragmentHomeBinding
import org.json.JSONObject

class Graddy_home : AppCompatActivity() {

    lateinit var request : StringRequest
    lateinit var queue : RequestQueue

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val binding = FragmentHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        queue = Volley.newRequestQueue(this)
        val rvGraddy = findViewById<RecyclerView>(R.id.rvGraddy)
        rvGraddy.layoutManager = GridLayoutManager(this, 2)



    }
}