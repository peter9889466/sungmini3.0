package com.example.hackathon.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.hackathon.databinding.FragmentLoginBinding
import com.example.hackathon.ui.login.LoginViewModel

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    // 테스트용 계정 정보
    private val validEmail = "111"
    private val validPhone = "111"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // 로그인 버튼 클릭 시
        binding.btnLogin.setOnClickListener {
            val email = binding.loginId.text.toString()
            val phone = binding.loginPw.text.toString()

            if (email.isBlank() || phone.isBlank()) {
                Toast.makeText(requireContext(), "모든 항목을 입력하세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (email == validEmail && phone == validPhone) {
                Toast.makeText(requireContext(), "로그인 성공: $email", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(requireContext(), "로그인 실패!", Toast.LENGTH_SHORT).show()
            }
        }

//        // "회원정보가 없으신가요?" 클릭 시 회원가입 화면으로 이동
//        binding.textView2.setOnClickListener {
//            findNavController().navigate(R.id.action_nav_login_to_nav_join)
//        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}