package com.example.hackathon.ui.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hackathon.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var studyAdapter: StudyAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val root: View = binding.root

        Log.d("SearchFragment", "SearchFragment 생성됨")
        
        setupRecyclerView()
        setupSearchFunction()
        loadSampleData()

        return root
    }

    private fun setupRecyclerView() {
        studyAdapter = StudyAdapter()
        binding.studyRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = studyAdapter
        }
    }

    private fun setupSearchFunction() {
        binding.searchButton.setOnClickListener {
            val query = binding.searchEditText.text.toString()
            Log.d("SearchFragment", "result: $query")
            
            // 검색 기능 구현
            if (query.isNotEmpty()) {
                searchStudies(query)
            } else {
                loadSampleData()
            }
        }
    }

    private fun searchStudies(query: String) {
        val allStudies = listOf(
            Study("알고리즘 스터디", "코딩테스트 대비를 위한 알고리즘 문제 해결 스터디입니다."),
            Study("안드로이드 개발 스터디", "Kotlin과 Android를 활용한 앱 개발을 함께 공부합니다."),
            Study("웹 개발 스터디", "React와 Node.js를 이용한 풀스택 웹 개발 스터디입니다."),
            Study("데이터 사이언스 스터디", "Python을 활용한 데이터 분석과 머신러닝을 학습합니다."),
            Study("자바 스터디", "Java 기초부터 심화까지 함께 공부하는 스터디입니다."),
            Study("파이썬 스터디", "Python 프로그래밍 언어를 배우는 스터디입니다.")
        )
        
        val filteredStudies = allStudies.filter { study ->
            study.name.contains(query, ignoreCase = true) || 
            study.description.contains(query, ignoreCase = true)
        }
        
        studyAdapter.submitList(filteredStudies)
        Log.d("SearchFragment", "검색 결과: ${filteredStudies.size}개 스터디 발견")
    }

    private fun loadSampleData() {
        val sampleStudies = listOf(
            Study("알고리즘 스터디", "코딩테스트 대비를 위한 알고리즘 문제 해결 스터디입니다."),
            Study("안드로이드 개발 스터디", "Kotlin과 Android를 활용한 앱 개발을 함께 공부합니다."),
            Study("웹 개발 스터디", "React와 Node.js를 이용한 풀스택 웹 개발 스터디입니다."),
            Study("데이터 사이언스 스터디", "Python을 활용한 데이터 분석과 머신러닝을 학습합니다.")
        )
        studyAdapter.submitList(sampleStudies)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}