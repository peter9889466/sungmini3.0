package com.example.hackathon.ui.search

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hackathon.R
import com.example.hackathon.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var studyAdapter: StudyAdapter
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val root: View = binding.root

        sharedPreferences = requireContext().getSharedPreferences("study_prefs", Context.MODE_PRIVATE)
        
        Log.d("SearchFragment", "SearchFragment 생성됨")
        
        setupRecyclerView()
        setupSearchFunction()
        loadAllStudies()

        return root
    }

    private fun setupRecyclerView() {
        studyAdapter = StudyAdapter { study ->
            // 스터디 클릭 시 상세 페이지로 이동
            val bundle = bundleOf("studyId" to study.id)
            findNavController().navigate(R.id.action_search_to_study_detail, bundle)
        }
        binding.studyRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = studyAdapter
        }
    }

    private fun setupSearchFunction() {
        binding.searchButton.setOnClickListener {
            val query = binding.searchEditText.text.toString()
            Log.d("SearchFragment", "검색어: $query")
            
            if (query.isNotEmpty()) {
                searchStudies(query)
            } else {
                loadAllStudies()
            }
        }
    }

    private fun searchStudies(query: String) {
        val allStudies = loadStudiesFromStorage()
        
        val filteredStudies = allStudies.filter { study ->
            study.name.contains(query, ignoreCase = true) || 
            study.description.contains(query, ignoreCase = true) ||
            study.category.contains(query, ignoreCase = true) ||
            study.creator.contains(query, ignoreCase = true)
        }
        
        studyAdapter.submitList(filteredStudies)
        Log.d("SearchFragment", "검색 결과: ${filteredStudies.size}개 스터디 발견")
    }

    private fun loadAllStudies() {
        val allStudies = loadStudiesFromStorage()
        studyAdapter.submitList(allStudies)
        Log.d("SearchFragment", "전체 스터디: ${allStudies.size}개")
    }
    
    private fun loadStudiesFromStorage(): List<Study> {
        val studyIds = sharedPreferences.getStringSet("study_list", emptySet()) ?: emptySet()
        val studies = mutableListOf<Study>()
        
        for (id in studyIds) {
            val name = sharedPreferences.getString("study_${id}_name", "") ?: ""
            val description = sharedPreferences.getString("study_${id}_description", "") ?: ""
            val category = sharedPreferences.getString("study_${id}_category", "") ?: ""
            val creator = sharedPreferences.getString("study_${id}_creator", "") ?: ""
            
            if (name.isNotEmpty()) {
                studies.add(Study(id, name, description, category, creator))
            }
        }
        
        return studies
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}