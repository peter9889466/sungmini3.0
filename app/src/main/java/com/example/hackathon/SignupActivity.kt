package com.example.hackathon

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.hackathon.databinding.ActivitySignupBinding

class SignupActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivitySignupBinding
    private lateinit var sharedPreferences: SharedPreferences
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        setupClickListeners()
    }
    
    private fun setupClickListeners() {
        // 회원가입 버튼 클릭
        binding.btnSignup.setOnClickListener {
            val id = binding.signupId.text.toString().trim()
            val password = binding.signupPassword.text.toString().trim()
            val name = binding.signupName.text.toString().trim()
            val nickname = binding.signupNickname.text.toString().trim()
            val phone = binding.signupPhone.text.toString().trim()
            
            if (id.isEmpty() || password.isEmpty() || name.isEmpty() || nickname.isEmpty() || phone.isEmpty()) {
                Toast.makeText(this, "모든 항목을 입력해주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            
            // 사용자 정보 저장
            saveUserInfo(id, password, name, nickname, phone)
            
            // 회원가입 성공 처리 (실제로는 서버 통신)
            Toast.makeText(this, "회원가입이 완료되었습니다!", Toast.LENGTH_SHORT).show()
            
            // 로그인 페이지로 돌아가기
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        
        // 취소 버튼 클릭
        binding.btnCancel.setOnClickListener {
            finish() // 현재 액티비티 종료하고 로그인 페이지로 돌아가기
        }
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
}