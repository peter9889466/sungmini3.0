package com.example.hackathon.ui.addstudy

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.hackathon.databinding.FragmentAddStudyBinding

class AddStudyFragment : Fragment() {

    private var _binding: FragmentAddStudyBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddStudyBinding.inflate(inflater, container, false)
        val root: View = binding.root

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
        
        // TODO: 실제 스터디 생성 로직 구현
        Toast.makeText(context, "스터디가 생성되었습니다!", Toast.LENGTH_SHORT).show()
        clearForm()
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