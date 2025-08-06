package com.example.hackathon.ui.addstudy

import android.app.DatePickerDialog
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
import com.example.hackathon.data.UserManager
import com.example.hackathon.databinding.FragmentAddStudyBinding
import java.text.SimpleDateFormat
import java.util.*

class AddStudyFragment : Fragment() {

    private var _binding: FragmentAddStudyBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var userManager: UserManager
    
    private var startDate: Calendar? = null
    private var endDate: Calendar? = null
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddStudyBinding.inflate(inflater, container, false)
        val root: View = binding.root

        sharedPreferences = requireContext().getSharedPreferences("study_prefs", Context.MODE_PRIVATE)
        userManager = UserManager(requireContext())
        
        Log.d("AddStudyFragment", "AddStudyFragment 생성됨")
        
        setupClickListeners()
        setupDateButtons()

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
    
    private fun setupDateButtons() {
        // 시작일 선택 버튼
        binding.btnStartDate.setOnClickListener {
            showDatePicker(true) { selectedDate ->
                startDate = selectedDate
                binding.btnStartDate.text = dateFormat.format(selectedDate.time)
                updateDurationText()
                
                // 시작일이 선택되면 종료일의 최소 날짜를 시작일로 설정
                if (endDate != null && endDate!!.before(startDate)) {
                    endDate = null
                    binding.btnEndDate.text = "날짜 선택"
                    updateDurationText()
                }
            }
        }
        
        // 종료일 선택 버튼
        binding.btnEndDate.setOnClickListener {
            showDatePicker(false) { selectedDate ->
                if (startDate != null && selectedDate.before(startDate)) {
                    Toast.makeText(context, "종료일은 시작일보다 늦어야 합니다", Toast.LENGTH_SHORT).show()
                    return@showDatePicker
                }
                
                endDate = selectedDate
                binding.btnEndDate.text = dateFormat.format(selectedDate.time)
                updateDurationText()
            }
        }
    }
    
    private fun showDatePicker(isStartDate: Boolean, onDateSelected: (Calendar) -> Unit) {
        val calendar = Calendar.getInstance()
        
        // 최소 날짜 설정
        val minDate = if (isStartDate) {
            Calendar.getInstance() // 오늘 날짜
        } else {
            startDate ?: Calendar.getInstance() // 시작일이 있으면 시작일, 없으면 오늘
        }
        
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                val selectedCalendar = Calendar.getInstance()
                selectedCalendar.set(year, month, dayOfMonth)
                onDateSelected(selectedCalendar)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        
        // 최소 날짜 설정
        datePickerDialog.datePicker.minDate = minDate.timeInMillis
        datePickerDialog.show()
    }
    
    private fun updateDurationText() {
        when {
            startDate == null && endDate == null -> {
                binding.textStudyDuration.text = "기간을 선택해주세요"
            }
            startDate != null && endDate == null -> {
                binding.textStudyDuration.text = "${dateFormat.format(startDate!!.time)} ~ 종료일 선택"
            }
            startDate != null && endDate != null -> {
                val diffInMillis = endDate!!.timeInMillis - startDate!!.timeInMillis
                val diffInDays = (diffInMillis / (1000 * 60 * 60 * 24)).toInt() + 1
                binding.textStudyDuration.text = "${dateFormat.format(startDate!!.time)} ~ ${dateFormat.format(endDate!!.time)} (${diffInDays}일)"
            }
            else -> {
                binding.textStudyDuration.text = "기간을 다시 선택해주세요"
            }
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
        if (startDate == null) {
            Toast.makeText(context, "시작일을 선택해주세요.", Toast.LENGTH_SHORT).show()
            return false
        }
        if (endDate == null) {
            Toast.makeText(context, "종료일을 선택해주세요.", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun createStudy(name: String, description: String, category: String) {
        Log.d("AddStudyFragment", "스터디 생성 - 이름: $name, 설명: $description, 카테고리: $category")
        
        // UserManager를 통해 현재 사용자 정보 가져오기
        val currentUser = userManager.getCurrentUser()
        if (currentUser == null) {
            Toast.makeText(context, "로그인이 필요합니다.", Toast.LENGTH_SHORT).show()
            return
        }
        
        // 스터디 정보 저장 (임시로 SharedPreferences 사용)
        val studyId = System.currentTimeMillis().toString() // 임시 ID
        val startDateStr = if (startDate != null) dateFormat.format(startDate!!.time) else ""
        val endDateStr = if (endDate != null) dateFormat.format(endDate!!.time) else ""
        
        saveStudyToLocal(studyId, name, description, category, currentUser.nickname, startDateStr, endDateStr)
        
        Toast.makeText(context, "스터디가 생성되었습니다!", Toast.LENGTH_SHORT).show()
        
        // 홈페이지로 이동
        findNavController().navigate(R.id.botHome)
    }
    
    private fun saveStudyToLocal(id: String, name: String, description: String, category: String, creator: String, startDate: String, endDate: String) {
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
            putString("study_${id}_start_date", startDate)
            putString("study_${id}_end_date", endDate)
            putLong("study_${id}_created_time", System.currentTimeMillis())
            apply()
        }
    }

    private fun clearForm() {
        binding.editStudyName.text?.clear()
        binding.editStudyDescription.text?.clear()
        binding.spinnerCategory.setSelection(0)
        
        // 날짜 초기화
        startDate = null
        endDate = null
        binding.btnStartDate.text = "날짜 선택"
        binding.btnEndDate.text = "날짜 선택"
        binding.textStudyDuration.text = "기간을 선택해주세요"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}