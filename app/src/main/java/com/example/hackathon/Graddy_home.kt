package com.example.hackathon

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CalendarView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.hackathon.databinding.ActivityGraddyHomeBinding
import com.example.hackathon.databinding.FragmentHomeBinding
import com.example.hackathon.ui.mypage.MyPageFragment
import org.json.JSONObject

class Graddy_home : AppCompatActivity() {

    lateinit var request : StringRequest
    lateinit var queue : RequestQueue

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val binding = ActivityGraddyHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val calendarView = findViewById<CalendarView>(R.id.calendarView)
        calendarView.date = System.currentTimeMillis()  // 오늘 날짜로 초기화


    }
}