package com.example.hackathon.ui.profileedit

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.hackathon.R
import com.example.hackathon.databinding.FragmentProfileeditBinding

class ProfileEditFragment : Fragment() {

    private var _binding: FragmentProfileeditBinding? = null
    private lateinit var sharedPreferences: SharedPreferences

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val profileEditViewModel =
            ViewModelProvider(this).get(ProfileEditViewModel::class.java)

        _binding = FragmentProfileeditBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // SharedPreferences 초기화
        sharedPreferences = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

        setupViews()
        loadCurrentUserInfo()

        return root
    }

    private fun setupViews() {
        val nameEditText: EditText = binding.root.findViewById(R.id.edit_name)
        val emailEditText: EditText = binding.root.findViewById(R.id.edit_email)
        val phoneEditText: EditText = binding.root.findViewById(R.id.edit_pw)
        val saveButton: Button = binding.root.findViewById(R.id.btn_save)
        val cancelButton: Button = binding.root.findViewById(R.id.btn_cancel)

        // 저장 버튼 클릭
        saveButton?.setOnClickListener {
            val name = nameEditText?.text.toString().trim()
            val email = emailEditText?.text.toString().trim()
            val phone = phoneEditText?.text.toString().trim()

            if (validateInput(name, email)) {
                saveUserInfo(name, email, phone)
                Toast.makeText(context, "프로필이 수정되었습니다.", Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
            }
        }

        // 취소 버튼 클릭
        cancelButton?.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun loadCurrentUserInfo() {
        val currentName = sharedPreferences.getString("user_name", "")
        val currentEmail = sharedPreferences.getString("user_email", "")
        val currentPhone = sharedPreferences.getString("user_phone", "")

        binding.root.findViewById<EditText>(R.id.edit_name)?.setText(currentName)
        binding.root.findViewById<EditText>(R.id.edit_email)?.setText(currentEmail)
        binding.root.findViewById<EditText>(R.id.edit_pw)?.setText(currentPhone)
    }

    private fun validateInput(name: String, email: String): Boolean {
        if (name.isEmpty()) {
            Toast.makeText(context, "이름을 입력해주세요.", Toast.LENGTH_SHORT).show()
            return false
        }
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(context, "올바른 이메일을 입력해주세요.", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun saveUserInfo(name: String, email: String, phone: String) {
        with(sharedPreferences.edit()) {
            putString("user_name", name)
            putString("user_email", email)
            putString("user_phone", phone)
            apply()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}