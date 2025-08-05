package com.example.hackathon

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.hackathon.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityLoginBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupClickListeners()
    }
    
    private fun setupClickListeners() {
        // 로그인 버튼 클릭
        binding.btnLogin.setOnClickListener {
            val email = binding.loginEmail.text.toString().trim()
            val phone = binding.loginPw.text.toString().trim()
            
            if (email.isEmpty() || phone.isEmpty()) {
                Toast.makeText(this, "모든 항목을 입력해주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            
            // 간단한 로그인 검증 (실제 앱에서는 서버 통신)
            if (email.isNotEmpty() && phone.isNotEmpty()) {
                Toast.makeText(this, "로그인 성공!", Toast.LENGTH_SHORT).show()
                
                // 메인 액티비티로 이동
                val intent = Intent(this, Graddy_main::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "로그인 정보를 확인해주세요", Toast.LENGTH_SHORT).show()
            }
        }
        
        // 회원가입 텍스트 클릭
        binding.textView2.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
    }
}