package com.example.hackathon

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.hackathon.data.LoginResult
import com.example.hackathon.data.UserManager
import com.example.hackathon.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityLoginBinding
    private lateinit var userManager: UserManager
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        userManager = UserManager(this)
        
        // 이미 로그인된 상태라면 메인으로 이동
        if (userManager.isLoggedIn()) {
            navigateToMain()
            return
        }
        
        setupClickListeners()
    }
    
    private fun setupClickListeners() {
        // 로그인 버튼 클릭
        binding.btnLogin.setOnClickListener {
            val id = binding.loginId.text.toString().trim()
            val password = binding.loginPw.text.toString().trim()
            
            if (id.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "모든 항목을 입력해주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            
            // UserManager를 통한 로그인 검증
            when (val result = userManager.loginUser(id, password)) {
                LoginResult.SUCCESS -> {
                    Toast.makeText(this, "로그인 성공!", Toast.LENGTH_SHORT).show()
                    navigateToMain()
                }
                LoginResult.USER_NOT_FOUND -> {
                    Toast.makeText(this, "존재하지 않는 사용자입니다", Toast.LENGTH_SHORT).show()
                }
                LoginResult.WRONG_PASSWORD -> {
                    Toast.makeText(this, "비밀번호가 틀렸습니다", Toast.LENGTH_SHORT).show()
                }
            }
        }
        
        // 회원가입 텍스트 클릭
        binding.textView2.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
    }
    
    private fun navigateToMain() {
        val intent = Intent(this, Graddy_main::class.java)
        startActivity(intent)
        finish()
    }
}