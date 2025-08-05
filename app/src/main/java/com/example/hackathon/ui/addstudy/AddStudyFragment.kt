package com.example.hackathon.ui.addstudy

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.hackathon.R
import com.example.hackathon.databinding.FragmentAddStudyBinding

class AddStudyFragment : Fragment() {

    private var _binding: FragmentAddStudyBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddStudyBinding.inflate(inflater, container, false)
        val root: View = binding.root

        sharedPreferences = requireContext().getSharedPreferences("study_prefs", Context.MODE_PRIVATE)
        
        Log.d("AddStudyFragment", "AddStudyFragment 생성됨")
        
        setupClickListeners()

        return root
    }

    private fun setupClickListeners() {
        binding.btnCreateStudy.setOnClickListener {
            val studyName = binding.editStudyName.text.toString().trim()
            val studyDescription = binding.editStudyDescription.text.toString().trim()
            val studyCategory = binding.spinnerCategory.selectedItem.toString()

            if (validateInput(studyName, studyDescription)) {
                createStudy(studyName, studyDescription, studyCategory)
            }
        }

        binding.btnCancel.setOnClickListener {
            clearForm()
        }
    }

    private fun validateInput(name: String, description: String): Boolean {
        if (name.isEmpty()) {
            Toast.makeText(context, "스터디 이름을 입력해주세요.", Toast.LENGTH_SHORT).show()
            return false
        }
        if (description.isEmpty()) {
            Toast.makeText(context, "스터디 설명을 입력해주세요.", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun createStudy(name: String, description: String, category: String) {
        Log.d("AddStudyFragment", "스터디 생성 - 이름: $name, 설명: $description, 카테고리: $category")
        
        // 현재 사용자 정보 가져오기
        val userPrefs = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val creatorNickname = userPrefs.getString("user_nickname", "익명")
        
        // 스터디 정보 저장 (임시로 SharedPreferences 사용)
        val studyId = System.currentTimeMillis().toString() // 임시 ID
        saveStudyToLocal(studyId, name, description, category, creatorNickname ?: "익명")
        
        Toast.makeText(context, "스터디가 생성되었습니다!", Toast.LENGTH_SHORT).show()
        
        // 홈페이지로 이동
        findNavController().navigate(R.id.botHome)
    }
    
    private fun saveStudyToLocal(id: String, name: String, description: String, category: String, creator: String) {
        // 기존 스터디 목록 가져오기
        val existingStudies = sharedPreferences.getStringSet("study_list", mutableSetOf()) ?: mutableSetOf()
        val newStudies = existingStudies.toMutableSet()
        newStudies.add(id)
        
        // 스터디 목록 업데이트
        with(sharedPreferences.edit()) {
            putStringSet("study_list", newStudies)
            putString("study_${id}_name", name)
            putString("study_${id}_description", description)
            putString("study_${id}_category", category)
            putString("study_${id}_creator", creator)
            putLong("study_${id}_created_time", System.currentTimeMillis())
            apply()
        }
    }

    private fun clearForm() {
        binding.editStudyName.text?.clear()
        binding.editStudyDescription.text?.clear()
        binding.spinnerCategory.setSelection(0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}