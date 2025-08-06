package com.example.hackathon.ui.join

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.hackathon.R
import com.example.hackathon.data.RegisterResult
import com.example.hackathon.data.UserManager
import com.example.hackathon.databinding.FragmentJoinBinding
import com.example.hackathon.ui.join.JoinViewModel

class JoinFragment : Fragment() {
    private var _binding: FragmentJoinBinding? = null
    private val binding get() = _binding!!
    private lateinit var userManager: UserManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val joinViewModel = ViewModelProvider(this).get(JoinViewModel::class.java)
        _binding = FragmentJoinBinding.inflate(inflater, container, false)

        val root: View = binding.root
        userManager = UserManager(requireContext())

        // 회원가입 버튼 클릭 이벤트 처리
        binding.btnJoin.setOnClickListener {
            val id = binding.joinId.text.toString().trim()
            val password = binding.joinPw.text.toString().trim()
            val name = binding.joinName.text.toString().trim()
            val nickname = binding.joinNickname.text.toString().trim()
            val phone = binding.joinPhone.text.toString().trim()

            if (id.isBlank() || password.isBlank() || name.isBlank() || nickname.isBlank() || phone.isBlank()) {
                Toast.makeText(requireContext(), "모든 항목을 입력하세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 입력 유효성 검사
            if (!isValidInput(id, password, name, nickname, phone)) {
                return@setOnClickListener
            }

            // UserManager를 통한 회원가입
            when (val result = userManager.registerUser(id, password, name, nickname, phone)) {
                RegisterResult.SUCCESS -> {
                    Toast.makeText(requireContext(), "회원가입이 완료되었습니다!", Toast.LENGTH_SHORT).show()
                    // 로그인 페이지로 이동
                    findNavController().navigateUp()
                }
                RegisterResult.DUPLICATE_USER_ID -> {
                    Toast.makeText(requireContext(), "이미 존재하는 아이디입니다", Toast.LENGTH_SHORT).show()
                }
                RegisterResult.DUPLICATE_NICKNAME -> {
                    Toast.makeText(requireContext(), "이미 존재하는 닉네임입니다", Toast.LENGTH_SHORT).show()
                }
                RegisterResult.DUPLICATE_PHONE -> {
                    Toast.makeText(requireContext(), "이미 등록된 전화번호입니다", Toast.LENGTH_SHORT).show()
                }
            }
        }

        return root
    }
    
    private fun isValidInput(id: String, password: String, name: String, nickname: String, phone: String): Boolean {
        // ID 유효성 검사 (4-20자, 영문+숫자)
        if (id.length < 4 || id.length > 20) {
            Toast.makeText(requireContext(), "아이디는 4-20자로 입력해주세요", Toast.LENGTH_SHORT).show()
            return false
        }
        
        if (!id.matches(Regex("^[a-zA-Z0-9]+$"))) {
            Toast.makeText(requireContext(), "아이디는 영문과 숫자만 사용 가능합니다", Toast.LENGTH_SHORT).show()
            return false
        }
        
        // 비밀번호 유효성 검사 (4자 이상)
        if (password.length < 4) {
            Toast.makeText(requireContext(), "비밀번호는 4자 이상 입력해주세요", Toast.LENGTH_SHORT).show()
            return false
        }
        
        // 이름 유효성 검사 (2-10자)
        if (name.length < 2 || name.length > 10) {
            Toast.makeText(requireContext(), "이름은 2-10자로 입력해주세요", Toast.LENGTH_SHORT).show()
            return false
        }
        
        // 닉네임 유효성 검사 (2-15자)
        if (nickname.length < 2 || nickname.length > 15) {
            Toast.makeText(requireContext(), "닉네임은 2-15자로 입력해주세요", Toast.LENGTH_SHORT).show()
            return false
        }
        
        // 전화번호 유효성 검사 (숫자만, 10-11자)
        if (!phone.matches(Regex("^[0-9]{10,11}$"))) {
            Toast.makeText(requireContext(), "올바른 전화번호를 입력해주세요 (10-11자리 숫자)", Toast.LENGTH_SHORT).show()
            return false
        }
        
        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}