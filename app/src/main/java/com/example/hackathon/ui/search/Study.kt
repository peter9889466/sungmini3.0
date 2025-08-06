package com.example.hackathon.ui.search

data class Study(
    val id: String,
    val name: String,
    val description: String,
    val category: String,
    val creator: String,
    val startDate: String = "",
    val endDate: String = ""
)