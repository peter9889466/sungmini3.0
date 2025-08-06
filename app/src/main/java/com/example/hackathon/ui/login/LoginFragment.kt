package com.example.hackathon.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.hackathon.R
import com.example.hackathon.data.LoginResult
import com.example.hackathon.data.UserManager
import com.example.hackathon.databinding.FragmentLoginBinding
import com.example.hackathon.ui.login.LoginViewModel

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var userManager: UserManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val root: View = binding.root

        userManager = UserManager(requireContext())

        // 로그인 버튼 클릭 시
        binding.btnLogin.setOnClickListener {
            val id = binding.loginId.text.toString().trim()
            val password = binding.loginPw.text.toString().trim()

            if (id.isBlank() || password.isBlank()) {
                Toast.makeText(requireContext(), "모든 항목을 입력하세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // UserManager를 통한 로그인 검증
            when (val result = userManager.loginUser(id, password)) {
                LoginResult.SUCCESS -> {
                    Toast.makeText(requireContext(), "로그인 성공: $id", Toast.LENGTH_SHORT).show()
                    // 홈 화면으로 이동
                    findNavController().navigate(R.id.botHome)
                }
                LoginResult.USER_NOT_FOUND -> {
                    Toast.makeText(requireContext(), "존재하지 않는 사용자입니다", Toast.LENGTH_SHORT).show()
                }
                LoginResult.WRONG_PASSWORD -> {
                    Toast.makeText(requireContext(), "비밀번호가 틀렸습니다", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // "회원정보가 없으신가요?" 클릭 시 회원가입 화면으로 이동
        binding.textView2.setOnClickListener {
            findNavController().navigate(R.id.action_nav_login_to_nav_join)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}