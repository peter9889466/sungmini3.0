package com.example.hackathon

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.hackathon.data.RegisterResult
import com.example.hackathon.data.UserManager
import com.example.hackathon.databinding.ActivitySignupBinding

class SignupActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivitySignupBinding
    private lateinit var userManager: UserManager
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        userManager = UserManager(this)
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
            
            // 입력 유효성 검사
            if (!isValidInput(id, password, name, nickname, phone)) {
                return@setOnClickListener
            }
            
            // UserManager를 통한 회원가입
            when (val result = userManager.registerUser(id, password, name, nickname, phone)) {
                RegisterResult.SUCCESS -> {
                    Toast.makeText(this, "회원가입이 완료되었습니다!", Toast.LENGTH_SHORT).show()
                    navigateToLogin()
                }
                RegisterResult.DUPLICATE_USER_ID -> {
                    Toast.makeText(this, "이미 존재하는 아이디입니다", Toast.LENGTH_SHORT).show()
                }
                RegisterResult.DUPLICATE_NICKNAME -> {
                    Toast.makeText(this, "이미 존재하는 닉네임입니다", Toast.LENGTH_SHORT).show()
                }
                RegisterResult.DUPLICATE_PHONE -> {
                    Toast.makeText(this, "이미 등록된 전화번호입니다", Toast.LENGTH_SHORT).show()
                }
            }
        }
        
        // 취소 버튼 클릭
        binding.btnCancel.setOnClickListener {
            finish()
        }
    }
    
    private fun isValidInput(id: String, password: String, name: String, nickname: String, phone: String): Boolean {
        // ID 유효성 검사 (4-20자, 영문+숫자)
        if (id.length < 4 || id.length > 20) {
            Toast.makeText(this, "아이디는 4-20자로 입력해주세요", Toast.LENGTH_SHORT).show()
            return false
        }
        
        if (!id.matches(Regex("^[a-zA-Z0-9]+$"))) {
            Toast.makeText(this, "아이디는 영문과 숫자만 사용 가능합니다", Toast.LENGTH_SHORT).show()
            return false
        }
        
        // 비밀번호 유효성 검사 (4자 이상)
        if (password.length < 4) {
            Toast.makeText(this, "비밀번호는 4자 이상 입력해주세요", Toast.LENGTH_SHORT).show()
            return false
        }
        
        // 이름 유효성 검사 (2-10자)
        if (name.length < 2 || name.length > 10) {
            Toast.makeText(this, "이름은 2-10자로 입력해주세요", Toast.LENGTH_SHORT).show()
            return false
        }
        
        // 닉네임 유효성 검사 (2-15자)
        if (nickname.length < 2 || nickname.length > 15) {
            Toast.makeText(this, "닉네임은 2-15자로 입력해주세요", Toast.LENGTH_SHORT).show()
            return false
        }
        
        // 전화번호 유효성 검사 (숫자만, 10-11자)
        if (!phone.matches(Regex("^[0-9]{10,11}$"))) {
            Toast.makeText(this, "올바른 전화번호를 입력해주세요 (10-11자리 숫자)", Toast.LENGTH_SHORT).show()
            return false
        }
        
        return true
    }
    
    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}