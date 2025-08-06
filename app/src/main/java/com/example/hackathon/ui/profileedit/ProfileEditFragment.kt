package com.example.hackathon.ui.profileedit

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
import com.example.hackathon.data.UpdateResult
import com.example.hackathon.data.UserManager
import com.example.hackathon.databinding.FragmentProfileeditBinding

class ProfileEditFragment : Fragment() {

    private var _binding: FragmentProfileeditBinding? = null
    private lateinit var userManager: UserManager

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

        userManager = UserManager(requireContext())

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
            val name = nameEditText?.text.toString().trim()
            val nickname = nicknameEditText?.text.toString().trim()
            val phone = phoneEditText?.text.toString().trim()

            if (validateInput(name, nickname, phone)) {
                val currentUser = userManager.getCurrentUser()
                if (currentUser != null) {
                    when (val result = userManager.updateUser(currentUser.userId, name, nickname, phone)) {
                        UpdateResult.SUCCESS -> {
                            Toast.makeText(context, "프로필이 수정되었습니다.", Toast.LENGTH_SHORT).show()
                            findNavController().navigateUp()
                        }
                        UpdateResult.USER_NOT_FOUND -> {
                            Toast.makeText(context, "사용자를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show()
                        }
                        UpdateResult.DUPLICATE_NICKNAME -> {
                            Toast.makeText(context, "이미 존재하는 닉네임입니다.", Toast.LENGTH_SHORT).show()
                        }
                        UpdateResult.DUPLICATE_PHONE -> {
                            Toast.makeText(context, "이미 등록된 전화번호입니다.", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(context, "로그인이 필요합니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // 취소 버튼 클릭
        cancelButton?.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun loadCurrentUserInfo() {
        val currentUser = userManager.getCurrentUser()
        if (currentUser != null) {
            binding.root.findViewById<EditText>(R.id.edit_id)?.setText(currentUser.userId)
            binding.root.findViewById<EditText>(R.id.edit_password)?.setText("****") // 보안상 비밀번호는 표시하지 않음
            binding.root.findViewById<EditText>(R.id.edit_name)?.setText(currentUser.name)
            binding.root.findViewById<EditText>(R.id.edit_nickname)?.setText(currentUser.nickname)
            binding.root.findViewById<EditText>(R.id.edit_phone)?.setText(currentUser.phone)
            
            // ID와 비밀번호는 수정 불가능하게 설정
            binding.root.findViewById<EditText>(R.id.edit_id)?.isEnabled = false
            binding.root.findViewById<EditText>(R.id.edit_password)?.isEnabled = false
        }
    }

    private fun validateInput(name: String, nickname: String, phone: String): Boolean {
        if (name.isEmpty()) {
            Toast.makeText(context, "이름을 입력해주세요.", Toast.LENGTH_SHORT).show()
            return false
        }
        if (nickname.isEmpty()) {
            Toast.makeText(context, "닉네임을 입력해주세요.", Toast.LENGTH_SHORT).show()
            return false
        }
        if (phone.isEmpty()) {
            Toast.makeText(context, "전화번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
            return false
        }
        
        // 전화번호 유효성 검사
        if (!phone.matches(Regex("^[0-9]{10,11}$"))) {
            Toast.makeText(context, "올바른 전화번호를 입력해주세요 (10-11자리 숫자)", Toast.LENGTH_SHORT).show()
            return false
        }
        
        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}