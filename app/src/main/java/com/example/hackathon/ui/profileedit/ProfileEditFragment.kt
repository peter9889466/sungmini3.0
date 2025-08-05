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
        val idEditText: EditText = binding.root.findViewById(R.id.edit_id)
        val passwordEditText: EditText = binding.root.findViewById(R.id.edit_password)
        val nameEditText: EditText = binding.root.findViewById(R.id.edit_name)
        val nicknameEditText: EditText = binding.root.findViewById(R.id.edit_nickname)
        val phoneEditText: EditText = binding.root.findViewById(R.id.edit_phone)
        val saveButton: Button = binding.root.findViewById(R.id.btn_save)
        val cancelButton: Button = binding.root.findViewById(R.id.btn_cancel)

        // 저장 버튼 클릭
        saveButton?.setOnClickListener {
            val id = idEditText?.text.toString().trim()
            val password = passwordEditText?.text.toString().trim()
            val name = nameEditText?.text.toString().trim()
            val nickname = nicknameEditText?.text.toString().trim()
            val phone = phoneEditText?.text.toString().trim()

            if (validateInput(id, password, name, nickname)) {
                saveUserInfo(id, password, name, nickname, phone)
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
        val currentId = sharedPreferences.getString("user_id", "")
        val currentPassword = sharedPreferences.getString("user_password", "")
        val currentName = sharedPreferences.getString("user_name", "")
        val currentNickname = sharedPreferences.getString("user_nickname", "")
        val currentPhone = sharedPreferences.getString("user_phone", "")

        binding.root.findViewById<EditText>(R.id.edit_id)?.setText(currentId)
        binding.root.findViewById<EditText>(R.id.edit_password)?.setText(currentPassword)
        binding.root.findViewById<EditText>(R.id.edit_name)?.setText(currentName)
        binding.root.findViewById<EditText>(R.id.edit_nickname)?.setText(currentNickname)
        binding.root.findViewById<EditText>(R.id.edit_phone)?.setText(currentPhone)
    }

    private fun validateInput(id: String, password: String, name: String, nickname: String): Boolean {
        if (id.isEmpty()) {
            Toast.makeText(context, "ID를 입력해주세요.", Toast.LENGTH_SHORT).show()
            return false
        }
        if (password.isEmpty()) {
            Toast.makeText(context, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
            return false
        }
        if (name.isEmpty()) {
            Toast.makeText(context, "이름을 입력해주세요.", Toast.LENGTH_SHORT).show()
            return false
        }
        if (nickname.isEmpty()) {
            Toast.makeText(context, "닉네임을 입력해주세요.", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun saveUserInfo(id: String, password: String, name: String, nickname: String, phone: String) {
        with(sharedPreferences.edit()) {
            putString("user_id", id)
            putString("user_password", password)
            putString("user_name", name)
            putString("user_nickname", nickname)
            putString("user_phone", phone)
            apply()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}